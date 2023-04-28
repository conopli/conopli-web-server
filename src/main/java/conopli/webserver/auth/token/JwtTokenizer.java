package conopli.webserver.auth.token;

import conopli.webserver.auth.token.refresh.dto.RefreshDto;
import conopli.webserver.auth.token.refresh.service.RefreshService;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenizer {

    @Getter
    @Setter
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    @Getter
    private final int accessTokenExpirationMinutes = 15;

    @Getter
    private final int refreshTokenExpirationMinutes = 10000000;

    private final JwtAuthorityUtils jwtAuthorityUtils;

    private final RefreshService refreshService;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /* jwt 토큰을 생성 */
    private Token generateToken(
            Map<String, Object> claims,
            String subject,
            String base64EncodedSecretKey
    ) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(getTokenExpiration(accessTokenExpirationMinutes))
                        .signWith(key)
                        .compact(),
                Jwts.builder()
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(getTokenExpiration(refreshTokenExpirationMinutes))
                        .signWith(key)
                        .compact());

    }

    /* user 매개변수를 받아 jwt 토큰을 생성 */
    public void delegateToken(
            String email,
            HttpServletResponse response
    ) {
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        setHeader(base64SecretKey, response, email);
    }

    public Token getDelegateToken(
            String email,
            HttpServletResponse response
    ) {
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        return setHeader(base64SecretKey, response, email);
    }

    /* RefreshToken 을 검증하여 토큰을 재 발급 */
    private void reIssueToken(
            String refreshToken,
            String base64SecretKey,
            HttpServletResponse response
    ) {
        String subject = getEmail(refreshToken);
        setHeader(base64SecretKey,response, subject);
    }

    private Token setHeader(
            String base64SecretKey,
            HttpServletResponse response,
            String subject
    ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", subject);
        claims.put("roles", jwtAuthorityUtils.createRoles(subject));
        Token token = generateToken(claims, subject, base64SecretKey);
        String newAccessToken = token.getAccessToken();
        String newRefreshToken = token.getRefreshToken();
        refreshService.createRefresh(subject, newRefreshToken);
        response.setHeader("Authorization", "Bearer " + newAccessToken);
        return token;
    }


    /* Refresh Token 검증 */
    public void verifyRefreshToken(
            String email,
            HttpServletResponse response
    ) throws IOException {
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        try {
            RefreshDto refreshToken = refreshService.getRefresh(email);
            if (refreshToken == null) {
                throw new ServiceLogicException(ErrorCode.TOKEN_NOT_NULL);
            }
            verifySignature(refreshToken.getRefreshToken(), base64SecretKey);
            reIssueToken(refreshToken.getRefreshToken(), base64SecretKey, response);
        } catch (ExpiredJwtException | ServiceLogicException ee) {
            throw new ServiceLogicException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw e;
        }
    }

    /* Refresh Token 삭제 */
    public void deleteRefresh(String accessToken) {
        refreshService.deleteRefresh(getEmail(accessToken));
    }

    /* AccessToken 검증 */
    public void verifyAccessToken(
            String accessToken
    ) {
        String base64SecretKey = encodeBase64SecretKey(getSecretKey());
        try {
            verifySignature(accessToken, base64SecretKey);
        } catch (ExpiredJwtException ee) {
            throw new ServiceLogicException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw e;
        }
    }

    /* Secret key 생성 */
    public Key getKeyFromBase64EncodedSecretKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* Server에서 발급한 토큰이 맞는지 검증 */
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    /* Claims 정보를 가져옴 */
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedSecretKey(base64EncodedSecretKey);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    /* Token의 만료 기한 설정 */
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    public String getEmail(String token) {
        Key key = getKeyFromBase64EncodedSecretKey(encodeBase64SecretKey(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}