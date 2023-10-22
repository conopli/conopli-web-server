package conopli.webserver.search.controller;

import com.google.gson.Gson;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.auth.token.refresh.repository.RefreshRepository;
import conopli.webserver.auth.token.refresh.service.RefreshService;
import conopli.webserver.config.MusicDomainConfig;
import conopli.webserver.config.SecurityConfig;
import conopli.webserver.search.dto.PopularRequestDto;
import conopli.webserver.search.dto.SearchDto;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.service.UserService;
import conopli.webserver.utils.ApiDocumentUtils;
import conopli.webserver.utils.StubUtils;
import conopli.webserver.utils.UrlCreateUtil;
import conopli.webserver.webhook.WebHookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
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
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ActiveProfiles("local")
@Import({JwtAuthorityUtils.class,
        JwtTokenizer.class,
        HttpClientService.class,
        UrlCreateUtil.class,
        MusicDomainConfig.class,
        WebHookService.class,
        SecurityConfig.class})
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private HttpClientService httpClientService;

    @MockBean
    private UserService userService;

    @MockBean
    private RefreshService refreshService;

    @MockBean
    private RefreshRepository refreshRepository;


    @Test
    @DisplayName("음악 검색 TEST")
    @WithMockUser
    void searchMusic() throws Exception {
        // Given
        SearchDto searchDto = StubUtils.createSearchDto();
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/search?searchType="+searchDto.getSearchType()+"&searchKeyWord="+searchDto.getSearchKeyWord()+"&searchNation="+searchDto.getSearchNation()+"&page="+searchDto.getPage())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("searchMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.queryParameters(
                                        parameterWithName("searchType").description("검색 타입 (1 = 제목, 2 = 가수 , 4 = 작사가 , 8 = 작곡가, 16 = 곡번호)"),
                                        parameterWithName("page").description("요청 페이지 정보"),
                                        parameterWithName("searchKeyWord").description("검색 키워드"),
                                        parameterWithName("searchNation").description("검색 국가")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data[].musicId").type(JsonFieldType.NUMBER).description("음악 식별자"),
                                                fieldWithPath("data[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data[].singer").type(JsonFieldType.STRING).description("가수"),
                                                fieldWithPath("data[].lyricist").type(JsonFieldType.STRING).description("작사"),
                                                fieldWithPath("data[].composer").type(JsonFieldType.STRING).description("작곡"),
                                                fieldWithPath("data[].youtubeUrl").type(JsonFieldType.STRING).description("YouTube URL"),
                                                fieldWithPath("data[].nation").type(JsonFieldType.STRING).description("국가"),
                                                fieldWithPath("data[].kyNum").type(JsonFieldType.STRING).description("KY 곡번호").optional(),
                                                fieldWithPath("data[].mrSound").type(JsonFieldType.BOOLEAN).description("MR 음원 여부"),
                                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("요청 페이지 정보"),
                                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("요청 페이지 - 0 = 1 페이지"),
                                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지당 요청 회원"),
                                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 멤버"),
                                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("생성된 총 페이지")

                                        ))));
    }

    @Test
    @DisplayName("인기 음악 조회 TEST")
    @WithMockUser
    void popularMusic() throws Exception {
        // Given
        PopularRequestDto requestDto = StubUtils.createPopularRequestDto();
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/search/popular?searchType="+requestDto.getSearchType()+"&syy="+requestDto.getSyy()+"&smm="+requestDto.getSmm()+"&eyy="+requestDto.getEyy()+"&emm="+requestDto.getEmm())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("popularMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.queryParameters(
                                        parameterWithName("searchType").description("검색 타입 (1 = 제목, 2 = 가수 , 4 = 작사가 , 8 = 작곡가, 16 = 곡번호)"),
                                        parameterWithName("syy").description("시작 연도(ex. 2023)"),
                                        parameterWithName("smm").description("시작 월(ex. 04)"),
                                        parameterWithName("eyy").description("종료 연도(ex. 2023)"),
                                        parameterWithName("emm").description("종료 월(ex. 04)")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data[].ranking").type(JsonFieldType.STRING).description("순위"),
                                                fieldWithPath("data[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data[].singer").type(JsonFieldType.STRING).description("가수")

                                        ))));
    }

    @Test
    @DisplayName("신곡 조회 TEST")
    @WithMockUser
    void newMusic() throws Exception {
        // Given
        // When
        RequestBuilder result = RestDocumentationRequestBuilders
                .get("/api/search/new-music?yy=2023&mm=09")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.displayName());
        // Then
        mockMvc.perform(result)
                .andExpect(status().isOk())
                .andDo(
                        MockMvcRestDocumentation.document("newMusic",
                                ApiDocumentUtils.getRequestPreProcessor(),
                                ApiDocumentUtils.getResponsePreProcessor(),
                                RequestDocumentation.queryParameters(
                                        parameterWithName("yy").description("검색 연도(ex. 2023)"),
                                        parameterWithName("mm").description("검색 월(ex. 04)")
                                ),
                                PayloadDocumentation.responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                                fieldWithPath("data[].num").type(JsonFieldType.STRING).description("곡번호"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                                                fieldWithPath("data[].singer").type(JsonFieldType.STRING).description("가수"),
                                                fieldWithPath("data[].lyricist").type(JsonFieldType.STRING).description("작사"),
                                                fieldWithPath("data[].composer").type(JsonFieldType.STRING).description("작곡"),
                                                fieldWithPath("data[].youtubeUrl").type(JsonFieldType.STRING).description("YouTube URL"),
                                                fieldWithPath("data[].nation").type(JsonFieldType.STRING).description("국가")
                                        ))));
    }



}