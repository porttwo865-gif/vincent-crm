package cn.vincent.crm.workbench;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.workbench.dto.request.WorkbenchActivityPageRequest;
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
 * 工作台控制器集成测试
 * <p>
 * 测试工作台的业绩统计、待办事项、最近动态等接口。
 * 接口路径：/workbench/*
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkbenchControllerTests extends BaseTest {

    /**
     * 测试业绩统计接口 - 应返回 WorkbenchOverviewResponse 格式数据
     */
    @Test
    @Order(1)
    void testGetStats_WhenAuthenticated_ShouldReturnOverview() throws Exception {
        // 执行请求并断言响应结构
        mockMvc.perform(get("/crm/v1/workbench/stats")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                // 响应格式：{code, message, data: {clueCount, customerCount, ...}}
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isMap());
    }

    /**
     * 测试待办事项接口 - 应返回数组格式的待办列表
     */
    @Test
    @Order(2)
    void testGetTodo_WhenAuthenticated_ShouldReturnTodoList() throws Exception {
        // 执行请求并断言响应为数组格式
        mockMvc.perform(get("/crm/v1/workbench/todo")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                // /todo 返回数组格式
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试最近动态接口（非分页）- 应返回数组格式的动态列表
     */
    @Test
    @Order(3)
    void testGetRecent_WhenAuthenticated_ShouldReturnRecentList() throws Exception {
        // 执行请求并断言响应为数组格式
        mockMvc.perform(get("/crm/v1/workbench/recent")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试工作台动态分页接口 - 应返回分页格式的动态列表
     */
    @Test
    @Order(4)
    void testListActivity_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        WorkbenchActivityPageRequest request = new WorkbenchActivityPageRequest();
        request.setPageNum(1);
        request.setPageSize(20);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/workbench/activity")
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
     * 测试业绩概览接口（/overview 与 /stats 同源）- 应返回相同结构数据
     */
    @Test
    @Order(5)
    void testGetOverview_WhenAuthenticated_ShouldReturnOverview() throws Exception {
        // 执行请求并断言响应结构
        mockMvc.perform(get("/crm/v1/workbench/overview")
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isMap());
    }

    /**
     * 测试未认证访问 stats 接口 - 不携带 Cookie 时应返回 401
     */
    @Test
    @Order(6)
    void testGetStats_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        // 不携带认证 Cookie
        mockMvc.perform(get("/crm/v1/workbench/stats"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    /**
     * 测试未认证访问 todo 接口 - 不携带 Cookie 时应返回 401
     */
    @Test
    @Order(7)
    void testGetTodo_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        // 不携带认证 Cookie
        mockMvc.perform(get("/crm/v1/workbench/todo"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
