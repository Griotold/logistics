package com.griotold.auth.application.service
import com.griotold.auth.domain.enums.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys
import org.springframework.test.util.ReflectionTestUtils
import spock.lang.Specification

class JwtTokenProviderTest extends Specification {

    JwtTokenProvider jwtTokenProvider

    def setup() {
        def secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(
                "46930c6e2098076c2ffdc8f1de5d7095debe589eea5b0f43819599a" +
                        "3749d88e26fff284659a26f4baecffc1e7ba574d24d4da5164bf2cf21670ce983d58eb91c"))
        def encodedKey = Encoders.BASE64URL.encode(secretKey.getEncoded())
        jwtTokenProvider = new JwtTokenProvider(encodedKey)
        ReflectionTestUtils.setField(jwtTokenProvider, "issuer", "auth-service")
        ReflectionTestUtils.setField(jwtTokenProvider, "accessExpiration", 3600000L)
    }

    def "JWT 토큰 생성 테스트"() {
        given:
        def userId = 1L
        def role = Role.HUB

        when:
        def token = jwtTokenProvider.createAccessToken(userId, role)

        then:
        token != null

        and:
        def claims = Jwts.parser()
                .verifyWith(jwtTokenProvider.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()

        claims.get("user_id", Long.class) == userId
        claims.get("role") == role.name()
        claims.getIssuer() == "auth-service"
    }
}
