package cn.vincent.crm;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 集成测试基类，提供 MySQL + Redis Testcontainer + Spring Boot Test 环境
 * <p>
 * 测试前会自动执行 Flyway 迁移，初始化数据库表和默认数据。
 * 登录后保存 JSESSIONID，后续请求自动携带认证 Cookie。
 */
@SpringBootTest(classes = cn.vincent.crm.TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
public abstract class BaseTest {

    /**
     * MySQL 测试容器
     */
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("vincent_crm_test")
            .withUsername("test")
            .withPassword("test123")
            .withCommand("--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci");

    /**
     * Redis 测试容器
     */
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    /**
     * MockMvc 用于执行 HTTP 请求
     */
    @Resource
    protected MockMvc mockMvc;

    /**
     * JSON 序列化器
     */
    @Resource
    protected ObjectMapper objectMapper;

    /**
     * Shiro 安全管理器（用于程序化登录）
     */
    @Resource
    private DefaultWebSecurityManager securityManager;

    /**
     * 认证 Cookie（JSESSIONID），登录后设置
     */
    protected String authCookie;

    /**
     * 动态配置数据源和 Redis 连接信息
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // MySQL 数据源配置
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);

        // Redis 配置
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort().toString());
    }

    /**
     * 每个测试类执行前登录
     *
     * @throws Exception 登录失败异常
     */
    @BeforeEach
    void beforeEachBase() throws Exception {
        // 执行管理员登录
        login();
    }

    /**
     * 执行管理员登录并返回认证 Cookie
     * <p>
     * 绕过 HTTP RSA 登录流程，直接通过 Shiro SecurityManager 程序化认证。
     * LocalRealm.doGetAuthenticationInfo 只校验用户是否存在，不校验密码强度，
     * 因此测试环境可使用任意密码字符串完成认证。
     *
     * @return JSESSIONID Cookie 字符串
     * @throws Exception 登录失败异常
     */
    protected String login() throws Exception {
        // 1. 绑定 SecurityManager 到当前线程（程序化 Shiro 使用所需）
        SecurityUtils.setSecurityManager(securityManager);

        // 2. 构建离线 Subject 并执行程序化登录
        //    LocalRealm 不校验密码（将 token 密码作为凭证回写），任意密码均可通过
        Subject subject = new Subject.Builder(securityManager).buildSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("admin", "test_password");
        subject.login(token);

        // 3. 提取 Shiro Session ID 作为 JSESSIONID Cookie
        //    MemorySessionDAO 会持有该 Session，后续 MockMvc 请求携带此 Cookie
        //    即可通过 DefaultWebSessionManager 的会话查找完成认证
        String sessionId = subject.getSession().getId().toString();
        authCookie = "JSESSIONID=" + sessionId;

        return authCookie;
    }
}
