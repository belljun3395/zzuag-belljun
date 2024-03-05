package com.zzaug.api.web.controller.v1.member;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseResponse;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.usecase.CheckCertificationDuplicationUseCase;
import com.zzaug.api.domain.member.usecase.CheckEmailAuthUseCase;
import com.zzaug.api.domain.member.usecase.EmailAuthUseCase;
import com.zzaug.api.web.controller.config.TestTokenUserDetailsService;
import com.zzaug.api.web.controller.v1.description.Description;
import com.zzaug.api.web.controller.v1.description.MemberCheckDescription;
import com.zzaug.api.web.controller.v1.description.MemberCheckRequestDescription;
import com.zzaug.api.web.dto.member.CheckEmailAuthRequest;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = {"test", "api"})
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {ApiApp.class, TestTokenUserDetailsService.class})
class MemberCheckControllerTest {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean CheckCertificationDuplicationUseCase checkCertificationDuplicationUseCase;
	@MockBean EmailAuthUseCase emailAuthUseCase;
	@MockBean CheckEmailAuthUseCase checkEmailAuthUseCase;

	@Value("${token.test}")
	private String testToken;

	private static final String TAG = "MemberCheckController";
	private static final String BASE_URL = "/api/v1/members/check";

	private static final String POST_BASE_URL_DNAME = "[POST] " + BASE_URL;
	private static final String GET_BASE_URL_DNAME = "[GET] " + BASE_URL;
	private static final String PUT_BASE_URL_DNAME = "[PUT] " + BASE_URL;
	private static final String DELETE_BASE_URL_DNAME = "[DELETE] " + BASE_URL;

	private static final String X_ZZAUG_ID = "X-ZZAUG-ID";
	private static final String CERTIFICATION = "certification";
	private static final String PASSWORD = "password@123";
	private static final String EMAIL = "sample@email.com";
	private static final String NONCE = "nonce";
	private static final String CODE = "code";

	@Test
	@DisplayName(GET_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void check() throws Exception {
		// set service mock
		when(checkCertificationDuplicationUseCase.execute(any()))
				.thenReturn(
						CheckCertificationDuplicationUseCaseResponse.builder().duplication(true).build());

		mockMvc
				.perform(
						get(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.param("certification", CERTIFICATION)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"CheckMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("아이디 중복 검사를 진행합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("CheckMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestParameters(
														parameterWithName("certification").description("증명(아이디)"))
												.responseSchema(Schema.schema("CheckMemberResponse"))
												.responseFields(Description.success(MemberCheckDescription.checkMember()))
												.build())));
	}

	@Test
	@DisplayName(GET_BASE_URL_DNAME + "/email")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void emailAuth() throws Exception {
		// set service mock
		when(emailAuthUseCase.execute(any()))
				.thenReturn(EmailAuthUseCaseResponse.builder().duplication(true).build());

		mockMvc
				.perform(
						get(BASE_URL + "/email", 0)
								.contentType(MediaType.APPLICATION_JSON)
								.param("email", EMAIL)
								.param("nonce", NONCE)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"EmailAuth",
								resource(
										ResourceSnippetParameters.builder()
												.description("이메일 인증 요청을 진행합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("EmailAuthRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestParameters(
														parameterWithName("email").description("이메일"),
														parameterWithName("nonce").description("인증번호"))
												.responseSchema(Schema.schema("EmailAuthResponse"))
												.responseFields(Description.success(MemberCheckDescription.emailAuth()))
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME + "/email")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void checkEmailAuth() throws Exception {
		// set service mock
		CheckEmailAuthRequest request =
				CheckEmailAuthRequest.builder().code(CODE).email(EMAIL).nonce(NONCE).build();
		String content = objectMapper.writeValueAsString(request);
		Cookie cookie = new Cookie("refreshToken", testToken);

		when(checkEmailAuthUseCase.execute(any()))
				.thenReturn(
						CheckEmailAuthUseCaseResponse.builder().authentication(true).tryCount(1L).build());

		mockMvc
				.perform(
						post(BASE_URL + "/email", 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
								.cookie(cookie))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"CheckEmailAuth",
								resource(
										ResourceSnippetParameters.builder()
												.description("이메일 인증 번호 확인합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("CheckEmailAuthRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberCheckRequestDescription.checkEmailAuthRequest())
												.responseSchema(Schema.schema("CheckEmailAuthResponse"))
												.responseFields(
														Description.success(MemberCheckDescription.checkEmailAuth()))
												.build())));
	}
}
