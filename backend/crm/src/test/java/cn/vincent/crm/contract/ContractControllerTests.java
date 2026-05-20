package cn.vincent.crm.contract;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.contract.dto.request.ContractAddRequest;
import cn.vincent.crm.contract.dto.request.ContractPageRequest;
import cn.vincent.crm.contract.dto.request.ContractStatusRequest;
import cn.vincent.crm.contract.dto.request.ContractUpdateRequest;
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
 * 合同管理控制器集成测试
 * <p>
 * 测试合同增删改查、状态变更等接口。
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContractControllerTests extends BaseTest {

    /** 保存新增的合同 ID，供后续测试使用 */
    private static String contractId;

    /**
     * 测试新增合同 - 传入合法数据时应成功返回合同实体
     */
    @Test
    @Order(1)
    void testAddContract_WhenValidRequest_ShouldReturnContract() throws Exception {
        // 构造新增合同请求
        ContractAddRequest request = new ContractAddRequest();
        request.setName("测试合同-" + System.currentTimeMillis());
        request.setAmount(new BigDecimal("100000.00"));
        request.setStatus("draft");
        request.setStartDate(System.currentTimeMillis());
        request.setEndDate(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        request.setSignedDate(System.currentTimeMillis());
        request.setRemark("集成测试创建的合同");

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/contract/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.status").value("draft"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 保存 ID 供后续测试使用
        contractId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试分页查询合同 - 应返回分页格式的合同列表
     */
    @Test
    @Order(2)
    void testPageContract_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        ContractPageRequest request = new ContractPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/contract/page")
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
     * 测试查询合同详情 - 合同存在时应返回详情数据
     */
    @Test
    @Order(3)
    void testGetContract_WhenExists_ShouldReturnDetail() throws Exception {
        // 确保已有合同
        if (contractId == null) {
            testAddContract_WhenValidRequest_ShouldReturnContract();
        }

        // 执行详情查询
        mockMvc.perform(get("/crm/v1/contract/get/" + contractId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(contractId));
    }

    /**
     * 测试更新合同 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateContract_WhenValidRequest_ShouldUpdate() throws Exception {
        // 确保已有合同
        if (contractId == null) {
            testAddContract_WhenValidRequest_ShouldReturnContract();
        }

        // 构造更新请求
        ContractUpdateRequest updateRequest = new ContractUpdateRequest();
        updateRequest.setId(contractId);
        updateRequest.setName("更新后合同名称-" + System.currentTimeMillis());
        updateRequest.setAmount(new BigDecimal("200000.00"));
        updateRequest.setRemark("集成测试更新的合同");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/contract/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(contractId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试变更合同状态 - 状态变更应成功返回更新后的合同
     */
    @Test
    @Order(5)
    void testChangeContractStatus_WhenValidRequest_ShouldUpdateStatus() throws Exception {
        // 确保已有合同
        if (contractId == null) {
            testAddContract_WhenValidRequest_ShouldReturnContract();
        }

        // 构造状态变更请求（draft -> active）
        ContractStatusRequest statusRequest = new ContractStatusRequest();
        statusRequest.setId(contractId);
        statusRequest.setStatus("active");

        // 执行状态变更并断言
        mockMvc.perform(post("/crm/v1/contract/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(contractId))
                .andExpect(jsonPath("$.data.status").value("active"));
    }

    /**
     * 测试删除合同 - 合同存在时应成功删除
     */
    @Test
    @Order(6)
    void testDeleteContract_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的合同
        ContractAddRequest request = new ContractAddRequest();
        request.setName("待删除合同-" + System.currentTimeMillis());
        request.setAmount(new BigDecimal("50000.00"));
        request.setStatus("draft");

        String createResponse = mockMvc.perform(post("/crm/v1/contract/add")
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
        mockMvc.perform(get("/crm/v1/contract/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增合同校验 - 合同名称为空时应返回校验错误
     */
    @Test
    @Order(7)
    void testAddContract_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        ContractAddRequest request = new ContractAddRequest();
        request.setAmount(new BigDecimal("100000.00"));
        // name 故意不填

        mockMvc.perform(post("/crm/v1/contract/add")
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
    @Order(8)
    void testPageContract_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        ContractPageRequest request = new ContractPageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 不携带认证 Cookie
        mockMvc.perform(post("/crm/v1/contract/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
