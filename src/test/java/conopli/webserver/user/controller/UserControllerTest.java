package conopli.webserver.user.controller;

import com.google.gson.Gson;
import conopli.webserver.auth.controller.AuthController;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.service.AuthService;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.auth.token.Token;
import conopli.webserver.auth.token.refresh.repository.RefreshRepository;
import conopli.webserver.auth.token.refresh.service.RefreshService;
import conopli.webserver.config.SecurityConfig;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.service.UserService;
import conopli.webserver.utils.ApiDocumentUtils;
import conopli.webserver.utils.StubUtils;
import conopli.webserver.webhook.WebHookService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ActiveProfiles("local")
@Import({JwtAuthorityUtils.class,
        JwtTokenizer.class,
        AuthService.class,
        WebHookService.class,
        SecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private AuthService authService;

    @MockBean
    private RefreshService refreshService;

    @MockBean
    private HttpClientService httpClientService;

    @MockBean
    private UserService userService;

    @MockBean
    private RefreshRepository refreshRepository;

    Gson gson = new Gson();

    @Test
    @DisplayName("회원 조회 TEST")
    @WithMockUser
    void searchUser() throws Exception {
        // Given
        Token token = createToken();
        Long userId = 1L;
        given(userService.searchUser(anyLong())).willReturn(StubUtils.createUserDto());
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/users/{userId}", userId)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("searchUser",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("userId").description("회원 식별자")
                                ),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                                fieldWithPath("data.userStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                                fieldWithPath("data.loginType").type(JsonFieldType.STRING).description("로그인 타입")
                                        ))));
    }

    @Test
    @DisplayName("탈퇴 회원 재활성화 TEST")
    @WithMockUser
    void reActivationUser() throws Exception {
        // Given
        LoginDto loginDto = StubUtils.createLoginDto();
        String content = gson.toJson(loginDto);
        User user = User.builder()
                .userId(1L)
                .userStatus(UserStatus.VERIFIED)
                .email("test@test.com")
                .loginType(LoginType.valueOf(loginDto.getLoginType()))
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
        given(httpClientService.generateLoginRequest(any(LoginDto.class))).willReturn(user.getEmail());
        given(userService.verifiedUserByEmail(anyString())).willReturn(user);
        given(userService.delegateSaveUser(any(User.class))).willReturn(user);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .patch("/api/users")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("reActivationUser",
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
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"),
                                                fieldWithPath("data.userStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                                fieldWithPath("data.loginType").type(JsonFieldType.STRING).description("로그인 타입")
                                        ))));
    }

    @Test
    @DisplayName("회원 삭제 TEST")
    @WithMockUser
    void deleteUser() throws Exception {
        // Given
        Token token = createToken();
        Long userId = 1L;
        doNothing().when(userService).deleteUser(anyLong());
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .delete("/api/users/{userId}", userId)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isNoContent())
                .andDo(
                        MockMvcRestDocumentation.document("deleteUser",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("userId").description("회원 식별자")
                                ),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                )
                        ));
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