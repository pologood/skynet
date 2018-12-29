package org.shieldproject.usercenter.access;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author kezhijie
 * @date 2018/12/29 9:55
 */
public class Token {

    // token store repository
    private static final List<String> TOKENS = new ArrayList<>();

    // secret key
    private static final byte[] SECRET = "https://github.com/shield-project".getBytes();

    private static final Random RANDOM = new Random();

    /**
     * 根据userId生成token
     * @param userId
     * @return
     */
    public static String generate(String userId) {
        if (StringUtils.isEmpty(userId))
            return null;

        String var = String.valueOf(RANDOM.nextInt(10000));
        for (int i=0; i < 5 - var.length(); i++) {
            var = "0" + var;
        }

        String token = JWT.create().withAudience(userId, var).sign(Algorithm.HMAC256(SECRET));
        TOKENS.add(token);

        return token;
    }

    /**
     * 验证token是否已经生成
     * @param token
     * @return
     */
    public static boolean validate(String token) {
        return TOKENS.contains(token);
    }

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     * @throws IllegalAccessException
     */
    public static String getUserIdByToken(String token) throws IllegalAccessException {
        if (!validate(token))
            throw new IllegalAccessException("token invalid");

        return JWT.decode(token).getAudience().get(0);
    }

}
