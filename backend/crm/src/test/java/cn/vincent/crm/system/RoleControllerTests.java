package cn.vincent.crm.system;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.system.dto.request.RoleAddRequest;
import cn.vincent.crm.system.dto.request.RoleUpdateRequest;
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
 * 系统角色管理控制器集成测试
 * <p>
 * 测试角色的增删改查及权限分配等接口。
 * 接口路径：/role/*
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleControllerTests extends BaseTest {

    /** 保存新增的角色 ID，供后续测试使用 */
    private static String roleId;

    /**
     * 测试新增角色 - 传入合法数据时应成功返回角色实体
     */
    @Test
    @Order(1)
    void testAddRole_WhenValidRequest_ShouldReturnRole() throws Exception {
        // 构造新增角色请求
        RoleAddRequest request = new RoleAddRequest();
        request.setName("测试角色-" + System.currentTimeMillis());
        request.setDescription("集成测试创建的角色");
        request.setDataScope("all");

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/role/add")
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
        roleId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试角色列表 - 应返回角色列表数组
     */
    @Test
    @Order(2)
    void testListRole_ShouldReturnRoleList() throws Exception {
        // 执行请求并断言响应为数组格式
        mockMvc.perform(get("/crm/v1/role/list")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                // 角色列表返回数组格式，包含默认的 admin 角色
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试查询角色权限 - 角色存在时应返回权限 ID 列表
     */
    @Test
    @Order(3)
    void testGetRolePermissions_WhenExists_ShouldReturnPermissions() throws Exception {
        // 确保已有角色
        if (roleId == null) {
            testAddRole_WhenValidRequest_ShouldReturnRole();
        }

        // 执行权限查询
        mockMvc.perform(get("/crm/v1/role/permissions/" + roleId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                // 新角色无权限时返回空数组
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试更新角色 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateRole_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有角色
        if (roleId == null) {
            testAddRole_WhenValidRequest_ShouldReturnRole();
        }

        // 构造更新请求
        RoleUpdateRequest updateRequest = new RoleUpdateRequest();
        updateRequest.setId(roleId);
        updateRequest.setName("更新后角色名称-" + System.currentTimeMillis());
        updateRequest.setDescription("集成测试更新的角色");
        updateRequest.setDataScope("self");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/role/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(roleId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试删除角色 - 角色存在时应成功删除
     */
    @Test
    @Order(5)
    void testDeleteRole_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的角色
        RoleAddRequest request = new RoleAddRequest();
        request.setName("待删除角色-" + System.currentTimeMillis());
        request.setDescription("待删除");

        String createResponse = mockMvc.perform(post("/crm/v1/role/add")
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
        mockMvc.perform(get("/crm/v1/role/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增角色校验 - 角色名称为空时应返回校验错误
     */
    @Test
    @Order(6)
    void testAddRole_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        RoleAddRequest request = new RoleAddRequest();
        request.setDescription("没有名称的角色");
        // name 故意不填

        mockMvc.perform(post("/crm/v1/role/add")
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
    @Order(7)
    void testListRole_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        // 不携带认证 Cookie
        mockMvc.perform(get("/crm/v1/role/list"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
