package com.zzaug.api.web.controller.v1.member;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzaug.api.ApiApp;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseResponse;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.dto.PostMemberUseCaseResponse;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseResponse;
import com.zzaug.api.domain.member.usecase.DeleteMemberUseCase;
import com.zzaug.api.domain.member.usecase.GetMemberUseCase;
import com.zzaug.api.domain.member.usecase.LoginUseCase;
import com.zzaug.api.domain.member.usecase.PostMemberUseCase;
import com.zzaug.api.domain.member.usecase.ReadMemberByCertificationUseCase;
import com.zzaug.api.domain.member.usecase.RenewalTokenUseCase;
import com.zzaug.api.domain.member.usecase.UpdateMemberUseCase;
import com.zzaug.api.web.controller.config.TestTokenUserDetailsService;
import com.zzaug.api.web.controller.v1.description.Description;
import com.zzaug.api.web.controller.v1.description.MemberDescription;
import com.zzaug.api.web.controller.v1.description.MemberRequestDescription;
import com.zzaug.api.web.dto.member.LoginRequest;
import com.zzaug.api.web.dto.member.MemberSaveRequest;
import com.zzaug.api.web.dto.member.MemberUpdateRequest;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.apache.logging.log4j.util.Strings;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = {"test", "api"})
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = {ApiApp.class, TestTokenUserDetailsService.class})
class MemberControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@MockBean GetMemberUseCase getMemberUseCase;
	@MockBean UpdateMemberUseCase updateMemberUseCase;
	@MockBean PostMemberUseCase postMemberUseCase;
	@MockBean DeleteMemberUseCase deleteMemberUseCase;
	@MockBean LoginUseCase loginUseCase;
	@MockBean RenewalTokenUseCase renewalTokenUseCase;
	@MockBean ReadMemberByCertificationUseCase readMemberByCertificationUseCase;

	@Value("${token.test}")
	private String testToken;

	private static final String TAG = "MemberController";
	private static final String BASE_URL = "/api/v1/members";

	private static final String POST_BASE_URL_DNAME = "[POST] " + BASE_URL;
	private static final String GET_BASE_URL_DNAME = "[GET] " + BASE_URL;
	private static final String PUT_BASE_URL_DNAME = "[PUT] " + BASE_URL;
	private static final String DELETE_BASE_URL_DNAME = "[DELETE] " + BASE_URL;

	private static final String X_ZZAUG_ID = "X-ZZAUG-ID";
	private static final String CERTIFICATION = "certification12";
	private static final String PASSWORD = "password@123";

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification(CERTIFICATION).password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);

		when(postMemberUseCase.execute(any()))
				.thenReturn(
						PostMemberUseCaseResponse.builder()
								.memberId(1L)
								.certification(CERTIFICATION)
								.password(PASSWORD)
								.build());

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"SaveMember",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.responseSchema(Schema.schema("SaveMemberResponse"))
												.responseFields(Description.success())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_certification_under_min_length() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification("ab1").password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_certification_under_min_length",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_certification_over_max_length() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification("abcdefgh123456789").password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_certification_over_max_length",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_certification_is_english_and_number_only() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification("certification@123").password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_certification_is_english_and_number_only",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_password_under_min_length() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification(CERTIFICATION).password("abc@123").build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_password_under_min_length",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_password_over_max_length() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder()
						.certification(CERTIFICATION)
						.password("abcdefg@123456789")
						.build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_password_over_max_length",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void save_password_is_english_and_number_and_special() throws Exception {
		// set service mock
		MemberSaveRequest request =
				MemberSaveRequest.builder().certification(CERTIFICATION).password("password123").build();
		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is4xxClientError())
				.andDo(
						document(
								"SaveMember_password_over_max_length",
								resource(
										ResourceSnippetParameters.builder()
												.summary("회원가입을 진행합니다.")
												.description(
														"회원가입을 진행합니다. 다른 API에서의 Certification과 Password에 관한 규칙은 회원가입을 따릅니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("SaveMember_400_fail_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberSaveRequest())
												.responseSchema(Schema.schema("SaveMember_400_fail_response"))
												.responseFields(Description.fail())
												.build())));
	}

	@Test
	@DisplayName(PUT_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void update() throws Exception {
		// set service mock
		MemberUpdateRequest request =
				MemberUpdateRequest.builder().certification(CERTIFICATION).password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);
		Cookie cookie = new Cookie("refreshToken", testToken);

		when(updateMemberUseCase.execute(any()))
				.thenReturn(
						UpdateMemberUseCaseResponse.builder()
								.accessToken(testToken)
								.refreshToken(testToken)
								.build());

		mockMvc
				.perform(
						put(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
								.cookie(cookie))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"UpdateMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("회원정보를 수정합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("UpdateMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.memberUpdateRequest())
												.responseSchema(Schema.schema("UpdateMemberResponse"))
												.responseFields(Description.success(MemberDescription.updateMember()))
												.build())));
	}

	@Test
	@DisplayName(DELETE_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void delete() throws Exception {
		// set service mock
		Cookie cookie = new Cookie("refreshToken", testToken);

		mockMvc
				.perform(
						RestDocumentationRequestBuilders.delete(BASE_URL, 0)
								.contentType(MediaType.APPLICATION_JSON)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
								.cookie(cookie))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"DeleteMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("회원 탈퇴를 진행합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("DeleteMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(Schema.schema("DeleteMemberResponse"))
												.responseFields(Description.deleted())
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME + "/login")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void login() throws Exception {
		// set service mock
		LoginRequest request =
				LoginRequest.builder().certification(CERTIFICATION).password(PASSWORD).build();
		String content = objectMapper.writeValueAsString(request);

		when(loginUseCase.execute(any()))
				.thenReturn(
						MemberAuthToken.builder().accessToken(testToken).refreshToken(testToken).build());

		mockMvc
				.perform(
						post(BASE_URL + "/login", 0)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
								.header(HttpHeaders.USER_AGENT, "agent"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"LoginMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("로그인을 진행합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("LoginMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.requestFields(MemberRequestDescription.loginRequest())
												.responseSchema(Schema.schema("LoginMemberResponse"))
												.responseFields(Description.success(MemberDescription.loginMember()))
												.responseHeaders(
														new HeaderDescriptorWithType[] {Description.setCookieHeader()})
												.build())));
	}

	@Test
	@DisplayName(GET_BASE_URL_DNAME + "/{id}")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void read() throws Exception {
		// set service mock
		when(getMemberUseCase.execute(any()))
				.thenReturn(
						GetMemberUseCaseResponse.builder()
								.id(1L)
								.certification("certification")
								.email("sample@email.com")
								.github("github")
								.build());

		mockMvc
				.perform(
						get(BASE_URL + "/{id}", 1)
								.contentType(MediaType.APPLICATION_JSON)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"ReadMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("멤버 정보를 조회합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("ReadMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.pathParameters(parameterWithName("id").description("조회할 멤버의 아이디"))
												.responseSchema(Schema.schema("ReadMemberResponse"))
												.responseFields(Description.success(MemberDescription.readMember()))
												.build())));
	}

	@Test
	@DisplayName(GET_BASE_URL_DNAME + "/certification/{certification}")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void readByCertification() throws Exception {
		// set service mock
		when(readMemberByCertificationUseCase.execute(any()))
				.thenReturn(
						SearchMemberUseCaseResponse.builder()
								.id(1L)
								.certification("certification")
								.email("sample@email.com")
								.github("github")
								.build());

		mockMvc
				.perform(
						get(BASE_URL + "/certification/{certification}", CERTIFICATION)
								.contentType(MediaType.APPLICATION_JSON)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"ReadByCertificationMember",
								resource(
										ResourceSnippetParameters.builder()
												.description("증명(아이디)를 조회합니다.")
												.requestParameters(
														parameterWithName("certification").description("증명(아이디)").optional())
												.tag(TAG)
												.requestSchema(Schema.schema("ReadByCertificationMemberRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(Schema.schema("ReadByCertificationMemberResponse"))
												.responseFields(Description.success(MemberDescription.searchMember()))
												.build())));
	}

	@Test
	@DisplayName(GET_BASE_URL_DNAME)
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void readByCertification_not_exist_target() throws Exception {
		// set service mock
		when(readMemberByCertificationUseCase.execute(any()))
				.thenReturn(
						SearchMemberUseCaseResponse.builder()
								.id(Long.MIN_VALUE)
								.certification(Strings.EMPTY)
								.email(Strings.EMPTY)
								.github(Strings.EMPTY)
								.build());

		mockMvc
				.perform(
						get(BASE_URL + "/certification/{certification}", "notExistCertification")
								.contentType(MediaType.APPLICATION_JSON)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"ReadByCertificationMember_not_exist_target",
								resource(
										ResourceSnippetParameters.builder()
												.description("존재하지 않는 증명(아이디)를 조회합니다.")
												.requestParameters(
														parameterWithName("certification").description("증명(아이디)").optional())
												.tag(TAG)
												.requestSchema(
														Schema.schema("ReadByCertificationMember_not_exist_target_request"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(
														Schema.schema("ReadByCertificationMember_not_exist_target_response"))
												.responseFields(Description.success(MemberDescription.searchMember()))
												.build())));
	}

	@Test
	@DisplayName(POST_BASE_URL_DNAME + "/token")
	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
	void token() throws Exception {
		// set service mock
		Cookie cookie = new Cookie("refreshToken", testToken);

		when(renewalTokenUseCase.execute(any()))
				.thenReturn(
						MemberAuthToken.builder().accessToken(testToken).refreshToken(testToken).build());

		mockMvc
				.perform(
						post(BASE_URL + "/token", 0)
								.contentType(MediaType.APPLICATION_JSON)
								.header(X_ZZAUG_ID, UUID.randomUUID())
								.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
								.cookie(cookie))
				.andExpect(status().is2xxSuccessful())
				.andDo(
						document(
								"RenewalToken",
								resource(
										ResourceSnippetParameters.builder()
												.description("토큰을 갱신합니다.")
												.tag(TAG)
												.requestSchema(Schema.schema("RenewalTokenRequest"))
												.requestHeaders(Description.authHeader(), Description.xZzaugIdHeader())
												.responseSchema(Schema.schema("RenewalTokenResponse"))
												.responseFields(Description.success(MemberDescription.renewalToken()))
												.responseHeaders(
														new HeaderDescriptorWithType[] {Description.setCookieHeader()})
												.build())));
	}
}
