package cn.vincent.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 集成测试专用启动类
 * <p>
 * 用于替代 app 模块的 Application 类，避免 crm 模块循环依赖 app 模块。
 * 扫描范围与正式启动类一致，包含 Shiro 配置需要的所有 Bean。
 */
@SpringBootApplication(scanBasePackages = "cn.vincent")
@MapperScan("cn.vincent.crm.**.mapper")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
