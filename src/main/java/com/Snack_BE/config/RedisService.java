package com.Snack_BE.config;

import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LUA_RESERVE = """
            local stock = tonumber(redis.call('GET', KEYS[1]) or '0')
            local userCount = tonumber(redis.call('GET', KEYS[2]) or '0')
            local qty = tonumber(ARGV[1])
            local max = tonumber(ARGV[2])

            if stock >= qty and (userCount + qty) <= max then
                redis.call('DECRBY', KEYS[1], qty)
                redis.call('INCRBY', KEYS[2], qty)
                redis.call('EXPIRE', KEYS[2], 600)
                return 1
            end
            return 0
            """;

    public boolean tryReserve(Long productId, Long userId, Integer qty) {
        String stockKey = "product:" + productId + ":stock";
        String userKey = "user:" + userId + ":product:" + productId + ":count";

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_RESERVE);
        script.setResultType(Long.class);

        Long result = redisTemplate.execute(
                script,
                List.of(stockKey, userKey),
                qty,
                3);

        return result != null && result == 1;
    }

    public void updateStock(Long productId, int stock) {
        String key = "product:stock:" + productId;
        redisTemplate.opsForValue().set(key, String.valueOf(stock));
    }
}
