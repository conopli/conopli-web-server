package conopli.webserver.auth.controller;

import com.google.gson.Gson;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.service.AuthService;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.auth.token.Token;
import conopli.webserver.auth.token.refresh.dto.RefreshDto;
import conopli.webserver.auth.token.refresh.repository.RefreshRepository;
import conopli.webserver.auth.token.refresh.service.RefreshService;
import conopli.webserver.config.SecurityConfig;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.service.UserService;
import conopli.webserver.utils.ApiDocumentUtils;
import conopli.webserver.utils.StubUtils;
import conopli.webserver.webhook.WebHookService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ActiveProfiles("local")
@Import({JwtAuthorityUtils.class,
        JwtTokenizer.class,
        AuthService.class,
        WebHookService.class,
        WebHookService.class,
        SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshService refreshService;

    @MockBean
    private RefreshRepository refreshRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private HttpClientService httpClientService;

    @MockBean
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("로그인 TEST - OK")
    @WithMockUser
    void loginOk() throws Exception {
        // Given
        Token token = createToken();
        LoginDto loginDto = StubUtils.createLoginDto();
        Gson gson = new Gson();
        String content = gson.toJson(loginDto);
        User user = User.builder()
                .userId(1L)
                .userStatus(UserStatus.VERIFIED)
                .email("test@test.com")
                .loginType(LoginType.valueOf(loginDto.getLoginType()))
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
        given(httpClientService.generateLoginRequest(any(LoginDto.class))).willReturn(user.getEmail());
        given(userService.createOrVerifiedUserByEmailAndLoginType(anyString(), anyString()))
                .willReturn(UserDto.of(user));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .post("/api/auth/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("loginOk",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("oauthAccessToken").type(JsonFieldType.STRING).description("OAuth2 Access Token"),
                                                fieldWithPath("loginType").type(JsonFieldType.STRING).description("OAuth2 Type")
                                        )

                                ),
                                HeaderDocumentation.responseHeaders(
                                        headerWithName("Authorization").description("AccessToken"),
                                        headerWithName("userId").description("회원 식별자"),
                                        headerWithName("userStatus").description("UserStatus")
                                )
                        ));
    }

    @Test
    @DisplayName("로그인 TEST - User Exist")
    @WithMockUser
    void loginExist() throws Exception {
        // Given
        Token token = createToken();
        LoginDto loginDto = StubUtils.createLoginDto();
        Gson gson = new Gson();
        String content = gson.toJson(loginDto);
        User user = User.builder()
                .userId(1L)
                .userStatus(UserStatus.VERIFIED)
                .email("test@test.com")
                .loginType(LoginType.GOOGLE)
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
        given(httpClientService.generateLoginRequest(any(LoginDto.class))).willReturn(user.getEmail());
        given(userService.createOrVerifiedUserByEmailAndLoginType(anyString(), anyString()))
                .willThrow(new ServiceLogicException(ErrorCode.EXIST_USER));
        given(userService.verifiedUserByEmail(anyString()))
                .willReturn(user);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .post("/api/auth/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().is4xxClientError())
                .andDo(
                        MockMvcRestDocumentation.document("loginExist",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("oauthAccessToken").type(JsonFieldType.STRING).description("OAuth2 Access Token"),
                                                fieldWithPath("loginType").type(JsonFieldType.STRING).description("OAuth2 Type")
                                        )

                                ),
                                HeaderDocumentation.responseHeaders(
                                        headerWithName("userLoginType").description("회원 기존 로그인 타입")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("ErrorCode"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("ErrorMessage"),
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("Web Server ErrorCode"),
                                                fieldWithPath("fieldErrors").type(JsonFieldType.STRING).description("fieldErrors").optional(),
                                                fieldWithPath("violationErrors").type(JsonFieldType.STRING).description("violationErrors").optional()
                                        ))));
    }

    @Test
    @DisplayName("로그인 TEST - User Inactive")
    @WithMockUser
    void loginInactive() throws Exception {
        // Given
        Token token = createToken();
        LoginDto loginDto = StubUtils.createLoginDto();
        Gson gson = new Gson();
        String content = gson.toJson(loginDto);
        User user = User.builder()
                .userId(1L)
                .userStatus(UserStatus.VERIFIED)
                .email("test@test.com")
                .loginType(LoginType.valueOf(loginDto.getLoginType()))
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
        given(httpClientService.generateLoginRequest(any(LoginDto.class))).willReturn(user.getEmail());
        given(userService.createOrVerifiedUserByEmailAndLoginType(anyString(), anyString()))
                .willThrow(new ServiceLogicException(ErrorCode.INACTIVE_USER));
        given(userService.verifiedUserByEmail(anyString()))
                .willReturn(user);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .post("/api/auth/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().is4xxClientError())
                .andDo(
                        MockMvcRestDocumentation.document("loginInactive",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("oauthAccessToken").type(JsonFieldType.STRING).description("OAuth2 Access Token"),
                                                fieldWithPath("loginType").type(JsonFieldType.STRING).description("OAuth2 Type")
                                        )

                                ),
                                HeaderDocumentation.responseHeaders(
                                        headerWithName("userLoginType").description("회원 기존 로그인 타입")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("ErrorCode"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("ErrorMessage"),
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("Web Server ErrorCode"),
                                                fieldWithPath("fieldErrors").type(JsonFieldType.STRING).description("fieldErrors").optional(),
                                                fieldWithPath("violationErrors").type(JsonFieldType.STRING).description("violationErrors").optional()
                                        ))));
    }

    @Test
    @DisplayName("로그인 상태 확인 TEST - OK")
    @WithMockUser
    void verifyUserOK() throws Exception {
        // Given
        Token token = createToken();
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/auth/verify-user")
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("verifyUserOk",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                )));
    }

    @Test
    @DisplayName("로그인 상태 확인 TEST - AccessToken 만료")
    @WithMockUser
    void verifyUserThrowException() throws Exception {
        // Given
        Token token = createExpiredToken();
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/auth/verify-user")
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage()))
                .andDo(
                        MockMvcRestDocumentation.document("verifyUserException",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("ErrorCode"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("ErrorMessage"),
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("Web Server ErrorCode"),
                                                fieldWithPath("fieldErrors").type(JsonFieldType.STRING).description("fieldErrors").optional(),
                                                fieldWithPath("violationErrors").type(JsonFieldType.STRING).description("violationErrors").optional()
                                        ))));
    }


    @Test
    @DisplayName("토큰 재발급 TEST - OK")
    @WithMockUser
    void reissueToken() throws Exception {
        // Given
        Token token = createToken();
        Long userId = 1L;
        User user = StubUtils.createUser();
        RefreshDto refreshDto = new RefreshDto(
                user.getEmail(),
                token.getRefreshToken(),
                LocalDateTime.now());
        given(userService.verifiedUserById(userId)).willReturn(user);
        given(refreshService.getRefresh(anyString())).willReturn(refreshDto);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/auth/reissue-token/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        ResultActions perform = mockMvc.perform(result);
        // Then
        perform.andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("refreshOk",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("userId").description("회원 식별자")
                                ),
                                HeaderDocumentation.responseHeaders(
                                        headerWithName("Authorization").description("AccessToken"),
                                        headerWithName("userId").description("회원 식별자"),
                                        headerWithName("userStatus").description("UserStatus")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("authorization").type(JsonFieldType.STRING).description("AccessToken"),
                                                fieldWithPath("userId").type(JsonFieldType.STRING).description("회원 식별자"),
                                                fieldWithPath("userStatus").type(JsonFieldType.STRING).description("회원 타입")
                                        ))));

    }

    @Test
    @DisplayName("토큰 재발급 TEST - RefreshToken 만료")
    @WithMockUser
    void reissueTokenThrowException() throws Exception {
        // Given
        Token token = createExpiredToken();
        Long userId = 1L;
        User user = StubUtils.createUser();
        given(userService.verifiedUserById(userId)).willReturn(user);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/auth/reissue-token/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.EXPIRED_REFRESH_TOKEN.getMessage()))
                .andDo(
                        MockMvcRestDocumentation.document("refreshExpired",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("userId").description("회원 식별자")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("ErrorCode"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("ErrorMessage"),
                                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("Web Server ErrorCode"),
                                                fieldWithPath("fieldErrors").type(JsonFieldType.STRING).description("fieldErrors").optional(),
                                                fieldWithPath("violationErrors").type(JsonFieldType.STRING).description("violationErrors").optional()
                                        ))));
    }

    @Test
    @DisplayName("Logout 동작 TEST")
    @WithMockUser
    void logout() throws Exception {
        // Given
        HttpServletResponse response = mock(HttpServletResponse.class);
        User user = StubUtils.createUser();
        Token token = jwtTokenizer.getDelegateToken(user.getEmail(), response);
        doNothing().when(refreshService).deleteRefresh(anyString());
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/auth/logout")
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("logoutOk",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                )));
    }



    private Token createExpiredToken() {
        Map<String, Object> claims = new HashMap<>();
        User testUser = StubUtils.createUser();
        claims.put("username", testUser.getEmail());
        claims.put("roles", testUser.getRoles());

        String subject = testUser.getEmail();

        String base64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Key key = jwtTokenizer.getKeyFromBase64EncodedSecretKey(base64SecretKey);
        return new Token(
                "Bearer " + Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(jwtTokenizer.getTokenExpiration(0))
                        .signWith(key)
                        .compact(),
                Jwts.builder()
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(jwtTokenizer.getTokenExpiration(0))
                        .signWith(key)
                        .compact());
    }

    private Token createToken() {
        Map<String, Object> claims = new HashMap<>();
        User testUser = StubUtils.createUser();
        claims.put("username", testUser.getEmail());
        claims.put("roles", testUser.getRoles());

        String subject = testUser.getEmail();

        String base64SecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Key key = jwtTokenizer.getKeyFromBase64EncodedSecretKey(base64SecretKey);
        return new Token(
                "Bearer " + Jwts.builder()
                        .setClaims(claims)
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(jwtTokenizer.getTokenExpiration(10))
                        .signWith(key)
                        .compact(),
                Jwts.builder()
                        .setSubject(subject)
                        .setIssuedAt(Calendar.getInstance().getTime())
                        .setExpiration(jwtTokenizer.getTokenExpiration(10))
                        .signWith(key)
                        .compact());
    }



}