package cn.vincent.crm.customer;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.customer.dto.request.ContactAddRequest;
import cn.vincent.crm.customer.dto.request.ContactUpdateRequest;
import cn.vincent.crm.customer.dto.request.CustomerAddRequest;
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
 * 联系人管理控制器集成测试
 * <p>
 * 测试联系人的增删改查接口，联系人归属于客户。
 * 接口路径：/contact/*
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactControllerTests extends BaseTest {

    /** 测试用客户 ID（联系人依赖客户存在）*/
    private static String testCustomerId;

    /** 保存新增的联系人 ID，供后续测试使用 */
    private static String contactId;

    /**
     * 前置：新增一个客户，用于联系人测试
     */
    @Test
    @Order(1)
    void testSetupCustomer_ShouldCreateCustomerForContact() throws Exception {
        // 构造新增客户请求
        CustomerAddRequest request = new CustomerAddRequest();
        request.setName("联系人测试客户-" + System.currentTimeMillis());

        // 执行请求并保存客户 ID
        String responseBody = mockMvc.perform(post("/crm/v1/customer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 保存客户 ID 供后续测试使用
        testCustomerId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试新增联系人 - 传入合法数据时应成功返回联系人实体
     */
    @Test
    @Order(2)
    void testAddContact_WhenValidRequest_ShouldReturnContact() throws Exception {
        // 确保已有客户
        if (testCustomerId == null) {
            testSetupCustomer_ShouldCreateCustomerForContact();
        }

        // 构造新增联系人请求
        ContactAddRequest request = new ContactAddRequest();
        request.setCustomerId(testCustomerId);
        request.setName("张三");
        request.setPhone("13800138001");
        request.setEmail("zhangsan@example.com");
        request.setPosition("技术总监");
        request.setIsPrimary(true);

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/contact/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("张三"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 保存联系人 ID 供后续测试使用
        contactId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试联系人列表 - 根据客户 ID 查询应返回联系人列表
     */
    @Test
    @Order(3)
    void testListContact_ByCustomerId_ShouldReturnList() throws Exception {
        // 确保已有客户和联系人
        if (testCustomerId == null) {
            testSetupCustomer_ShouldCreateCustomerForContact();
        }
        if (contactId == null) {
            testAddContact_WhenValidRequest_ShouldReturnContact();
        }

        // 执行列表查询
        mockMvc.perform(get("/crm/v1/contact/list/" + testCustomerId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试更新联系人 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateContact_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有联系人
        if (contactId == null) {
            testSetupCustomer_ShouldCreateCustomerForContact();
            testAddContact_WhenValidRequest_ShouldReturnContact();
        }

        // 构造更新请求
        ContactUpdateRequest updateRequest = new ContactUpdateRequest();
        updateRequest.setId(contactId);
        updateRequest.setName("李四（已更新）");
        updateRequest.setPhone("13900139002");
        updateRequest.setEmail("lisi@example.com");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/contact/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(contactId))
                .andExpect(jsonPath("$.data.name").value("李四（已更新）"));
    }

    /**
     * 测试删除联系人 - 联系人存在时应成功删除
     */
    @Test
    @Order(5)
    void testDeleteContact_WhenExists_ShouldDelete() throws Exception {
        // 确保已有客户
        if (testCustomerId == null) {
            testSetupCustomer_ShouldCreateCustomerForContact();
        }

        // 先新增一条专门用于删除的联系人
        ContactAddRequest request = new ContactAddRequest();
        request.setCustomerId(testCustomerId);
        request.setName("待删除联系人");
        request.setPhone("18888888888");

        String createResponse = mockMvc.perform(post("/crm/v1/contact/add")
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
        mockMvc.perform(get("/crm/v1/contact/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增联系人校验 - 联系人姓名为空时应返回校验错误
     */
    @Test
    @Order(6)
    void testAddContact_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 确保已有客户
        if (testCustomerId == null) {
            testSetupCustomer_ShouldCreateCustomerForContact();
        }

        // 构造缺少必填字段的请求（缺少 name）
        ContactAddRequest request = new ContactAddRequest();
        request.setCustomerId(testCustomerId);
        // name 故意不填

        mockMvc.perform(post("/crm/v1/contact/add")
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
    void testListContact_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        // 不携带认证 Cookie，访问联系人列表
        mockMvc.perform(get("/crm/v1/contact/list/some-customer-id"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
