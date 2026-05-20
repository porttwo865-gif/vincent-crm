package cn.vincent.crm.clue;

import cn.vincent.crm.BaseTest;
import cn.vincent.crm.clue.dto.request.ClueAddRequest;
import cn.vincent.crm.clue.dto.request.CluePageRequest;
import cn.vincent.crm.clue.dto.request.ClueUpdateRequest;
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
 * 线索管理控制器集成测试
 * <p>
 * 测试线索增删改查相关接口，使用 Testcontainers 提供真实 MySQL + Redis 环境
 */
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClueControllerTests extends BaseTest {

    /** 保存新增的线索 ID，供后续测试使用 */
    private static String clueId;

    /**
     * 测试新增线索 - 传入合法数据时应成功返回线索实体
     */
    @Test
    @Order(1)
    void testAddClue_WhenValidRequest_ShouldReturnClue() throws Exception {
        // 构造新增线索请求
        ClueAddRequest request = new ClueAddRequest();
        request.setName("测试线索-" + System.currentTimeMillis());
        request.setContact("张三");
        request.setPhone("13800138001");

        // 执行请求并断言响应
        String responseBody = mockMvc.perform(post("/crm/v1/lead/add")
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
        clueId = objectMapper.readTree(responseBody).at("/data/id").asText();
    }

    /**
     * 测试分页查询线索 - 应返回分页格式的线索列表
     */
    @Test
    @Order(2)
    void testPageClue_ShouldReturnPagedResult() throws Exception {
        // 构造分页请求
        CluePageRequest request = new CluePageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 执行请求并断言分页格式
        mockMvc.perform(post("/crm/v1/lead/page")
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
     * 测试查询线索详情 - 线索存在时应返回详情数据
     */
    @Test
    @Order(3)
    void testGetClue_WhenExists_ShouldReturnDetail() throws Exception {
        // 先新增一条线索
        if (clueId == null) {
            testAddClue_WhenValidRequest_ShouldReturnClue();
        }

        // 执行详情查询
        mockMvc.perform(get("/crm/v1/lead/get/" + clueId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(clueId));
    }

    /**
     * 测试更新线索 - 传入合法数据时应成功更新
     */
    @Test
    @Order(4)
    void testUpdateClue_WhenValidRequest_ShouldUpdate() throws Exception {
        // 先确保有可更新的线索
        if (clueId == null) {
            testAddClue_WhenValidRequest_ShouldReturnClue();
        }

        // 构造更新请求
        ClueUpdateRequest updateRequest = new ClueUpdateRequest();
        updateRequest.setId(clueId);
        updateRequest.setName("更新后线索名称-" + System.currentTimeMillis());
        updateRequest.setContact("李四");
        updateRequest.setPhone("13900139002");

        // 执行更新并断言
        mockMvc.perform(post("/crm/v1/lead/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : ""))
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(clueId))
                .andExpect(jsonPath("$.data.name").value(updateRequest.getName()));
    }

    /**
     * 测试删除线索 - 线索存在时应成功删除（不报错）
     */
    @Test
    @Order(5)
    void testDeleteClue_WhenExists_ShouldDelete() throws Exception {
        // 先新增一条专门用于删除的线索
        ClueAddRequest request = new ClueAddRequest();
        request.setName("待删除线索-" + System.currentTimeMillis());

        String createResponse = mockMvc.perform(post("/crm/v1/lead/add")
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
        mockMvc.perform(get("/crm/v1/lead/delete/" + deleteId)
                        .cookie(new jakarta.servlet.http.Cookie("JSESSIONID",
                                authCookie != null ? authCookie.replace("JSESSIONID=", "") : "")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 测试新增线索校验 - 线索名称为空时应返回校验错误
     */
    @Test
    @Order(6)
    void testAddClue_WhenNameBlank_ShouldReturnValidationError() throws Exception {
        // 构造缺少必填字段的请求
        ClueAddRequest request = new ClueAddRequest();
        request.setContact("王五");

        // 期望返回 200 + code=422 校验错误
        mockMvc.perform(post("/crm/v1/lead/add")
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
    void testPageClue_WhenNotAuthenticated_ShouldReturn401() throws Exception {
        CluePageRequest request = new CluePageRequest();
        request.setCurrent(1);
        request.setPageSize(10);

        // 不携带认证 Cookie
        mockMvc.perform(post("/crm/v1/lead/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
