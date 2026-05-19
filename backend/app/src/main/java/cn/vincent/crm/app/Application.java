package cn.vincent.crm.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * VincentCRM 应用启动类
 */
@SpringBootApplication(scanBasePackages = "cn.vincent")
@MapperScan("cn.vincent.crm.**.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
