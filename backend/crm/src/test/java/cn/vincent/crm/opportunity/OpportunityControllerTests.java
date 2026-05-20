package cn.vincent.crm.opportunity;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.opportunity.dto.request.OpportunityAddRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityPageRequest;
import cn.vincent.crm.opportunity.dto.request.OpportunityUpdateRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商机管理控制器集成测试
 * <p>
 * 测试商机增删改查、阶段变更等接口。
 * 接口路径：/opportunity/*
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OpportunityControllerTests extends BaseTest {

    /** 保存新增的商机 ID，供后续测试使用 */
    private static String opportunityId;

    /**
     * 测试新增商机 - 传入合法数据时应成功返回商机实体
     */
    @Test
    @Order(1)
    void testAddOpportunity_WhenValidRequest_ShouldReturnOpportunity() throws Exception {
        // 构造新增商机请求
        OpportunityAddRequest request = new OpportunityAddRequest();
        request.setName("测试商机-" + System.currentTimeMillis());
        request.setAmount(new BigDecimal("500000.00"));
        request.setExpectedCloseTime(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000);
        request.setRemark("集成测试创建的商机");

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/opportunity/add")
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
        opportunityId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试分页查询商机 - 应返回分页格式的商机列表
     */
    @Test
    @Order(2)
    void testPageOpportunity_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        OpportunityPageRequest request = new OpportunityPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/opportunity/page")
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
     * 测试查询商机详情 - 商机存在时应返回详情数据
     */
    @Test
    @Order(3)
    void testGetOpportunity_WhenExists_ShouldReturnDetail() throws Exception {
        // 确保已有商机
        if (opportunityId == null) {
            testAddOpportunity_WhenValidRequest_ShouldReturnOpportunity();
        }

        // 执行详情查询
        mockMvc.perform(get("/crm/v1/opportunity/get/" + opportunityId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(opportunityId));
    }

    /**
     * 测试更新商机 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateOpportunity_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有商机
        if (opportunityId == null) {
            testAddOpportunity_WhenValidRequest_ShouldReturnOpportunity();
        }

        // 构造更新请求
        OpportunityUpdateRequest updateRequest = new OpportunityUpdateRequest();
        updateRequest.setId(opportunityId);
        updateRequest.setName("更新后商机名称-" + System.currentTimeMillis());
        updateRequest.setAmount(new BigDecimal("800000.00"));
        updateRequest.setRemark("集成测试更新的商机");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/opportunity/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(opportunityId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试删除商机 - 商机存在时应成功删除
     */
    @Test
    @Order(5)
    void testDeleteOpportunity_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的商机
        OpportunityAddRequest request = new OpportunityAddRequest();
        request.setName("待删除商机-" + System.currentTimeMillis());

        String createResponse = mockMvc.perform(post("/crm/v1/opportunity/add")
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
        mockMvc.perform(get("/crm/v1/opportunity/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增商机校验 - 商机名称为空时应返回校验错误
     */
    @Test
    @Order(6)
    void testAddOpportunity_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        OpportunityAddRequest request = new OpportunityAddRequest();
        request.setAmount(new BigDecimal("100000.00"));
        // name 故意不填

        mockMvc.perform(post("/crm/v1/opportunity/add")
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
    void testPageOpportunity_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        OpportunityPageRequest request = new OpportunityPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 不携带认证 Cookie
        mockMvc.perform(post("/crm/v1/opportunity/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
