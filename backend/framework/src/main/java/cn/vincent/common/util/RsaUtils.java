package cn.vincent.common.util;

import jakarta.annotation.Resource;
import org.apache.commons.codec.binary.Base64;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 加密工具 - 生成密钥对、公钥加密、私钥解密
 * <p>
 * 密钥对缓存到 Redis，TTL 5 分钟
 */
@Component
public class RsaUtils {

    /** Redis 缓存键前缀 */
    private static final String RSA_KEY_PREFIX = "rsa_key:";

    /** RSA 密钥长度 */
    private static final int KEY_SIZE = 2048;

    /** 密钥对缓存时间（5 分钟） */
    private static final long KEY_TTL_SECONDS = 300;

    /** Redisson 客户端（静态持有） */
    private static RedissonClient redissonClient;

    /**
     * 构造注入 RedissonClient
     *
     * @param redissonClient Redisson 客户端
     */
    @Resource
    public void setRedissonClient(RedissonClient redissonClient) {
        RsaUtils.redissonClient = redissonClient;
    }

    /**
     * 生成 RSA 密钥对，将私钥存入 Redis，返回公钥的 Base64 编码
     *
     * @param key 唯一标识（如 sessionId）
     * @return Base64 编码的公钥
     */
    public static String generatePublicKey(String key) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 公钥 Base64
            String publicKeyStr = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
            // 私钥 Base64
            String privateKeyStr = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());

            // 私钥缓存到 Redis
            RBucket<String> bucket = redissonClient.getBucket(RSA_KEY_PREFIX + key);
            bucket.set(privateKeyStr, KEY_TTL_SECONDS, java.util.concurrent.TimeUnit.SECONDS);

            return publicKeyStr;
        } catch (Exception e) {
            throw new RuntimeException("生成 RSA 密钥对失败", e);
        }
    }

    /**
     * 使用私钥解密数据
     *
     * @param key         唯一标识（与生成时一致）
     * @param cipherText  Base64 编码的密文
     * @return 解密后的明文
     */
    public static String decrypt(String key, String cipherText) {
        try {
            // 从 Redis 获取私钥
            RBucket<String> bucket = redissonClient.getBucket(RSA_KEY_PREFIX + key);
            String privateKeyStr = bucket.get();
            if (privateKeyStr == null) {
                throw new RuntimeException("RSA 密钥已过期，请重新获取");
            }

            // 还原私钥对象
            byte[] privateKeyBytes = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            // 解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(cipherText));

            // 用完后删除密钥
            bucket.delete();

            return new String(decryptedBytes);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("RSA 解密失败", e);
        }
    }

    /**
     * 使用公钥加密数据（主要用于测试）
     *
     * @param publicKeyStr Base64 编码的公钥
     * @param plainText    明文
     * @return Base64 编码的密文
     */
    public static String encryptWithPublicKey(String publicKeyStr, String plainText) {
        try {
            byte[] publicKeyBytes = Base64.decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            return Base64.encodeBase64String(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("RSA 加密失败", e);
        }
    }
}
