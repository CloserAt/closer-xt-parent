package com.closer.xt.sso.domain;

import com.closer.xt.common.Utils.JWTUtils;
import com.closer.xt.common.constant.RedisKey;
import com.closer.xt.sso.domain.repository.TokenDomainRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TokenDomain {
    private TokenDomainRepository tokenDomainRepository;

    public TokenDomain(TokenDomainRepository tokenDomainRepository) {
        this.tokenDomainRepository = tokenDomainRepository;
    }

    public Long checkToken(String token) {
        /*
        1.检测token字符串是否合法
        2.检测redis是否合法
         */
        try {
            //JWT验证
            JWTUtils.parseJWT(token, LoginDomain.secretKey);
            log.info(token);
            //redis验证
            String userIdStr = tokenDomainRepository.stringRedisTemplate.opsForValue().get(RedisKey.TOKEN + token);

            if (StringUtils.isBlank(userIdStr)) {
                return null;
            }
            return Long.parseLong(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("checkToken errot:{}", e.getMessage(), e);
            return null;
        }
    }
}
