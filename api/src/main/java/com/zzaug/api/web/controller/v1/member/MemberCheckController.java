package com.zzaug.api.web.controller.v1.member;

import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckCertificationDuplicationUseCaseResponse;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.CheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.dto.SuccessCheckEmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.usecase.CheckCertificationDuplicationUseCase;
import com.zzaug.api.domain.member.usecase.CheckEmailAuthUseCase;
import com.zzaug.api.domain.member.usecase.EmailAuthUseCase;
import com.zzaug.api.security.authentication.token.TokenUserDetails;
import com.zzaug.api.web.dto.member.CheckEmailAuthRequest;
import com.zzaug.api.web.dto.validator.Certification;
import com.zzaug.api.web.support.ApiResponse;
import com.zzaug.api.web.support.ApiResponseGenerator;
import com.zzaug.api.web.support.CookieGenerator;
import com.zzaug.api.web.support.CookieSameSite;
import com.zzaug.api.web.support.MessageCode;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/members/check")
@RequiredArgsConstructor
public class MemberCheckController {

	private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

	private final CookieGenerator cookieGenerator;

	private final CheckCertificationDuplicationUseCase checkCertificationDuplicationUseCase;
	private final EmailAuthUseCase emailAuthUseCase;
	private final CheckEmailAuthUseCase checkEmailAuthUseCase;

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<CheckCertificationDuplicationUseCaseResponse>> check(
			@Certification @RequestParam(value = "certification", required = true) String certification) {
		CheckCertificationDuplicationUseCaseRequest useCaseRequest =
				CheckCertificationDuplicationUseCaseRequest.builder().certification(certification).build();
		CheckCertificationDuplicationUseCaseResponse response =
				checkCertificationDuplicationUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	// todo 너무 많은 이메일 인증 요청을 보내는 것은 아닌지 확인한다.
	@GetMapping("/email")
	public ApiResponse<ApiResponse.SuccessBody<EmailAuthUseCaseResponse>> emailAuth(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Email @RequestParam(value = "email", required = true) String email,
			@NotNull @NotEmpty @RequestParam(value = "nonce", required = true) String nonce) {
		Long memberId = Long.valueOf(userDetails.getId());
		EmailAuthUseCaseRequest useCaseRequest =
				EmailAuthUseCaseRequest.builder().memberId(memberId).email(email).nonce(nonce).build();
		EmailAuthUseCaseResponse response = emailAuthUseCase.execute(useCaseRequest);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/email")
	public ApiResponse<ApiResponse.SuccessBody<CheckEmailAuthUseCaseResponse>> checkEmailAuth(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody CheckEmailAuthRequest request,
			HttpServletResponse httpServletResponse) {
		Long memberId = Long.valueOf(userDetails.getId());
		CheckEmailAuthUseCaseRequest useCaseRequest =
				CheckEmailAuthUseCaseRequest.builder()
						.memberId(memberId)
						.code(request.getCode())
						.email(request.getEmail())
						.nonce(request.getNonce())
						.build();
		CheckEmailAuthUseCaseResponse response = checkEmailAuthUseCase.execute(useCaseRequest);
		if (!(response instanceof SuccessCheckEmailAuthUseCaseResponse)) {
			return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
		}
		SuccessCheckEmailAuthUseCaseResponse successResponse =
				(SuccessCheckEmailAuthUseCaseResponse) response;
		ResponseCookie refreshToken =
				cookieGenerator.createCookie(
						CookieSameSite.LAX, REFRESH_TOKEN_COOKIE_NAME, successResponse.getRefreshToken());
		httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		return ApiResponseGenerator.success(successResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
