package com.zzaug.api.web.controller.v1.member;

import com.zzaug.api.domain.member.dto.AccessTokenResponse;
import com.zzaug.api.domain.member.dto.DeleteMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.GetMemberUseCaseResponse;
import com.zzaug.api.domain.member.dto.LoginUseCaseRequest;
import com.zzaug.api.domain.member.dto.MemberAuthToken;
import com.zzaug.api.domain.member.dto.PostMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.RenewalTokenUseCaseRequest;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.SearchMemberUseCaseResponse;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseRequest;
import com.zzaug.api.domain.member.dto.UpdateMemberUseCaseResponse;
import com.zzaug.api.domain.member.usecase.DeleteMemberUseCase;
import com.zzaug.api.domain.member.usecase.GetMemberUseCase;
import com.zzaug.api.domain.member.usecase.LoginUseCase;
import com.zzaug.api.domain.member.usecase.PostMemberUseCase;
import com.zzaug.api.domain.member.usecase.RenewalTokenUseCase;
import com.zzaug.api.domain.member.usecase.SearchMemberUseCase;
import com.zzaug.api.domain.member.usecase.UpdateMemberUseCase;
import com.zzaug.api.security.authentication.token.TokenUserDetails;
import com.zzaug.api.security.token.TokenResolver;
import com.zzaug.api.web.dto.member.LoginRequest;
import com.zzaug.api.web.dto.member.MemberSaveRequest;
import com.zzaug.api.web.dto.member.MemberUpdateRequest;
import com.zzaug.api.web.dto.validator.PositiveId;
import com.zzaug.api.web.support.ApiResponse;
import com.zzaug.api.web.support.ApiResponseGenerator;
import com.zzaug.api.web.support.CookieGenerator;
import com.zzaug.api.web.support.CookieSameSite;
import com.zzaug.api.web.support.MessageCode;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
	private static final String MEMBER_ID_SESSION_KEY = "memberId";

	private final CookieGenerator cookieGenerator;
	private final TokenResolver tokenResolver;

	private final GetMemberUseCase getMemberUseCase;
	private final UpdateMemberUseCase updateMemberUseCase;
	private final PostMemberUseCase postMemberUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;
	private final LoginUseCase loginUseCase;
	private final RenewalTokenUseCase renewalTokenUseCase;
	private final SearchMemberUseCase searchMemberUseCase;

	@PostMapping()
	public ApiResponse<ApiResponse.Success> save(@Valid @RequestBody MemberSaveRequest request) {
		PostMemberUseCaseRequest useCaseRequest =
				PostMemberUseCaseRequest.builder()
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		postMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@PutMapping()
	public ApiResponse<ApiResponse.SuccessBody<AccessTokenResponse>> update(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestBody MemberUpdateRequest request) {
		Long memberId = Long.valueOf(userDetails.getId());
		UpdateMemberUseCaseRequest useCaseRequest =
				UpdateMemberUseCaseRequest.builder()
						.memberId(memberId)
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		UpdateMemberUseCaseResponse response = updateMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(
				response.toResponse(), HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping()
	public ApiResponse<ApiResponse.Success> delete(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		Long memberId = Long.valueOf(userDetails.getId());
		DeleteMemberUseCaseRequest useCaseRequest =
				DeleteMemberUseCaseRequest.builder().memberId(memberId).build();
		deleteMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@PostMapping("/login")
	public ApiResponse<ApiResponse.SuccessBody<AccessTokenResponse>> login(
			@Valid @RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
		LoginUseCaseRequest useCaseRequest =
				LoginUseCaseRequest.builder()
						.certification(request.getCertification())
						.password(request.getPassword())
						.build();
		MemberAuthToken response = loginUseCase.execute(useCaseRequest);
		ResponseCookie refreshToken =
				cookieGenerator.createCookie(
						CookieSameSite.LAX, REFRESH_TOKEN_COOKIE_NAME, response.getRefreshToken());
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		return ApiResponseGenerator.success(response.toResponse(), HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/{id}")
	public ApiResponse<ApiResponse.SuccessBody<GetMemberUseCaseResponse>> read(
			@AuthenticationPrincipal TokenUserDetails userDetails, @PathVariable @PositiveId Long id) {
		Long memberId = Long.valueOf(userDetails.getId());
		GetMemberUseCaseRequest useCaseRequest =
				GetMemberUseCaseRequest.builder().memberId(memberId).queryMemberId(id).build();
		GetMemberUseCaseResponse response = getMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<SearchMemberUseCaseResponse>> search(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam(value = "certification", required = false) String certification) {
		Long memberId = Long.valueOf(userDetails.getId());
		SearchMemberUseCaseRequest useCaseRequest =
				SearchMemberUseCaseRequest.builder()
						.memberId(memberId)
						.certification(certification)
						.build();
		SearchMemberUseCaseResponse response = searchMemberUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/token")
	public ApiResponse<ApiResponse.SuccessBody<AccessTokenResponse>> token(
			@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshTokenValue,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Long memberId = resolveMemberId(refreshTokenValue);
		RenewalTokenUseCaseRequest useCaseRequest =
				RenewalTokenUseCaseRequest.builder().memberId(memberId).build();
		MemberAuthToken response = renewalTokenUseCase.execute(useCaseRequest);
		ResponseCookie refreshToken =
				cookieGenerator.createCookie(
						CookieSameSite.LAX, REFRESH_TOKEN_COOKIE_NAME, response.getRefreshToken());
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		httpServletRequest.getSession().setAttribute(MEMBER_ID_SESSION_KEY, response.getMemberId());
		return ApiResponseGenerator.success(response.toResponse(), HttpStatus.OK, MessageCode.SUCCESS);
	}

	@NotNull
	private Long resolveMemberId(String refreshToken) {
		Optional<Long> idSource = tokenResolver.resolveId(refreshToken);
		if (idSource.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return idSource.get();
	}
}
