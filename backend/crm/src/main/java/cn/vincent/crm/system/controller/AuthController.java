package cn.vincent.crm.system.controller;

import cn.vincent.crm.system.dto.request.LoginRequest;
import cn.vincent.crm.system.service.UserLoginService;
import cn.vincent.common.util.RsaUtils;
import cn.vincent.security.SessionUtils;
import cn.vincent.security.dto.SessionUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器 - 提供登录、登出、公钥获取等接口
 */
@RestController
@Tag(name = "认证管理")
public class AuthController {

    /** 用户登录服务 */
    @Resource
    private UserLoginService userLoginService;

    /**
     * 获取 RSA 公钥
     * <p>
     * 前端使用公钥加密密码，服务端使用私钥解密
     *
     * @return 包含公钥和密钥标识的 Map
     */
    @GetMapping("/rsa/key")
    @Operation(summary = "获取 RSA 公钥")
    public Map<String, String> getRsaKey() {
        // 使用 Session ID 作为 RSA 密钥标识
        Subject subject = SecurityUtils.getSubject();
        String sessionId = subject.getSession().getId().toString();

        String publicKey = RsaUtils.generatePublicKey(sessionId);

        Map<String, String> result = new HashMap<>();
        result.put("publicKey", publicKey);
        result.put("rsaKey", sessionId);
        return result;
    }

    /**
     * 用户登录
     *
     * @param request 登录请求（用户名 + RSA 加密密码 + 密钥标识）
     * @return SessionUser 会话用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public SessionUser login(@Validated @RequestBody LoginRequest request) {
        // 通过 UserLoginService 校验用户名密码
        SessionUser sessionUser = userLoginService.login(
                request.getUsername(),
                request.getPassword(),
                request.getRsaKey(),
                "org_default"  // 默认组织，后续支持多组织时从请求参数获取
        );

        // 执行 Shiro 登录（建立 Session）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(
                request.getUsername(), request.getPassword());
        subject.login(token);

        // 将 SessionUser 重新设置到 Shiro Principal（覆盖 token 中的简单字符串）
        // 注意：由于 LocalRealm 的 doGetAuthenticationInfo 已将 SessionUser 作为 Principal
        // subject.login 成功后，Principal 已自动设置为 SessionUser

        return sessionUser;
    }

    /**
     * 登录状态检测
     *
     * @return true 已登录，false 未登录
     */
    @GetMapping("/is-login")
    @Operation(summary = "登录状态检测")
    public boolean isLogin() {
        SessionUser user = SessionUtils.getSessionUser();
        return user != null;
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
    }
}
