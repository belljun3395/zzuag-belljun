package com.zzaug.api.domain.member.usecase;

import com.zzaug.api.domain.member.dao.auth.EmailAuthDao;
import com.zzaug.api.domain.member.dao.member.ExternalContactDao;
import com.zzaug.api.domain.member.data.entity.auth.EmailAuthEntity;
import com.zzaug.api.domain.member.data.entity.auth.EmailData;
import com.zzaug.api.domain.member.data.entity.member.ContactType;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseRequest;
import com.zzaug.api.domain.member.dto.EmailAuthUseCaseResponse;
import com.zzaug.api.domain.member.event.MemberEmailAuthMessage;
import com.zzaug.api.domain.member.util.usecase.RandomAuthCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailAuthUseCase {

	private final ExternalContactDao externalContactDao;
	private final EmailAuthDao emailAuthDao;

	private final RandomAuthCodeGenerator randomAuthCodeGenerator;

	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public EmailAuthUseCaseResponse execute(EmailAuthUseCaseRequest request) {
		final Long memberId = request.getMemberId();
		final EmailData email = EmailData.builder().email(request.getEmail()).build();
		final String nonce = request.getNonce();
		final String authCode = randomAuthCodeGenerator.generate(7);
		log.debug("AuthCode is generated : {}", authCode);

		// 이미 존재하는 Email인지 확인
		boolean isEmailExist =
				externalContactDao.existsByContactTypeAndSourceAndDeletedFalse(
						ContactType.EMAIL, email.getEmail());
		if (isEmailExist) {
			return EmailAuthUseCaseResponse.builder().duplication(true).build();
		}

		EmailAuthEntity emailAuthSource =
				EmailAuthEntity.builder()
						.memberId(memberId)
						.email(email)
						.nonce(nonce)
						.code(authCode)
						.build();
		emailAuthDao.saveEmailAuth(emailAuthSource);

		// Email 인증 메시지 발송을 위한 이벤트 발행
		applicationEventPublisher.publishEvent(
				MemberEmailAuthMessage.builder()
						.memberId(memberId)
						.email(email.getEmail())
						.code(authCode)
						.build());

		return EmailAuthUseCaseResponse.builder().duplication(false).build();
	}
}
