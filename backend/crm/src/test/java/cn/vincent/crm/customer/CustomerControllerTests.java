package cn.vincent.crm.customer;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.customer.dto.request.CustomerAddRequest;
import cn.vincent.crm.customer.dto.request.CustomerPageRequest;
import cn.vincent.crm.customer.dto.request.CustomerUpdateRequest;
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
 * 客户管理控制器集成测试
 * <p>
 * 测试客户增删改查相关接口。
 * CustomerController 映射路径为 /customer/*
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTests extends BaseTest {

    /** 保存新增的客户 ID，供后续测试使用 */
    private static String customerId;

    /**
     * 测试新增客户 - 传入合法数据时应成功返回客户实体
     */
    @Test
    @Order(1)
    void testAddCustomer_WhenValidRequest_ShouldReturnCustomer() throws Exception {
        // 构造新增客户请求
        CustomerAddRequest request = new CustomerAddRequest();
        request.setName("测试客户-" + System.currentTimeMillis());

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/customer/add")
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
        customerId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试分页查询客户 - 应返回分页格式的客户列表
     */
    @Test
    @Order(2)
    void testPageCustomer_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        CustomerPageRequest request = new CustomerPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/customer/page")
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
     * 测试查询客户详情 - 客户存在时应返回详情数据
     */
    @Test
    @Order(3)
    void testGetCustomer_WhenExists_ShouldReturnDetail() throws Exception {
        // 确保已有客户
        if (customerId == null) {
            testAddCustomer_WhenValidRequest_ShouldReturnCustomer();
        }

        // 执行详情查询
        mockMvc.perform(get("/crm/v1/customer/get/" + customerId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(customerId));
    }

    /**
     * 测试更新客户 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateCustomer_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有客户
        if (customerId == null) {
            testAddCustomer_WhenValidRequest_ShouldReturnCustomer();
        }

        // 构造更新请求
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest();
        updateRequest.setId(customerId);
        updateRequest.setName("更新后客户名称-" + System.currentTimeMillis());

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(customerId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试删除客户 - 客户存在时应成功删除
     */
    @Test
    @Order(5)
    void testDeleteCustomer_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的客户
        CustomerAddRequest request = new CustomerAddRequest();
        request.setName("待删除客户-" + System.currentTimeMillis());

        String createResponse = mockMvc.perform(post("/crm/v1/customer/add")
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
        mockMvc.perform(get("/crm/v1/customer/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增客户校验 - 客户名称为空时应返回校验错误
     */
    @Test
    @Order(6)
    void testAddCustomer_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        CustomerAddRequest request = new CustomerAddRequest();
        // name 故意不填

        mockMvc.perform(post("/crm/v1/customer/add")
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
    void testPageCustomer_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        CustomerPageRequest request = new CustomerPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 不携带认证 Cookie
        mockMvc.perform(post("/crm/v1/customer/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
