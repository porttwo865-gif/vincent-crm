package cn.vincent.crm.utils;

import cn.vincent.common.util.RsaUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试认证工具类 - 提供 RSA 公钥提取和密码加密等辅助方法
 */
public class TestAuthUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TestAuthUtils() {
        // 工具类禁止实例化
    }

    /**
     * 从 RSA 公钥响应中提取公钥
     *
     * @param rsaResponse /rsa/key 接口的响应 JSON 字符串
     * @return Base64 编码的 RSA 公钥
     * @throws Exception JSON 解析失败异常
     */
    public static String extractPublicKey(String rsaResponse) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(rsaResponse);
        // 响应经过 ResponseWrapperAdvice 包装为 {code, message, data: {publicKey, rsaKey}}
        JsonNode dataNode = jsonNode.get("data");
        if (dataNode != null && dataNode.has("publicKey")) {
            return dataNode.get("publicKey").asText();
        }
        // 兼容未包装格式
        return jsonNode.get("publicKey").asText();
    }

    /**
     * 从 RSA 公钥响应中提取密钥标识
     *
     * @param rsaResponse /rsa/key 接口的响应 JSON 字符串
     * @return RSA 密钥标识
     * @throws Exception JSON 解析失败异常
     */
    public static String extractRsaKey(String rsaResponse) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(rsaResponse);
        // 响应经过 ResponseWrapperAdvice 包装为 {code, message, data: {publicKey, rsaKey}}
        JsonNode dataNode = jsonNode.get("data");
        if (dataNode != null && dataNode.has("rsaKey")) {
            return dataNode.get("rsaKey").asText();
        }
        // 兼容未包装格式
        return jsonNode.get("rsaKey").asText();
    }

    /**
     * 使用 RSA 公钥加密密码
     *
     * @param publicKey Base64 编码的 RSA 公钥
     * @param password  明文密码
     * @return Base64 编码的加密密码
     */
    public static String encryptPassword(String publicKey, String password) {
        return RsaUtils.encryptWithPublicKey(publicKey, password);
    }
}
