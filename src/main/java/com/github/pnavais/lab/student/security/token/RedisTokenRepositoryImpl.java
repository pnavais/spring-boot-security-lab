package com.github.pnavais.lab.student.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis TokenRepository Implementation for Remember me
 * <p>
 * {@link org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl}
 * {@link org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices}
 * {@link org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken}
 */
@Slf4j
@Component
class RedisTokenRepositoryImpl implements PersistentTokenRepository {

    public static final String USERNAME = "username";
    public static final String TOKEN_VALUE = "tokenValue";
    public static final String DATE = "date";

    public static final Long DEFAULT_DAYS = 5L;

    @Value("${spring.session.redis.rememberme.validity:#{T(com.github.pnavais.lab.student.security.token.RedisTokenRepositoryImpl).DEFAULT_DAYS}}")
    private Long validityDays;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    RedisTokenRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        createOrUpdateToken(token);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        createOrUpdateToken(new PersistentRememberMeToken(null, series, tokenValue, lastUsed));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        String key = generateKey(seriesId);

        Map<Object, Object> keyValues = redisTemplate.opsForHash().entries(key);

        Object username = keyValues.get(USERNAME);
        Object tokenValue = keyValues.get(TOKEN_VALUE);
        Object date = keyValues.get(DATE);

        if (username == null || tokenValue == null || date == null) {
            return null;
        }

        return new PersistentRememberMeToken(username.toString(), seriesId,
                tokenValue.toString(), new Date(Long.parseLong(date.toString())));
    }

    @Override
    public void removeUserTokens(String username) {
        String userNameKey = generateUserNameKey(username);

        // Retrieve user's related token series
        String tokenKey = redisTemplate.opsForValue().get(userNameKey);

        if (Objects.nonNull(tokenKey)) {
            // Remove key by direct expiration
            redisTemplate.expire(tokenKey, 0, TimeUnit.SECONDS);

            // Removed user key reference
            redisTemplate.expire(userNameKey, 0, TimeUnit.SECONDS);

            log.debug("Token removed for user [{}]", username);
        } else {
            log.warn("Token not found for user [{}]", username);
        }
    }

    /**
     * Generate a key for series
     *
     * @param series series
     * @return the generated key for the series
     */
    private String generateKey(String series) {
        return "spring:security:rememberMe:token:" + series;
    }

    /**
     * Generates the key for the username
     *
     * @param userName username
     * @return the generated username key
     */
    private String generateUserNameKey(String userName) {
        return "spring:security:rememberMe:userName:" + userName;
    }

    /**
     * Creates or updates the given persistent token
     *
     * @param token the remember me token
     */
    private void createOrUpdateToken(PersistentRememberMeToken token) {
        boolean isCreate = Objects.nonNull(token.getUsername());

        log.debug("{} token seriesId: [{}]", isCreate ? "Creating" : "updating", token.getSeries());

        String tokenKey = generateKey(token.getSeries());
        HashMap<String, String> map = new HashMap<>();

        if (isCreate) {
            // Generate reference to the series token for the given username
            String userNameKey = generateUserNameKey(token.getUsername());
            redisTemplate.opsForValue().set(userNameKey, tokenKey);
            redisTemplate.expire(userNameKey, validityDays, TimeUnit.DAYS);

            map.put(USERNAME, token.getUsername());
        }

        map.put(TOKEN_VALUE, token.getTokenValue());
        map.put(DATE, String.valueOf(token.getDate().getTime()));

        // Keep token for the given series
        redisTemplate.opsForHash().putAll(tokenKey, map);
        redisTemplate.expire(tokenKey, validityDays, TimeUnit.DAYS);
    }
}