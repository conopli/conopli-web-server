package conopli.webserver.music.controller;

import com.google.gson.Gson;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.auth.token.Token;
import conopli.webserver.auth.token.refresh.repository.RefreshRepository;
import conopli.webserver.auth.token.refresh.service.RefreshService;
import conopli.webserver.config.SecurityConfig;
import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.music.dto.UserMusicDto;
import conopli.webserver.music.dto.UserMusicRequestDto;
import conopli.webserver.music.service.UserMusicService;
import conopli.webserver.playlist.dto.PlayListModifyRequestDto;
import conopli.webserver.playlist.dto.PlayListRequestDto;
import conopli.webserver.user.entity.User;
import conopli.webserver.utils.ApiDocumentUtils;
import conopli.webserver.utils.StubUtils;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserMusicController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ActiveProfiles("local")
@Import({JwtAuthorityUtils.class,
        JwtTokenizer.class,
        SecurityConfig.class})
class UserMusicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private RefreshService refreshService;

    @MockBean
    private RefreshRepository refreshRepository;

    @MockBean
    private UserMusicService userMusicService;

    @Test
    @DisplayName("회원 플레이 리스트 조회 TEST")
    @WithMockUser
    void userPlaylist() throws Exception {
        // Given
        Token token = createToken();
        Long userId = 1L;
        given(userMusicService.findUserPlayList(anyLong()))
                .willReturn(
                        ResponseDto.of(
                                List.of(
                                        StubUtils.createPlayListDto(),
                                        StubUtils.createPlayListDto()
                                )));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/user-music/playlist/{userId}", userId)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("userPlaylist",
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
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                                                fieldWithPath("data[].playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("플레이 리스트 제목"),
                                                fieldWithPath("data[].color").type(JsonFieldType.STRING).description("컬러 코드"),
                                                fieldWithPath("data[].emoji").type(JsonFieldType.STRING).description("이모지 코드")
                                        ))));
    }

    @Test
    @DisplayName("회원 플레이 리스트의 음악 조회 TEST")
    @WithMockUser
    void userMusic() throws Exception {
        // Given
        Token token = createToken();
        Long playListId = 1L;
        UserMusicDto dto1 = StubUtils.createUserMusicDto(1);
        UserMusicDto dto2 = StubUtils.createUserMusicDto(2);
        List<UserMusicDto> dtoList = List.of(dto1, dto2);
        Page page = new PageImpl(dtoList);
        PageResponseDto response = PageResponseDto.of(dtoList, page);
        given(userMusicService.findUserMusic(anyLong(), any(Pageable.class)))
                .willReturn(response);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/user-music/{playListId}", playListId)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("userMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("playListId").description("플레이 리스트 식별자")
                                ),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data[].userMusicId").type(JsonFieldType.STRING).description("저장 음악 식별자"),
                                                fieldWithPath("data[].playListId").type(JsonFieldType.STRING).description("플레이 리스트 식별자"),
                                                fieldWithPath("data[].musicId").type(JsonFieldType.STRING).description("음악 식별자"),
                                                fieldWithPath("data[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data[].singer").type(JsonFieldType.STRING).description("가수"),
                                                fieldWithPath("data[].lyricist").type(JsonFieldType.STRING).description("작사"),
                                                fieldWithPath("data[].composer").type(JsonFieldType.STRING).description("작곡"),
                                                fieldWithPath("data[].youtubeUrl").type(JsonFieldType.STRING).description("YouTube URL"),
                                                fieldWithPath("data[].nation").type(JsonFieldType.STRING).description("국가"),
                                                fieldWithPath("data[].orderNum").type(JsonFieldType.NUMBER).description("저장 순서"),
                                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("요청 페이지 정보"),
                                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("요청 페이지 - 0 = 1 페이지"),
                                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지당 요청 회원"),
                                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 멤버"),
                                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("생성된 총 페이지")

                                        ))));
    }

    @Test
    @DisplayName("회원 플레이 리스트 생성 TEST")
    @WithMockUser
    void savePlaylist() throws Exception {
        // Given
        Token token = createToken();
        PlayListRequestDto request = StubUtils.createPlayListRequestDto();
        Gson gson = new Gson();
        String content = gson.toJson(request);
        given(userMusicService.createUserPlayList(any(PlayListRequestDto.class)))
                .willReturn(ResponseDto.of(StubUtils.createPlayListDto()));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .post("/api/user-music/playlist")
                .content(content)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("savePlaylist",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("플레이 리스트 제목"),
                                                fieldWithPath("color").type(JsonFieldType.STRING).description("컬러 코드"),
                                                fieldWithPath("emoji").type(JsonFieldType.STRING).description("이모지 코드")
                                        )

                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                                fieldWithPath("data.playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("플레이 리스트 제목"),
                                                fieldWithPath("data.color").type(JsonFieldType.STRING).description("컬러 코드"),
                                                fieldWithPath("data.emoji").type(JsonFieldType.STRING).description("이모지 코드")
                                        ))));
    }

    @Test
    @DisplayName("회원 플레이 리스트에 음악 저장 TEST")
    @WithMockUser
    void saveUserMusic() throws Exception {
        // Given
        Token token = createToken();
        UserMusicRequestDto request = StubUtils.createUserMusicRequestDto();
        Gson gson = new Gson();
        String content = gson.toJson(request);
        given(userMusicService.createUserMusic(any(UserMusicRequestDto.class)))
                .willReturn(ResponseDto.of(List.of(StubUtils.createUserMusicDto(1),StubUtils.createUserMusicDto(2))));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .post("/api/user-music")
                .content(content)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("saveUserMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                fieldWithPath("playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("musicNum").type(JsonFieldType.ARRAY).description("곡 번호")
                                        )

                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data.[].userMusicId").type(JsonFieldType.STRING).description("저장 음악 식별자"),
                                                fieldWithPath("data.[].playListId").type(JsonFieldType.STRING).description("플레이 리스트 식별자"),
                                                fieldWithPath("data.[].musicId").type(JsonFieldType.STRING).description("음악 식별자"),
                                                fieldWithPath("data.[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data.[].singer").type(JsonFieldType.STRING).description("가수"),
                                                fieldWithPath("data.[].lyricist").type(JsonFieldType.STRING).description("작사"),
                                                fieldWithPath("data.[].composer").type(JsonFieldType.STRING).description("작곡"),
                                                fieldWithPath("data.[].youtubeUrl").type(JsonFieldType.STRING).description("YouTube URL"),
                                                fieldWithPath("data.[].nation").type(JsonFieldType.STRING).description("국가"),
                                                fieldWithPath("data.[].orderNum").type(JsonFieldType.NUMBER).description("저장 순서")

                                        ))));
    }

    @Test
    @DisplayName("회원 플레이 리스트 수정 TEST")
    @WithMockUser
    void modifyPlayList() throws Exception {
        // Given
        Token token = createToken();
        Long playListId = 1L;
        PlayListRequestDto request = StubUtils.createPlayListRequestDto();
        Gson gson = new Gson();
        String content = gson.toJson(request);
        given(userMusicService.modifyPlayList(anyLong(), any(PlayListRequestDto.class)))
                .willReturn(ResponseDto.of(StubUtils.createPlayListDto()));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .patch("/api/user-music/playlist/{playListId}", playListId)
                .header("Authorization", token.getAccessToken())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("modifyPlayList",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("playListId").description("플레이 리스트 식별자")
                                ),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                                fieldWithPath("title").type(JsonFieldType.STRING).description("플레이 리스트 제목"),
                                                fieldWithPath("color").type(JsonFieldType.STRING).description("컬러 코드"),
                                                fieldWithPath("emoji").type(JsonFieldType.STRING).description("이모지 코드")
                                        )

                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                                fieldWithPath("data.playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("플레이 리스트 제목"),
                                                fieldWithPath("data.color").type(JsonFieldType.STRING).description("컬러 코드"),
                                                fieldWithPath("data.emoji").type(JsonFieldType.STRING).description("이모지 코드")
                                        ))));
    }

    @Test
    @DisplayName("회원 플레이 리스트의 음악 수정 TEST")
    @WithMockUser
    void modifyUserMusic() throws Exception {
        // Given
        Token token = createToken();
        Long playListId = 1L;
        PlayListModifyRequestDto request = StubUtils.createPlayListModifyRequestDto();
        Gson gson = new Gson();
        String content = gson.toJson(request);
        UserMusicDto dto1 = StubUtils.createUserMusicDto(1);
        UserMusicDto dto2 = StubUtils.createUserMusicDto(2);
        List<UserMusicDto> dtoList = List.of(dto1, dto2);
        Page page = new PageImpl(dtoList);
        PageResponseDto response = PageResponseDto.of(dtoList, page);
        given(userMusicService.modifyUserMusic(any(PlayListModifyRequestDto.class)))
                .willReturn(response);
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .patch("/api/user-music/contents")
                .header("Authorization", token.getAccessToken())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("modifyUserMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                ),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("orderList").type(JsonFieldType.ARRAY).description("순서 수정 요청 값")
                                        )

                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data[].userMusicId").type(JsonFieldType.STRING).description("저장 음악 식별자"),
                                                fieldWithPath("data[].playListId").type(JsonFieldType.STRING).description("플레이 리스트 식별자"),
                                                fieldWithPath("data[].musicId").type(JsonFieldType.STRING).description("음악 식별자"),
                                                fieldWithPath("data[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data[].singer").type(JsonFieldType.STRING).description("가수"),
                                                fieldWithPath("data[].lyricist").type(JsonFieldType.STRING).description("작사"),
                                                fieldWithPath("data[].composer").type(JsonFieldType.STRING).description("작곡"),
                                                fieldWithPath("data[].youtubeUrl").type(JsonFieldType.STRING).description("YouTube URL"),
                                                fieldWithPath("data[].nation").type(JsonFieldType.STRING).description("국가"),
                                                fieldWithPath("data[].orderNum").type(JsonFieldType.NUMBER).description("저장 순서"),
                                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("요청 페이지 정보"),
                                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("요청 페이지 - 0 = 1 페이지"),
                                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지당 요청 회원"),
                                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 멤버"),
                                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("생성된 총 페이지")

                                        ))));
    }


    @Test
    @DisplayName("특정 플레이 리스트 삭제 TEST")
    @WithMockUser
    void deletePlayList() throws Exception {
        // Given
        Token token = createToken();
        Long playListId = 1L;
        doNothing().when(userMusicService).deleteUserPlayList(anyLong());
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .delete("/api/user-music/playlist/{playListId}", playListId)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isNoContent())
                .andDo(
                        MockMvcRestDocumentation.document("deletePlayList",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.pathParameters(
                                        parameterWithName("playListId").description("플레이 리스트 식별자")
                                ),
                                HeaderDocumentation.requestHeaders(
                                        headerWithName("Authorization").description("AccessToken")
                                )
                        ));
    }


    @Test
    @DisplayName("플레이 리스트 저장 음악 부분 삭제 TEST")
    @WithMockUser
    void deleteUserMusic() throws Exception {
        // Given
        Token token = createToken();
        PlayListModifyRequestDto request = StubUtils.createPlayListModifyRequestDto();
        Gson gson = new Gson();
        String content = gson.toJson(request);
        doNothing().when(userMusicService).deleteUserMusic(any(PlayListModifyRequestDto.class));
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .patch("/api/user-music")
                .content(content)
                .header("Authorization", token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isNoContent())
                .andDo(
                        MockMvcRestDocumentation.document("deleteUserMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                PayloadDocumentation.requestFields(
                                        List.of(
                                                fieldWithPath("playListId").type(JsonFieldType.NUMBER).description("플레이 리스트 식별자"),
                                                fieldWithPath("orderList").type(JsonFieldType.ARRAY).description("순서 수정 요청 값")
                                        )

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