package cn.vincent.crm.system;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.system.dto.request.UserAddRequest;
import cn.vincent.crm.system.dto.request.UserPageRequest;
import cn.vincent.crm.system.dto.request.UserUpdateRequest;
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
 * 系统用户管理控制器集成测试
 * <p>
 * 测试用户的增删改查、启用/禁用等接口。
 * 需要以管理员身份登录后执行（具有 SYSTEM_USER_* 系列权限）。
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTests extends BaseTest {

    /** 保存新增的用户 ID，供后续测试使用 */
    private static String newUserId;

    /** 测试用的用户名（唯一性保证） */
    private static final String TEST_USERNAME = "test_user_" + System.currentTimeMillis();

    /**
     * 测试新增用户 - 传入合法数据时应成功返回用户实体
     */
    @Test
    @Order(1)
    void testAddUser_WhenValidRequest_ShouldReturnUser() throws Exception {
        // 构造新增用户请求
        UserAddRequest request = new UserAddRequest();
        request.setUsername(TEST_USERNAME);
        request.setPassword("Test@123456");
        request.setName("测试用户");
        request.setEmail("testuser@example.com");
        request.setPhone("13700137001");

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value(TEST_USERNAME))
                .andExpect(jsonPath("$.data.enable").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 保存 ID 供后续测试使用
        newUserId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试分页查询用户 - 应返回分页格式的用户列表
     */
    @Test
    @Order(2)
    void testPageUser_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        UserPageRequest request = new UserPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/user/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.total").isNumber());
    }

    /**
     * 测试关键字搜索用户 - 按用户名搜索应返回匹配结果
     */
    @Test
    @Order(3)
    void testPageUser_WithKeyword_ShouldReturnFilteredResult() throws Exception {
        // 确保先有测试用户
        if (newUserId == null) {
            testAddUser_WhenValidRequest_ShouldReturnUser();
        }

        UserPageRequest request = new UserPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);
        request.setKeyword("test_user");

        mockMvc.perform(post("/crm/v1/user/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list").isArray());
    }

    /**
     * 测试更新用户 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateUser_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保有可更新的用户
        if (newUserId == null) {
            testAddUser_WhenValidRequest_ShouldReturnUser();
        }

        // 构造更新请求
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(newUserId);
        updateRequest.setName("更新后姓名-" + System.currentTimeMillis());
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhone("18888888888");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(newUserId));
    }

    /**
     * 测试禁用用户 - 禁用操作应成功执行
     */
    @Test
    @Order(5)
    void testDisableUser_WhenExists_ShouldDisable() throws Exception {
        // 确保有可操作的用户
        if (newUserId == null) {
            testAddUser_WhenValidRequest_ShouldReturnUser();
        }

        // 执行禁用
        mockMvc.perform(get("/crm/v1/user/disable/" + newUserId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试启用用户 - 启用操作应成功执行
     */
    @Test
    @Order(6)
    void testEnableUser_WhenExists_ShouldEnable() throws Exception {
        // 确保有可操作的用户
        if (newUserId == null) {
            testAddUser_WhenValidRequest_ShouldReturnUser();
        }

        // 执行启用
        mockMvc.perform(get("/crm/v1/user/enable/" + newUserId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试删除用户 - 用户存在时应成功删除
     */
    @Test
    @Order(7)
    void testDeleteUser_WhenExists_ShouldDelete() throws Exception {
        // 先新增一个专门用于删除的用户
        UserAddRequest request = new UserAddRequest();
        request.setUsername("delete_user_" + System.currentTimeMillis());
        request.setPassword("Test@123456");
        request.setName("待删除用户");

        String createResponse = mockMvc.perform(post("/crm/v1/user/add")
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
        mockMvc.perform(get("/crm/v1/user/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增用户校验 - 用户名为空时应返回校验错误
     */
    @Test
    @Order(8)
    void testAddUser_WhenUsernameBlank_ShouldReturnValidationError() throws Exception {
        // 缺少 username 字段
        UserAddRequest request = new UserAddRequest();
        request.setPassword("Test@123456");
        request.setName("测试用户");

        mockMvc.perform(post("/crm/v1/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(422));
    }

    /**
     * 测试重复用户名 - 同一组织内相同用户名应返回错误
     */
    @Test
    @Order(9)
    void testAddUser_WhenUsernameAlreadyExists_ShouldReturnError() throws Exception {
        // 使用已存在的 admin 用户名
        UserAddRequest request = new UserAddRequest();
        request.setUsername("admin");  // 系统已有此用户名
        request.setPassword("Test@123456");
        request.setName("测试用户");

        mockMvc.perform(post("/crm/v1/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                // 期望返回业务错误（HTTP 200 + code != 200）
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(org.hamcrest.Matchers.not(200)));
    }
}
