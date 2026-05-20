package cn.vincent.crm.system;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.system.dto.request.DepartmentAddRequest;
import cn.vincent.crm.system.dto.request.DepartmentUpdateRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 系统部门管理控制器集成测试
 * <p>
 * 测试部门的增删改查及树形结构查询等接口。
 * 接口路径：/department/*（DepartmentController）
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeptControllerTests extends BaseTest {

    /** 保存新增的部门 ID，供后续测试使用 */
    private static String deptId;

    /**
     * 测试新增部门 - 传入合法数据时应成功返回部门实体
     */
    @Test
    @Order(1)
    void testAddDept_WhenValidRequest_ShouldReturnDept() throws Exception {
        // 构造新增部门请求
        DepartmentAddRequest request = new DepartmentAddRequest();
        request.setName("测试部门-" + System.currentTimeMillis());
        request.setSort(1);

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/department/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 保存 ID 供后续测试使用
        deptId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试部门树 - 应返回树形结构的部门列表
     */
    @Test
    @Order(2)
    void testGetDeptTree_ShouldReturnTreeList() throws Exception {
        // 执行请求并断言响应为数组格式（树形结构）
        mockMvc.perform(get("/crm/v1/department/tree")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                // 部门树返回数组格式，包含默认的根部门
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试更新部门 - 传入合法数据时应成功更新
     */
    @Test
    @Order(3)
    void testUpdateDept_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有部门
        if (deptId == null) {
            testAddDept_WhenValidRequest_ShouldReturnDept();
        }

        // 构造更新请求
        DepartmentUpdateRequest updateRequest = new DepartmentUpdateRequest();
        updateRequest.setId(deptId);
        updateRequest.setName("更新后部门名称-" + System.currentTimeMillis());
        updateRequest.setSort(2);

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/department/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(deptId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试删除部门 - 部门存在时应成功删除
     */
    @Test
    @Order(4)
    void testDeleteDept_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的部门
        DepartmentAddRequest request = new DepartmentAddRequest();
        request.setName("待删除部门-" + System.currentTimeMillis());
        request.setSort(99);

        String createResponse = mockMvc.perform(post("/crm/v1/department/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String deleteId = objectMapper.readTree(createResponse).at("/data/id").asText();

        // 执行删除
        mockMvc.perform(get("/crm/v1/department/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增部门校验 - 部门名称为空时应返回校验错误
     */
    @Test
    @Order(5)
    void testAddDept_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        DepartmentAddRequest request = new DepartmentAddRequest();
        request.setSort(1);
        // name 故意不填

        mockMvc.perform(post("/crm/v1/department/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(422));
    }

    /**
     * 测试未认证访问 - 不携带 Cookie 时应返回 401
     */
    @Test
    @Order(6)
    void testGetDeptTree_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        // 不携带认证 Cookie
        mockMvc.perform(get("/crm/v1/department/tree"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
