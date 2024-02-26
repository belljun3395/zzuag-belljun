package com.zzaug.api.domain.member.dao.member;

import com.zzaug.api.domain.member.data.entity.member.AuthenticationEntity;
import com.zzaug.api.domain.member.data.entity.member.CertificationData;
import com.zzaug.api.domain.member.data.entity.member.ContactType;
import com.zzaug.api.domain.member.data.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberEntity;
import com.zzaug.api.domain.member.data.entity.member.MemberStatus;
import com.zzaug.api.domain.member.data.persistence.member.AuthenticationRepository;
import com.zzaug.api.domain.member.data.persistence.member.ExternalContactRepository;
import com.zzaug.api.domain.member.data.persistence.member.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile("!usecase-test")
@Repository
@RequiredArgsConstructor
public class MemberDaoImpl implements MemberDao {

	private final MemberRepository memberRepository;
	private final ExternalContactRepository externalContactRepository;
	private final AuthenticationRepository authenticationRepository;

	@Override
	public Optional<MemberEntity> findByIdAndStatusAndDeletedFalse(Long id, MemberStatus status) {
		return memberRepository.findByIdAndStatusAndDeletedFalse(id, status);
	}

	@Override
	public MemberEntity saveSource(MemberEntity memberEntity) {
		return memberRepository.save(memberEntity);
	}

	@Override
	public List<ExternalContactEntity> findAllByMemberIdAndDeletedFalse(Long memberId) {
		return externalContactRepository.findAllByMemberIdAndDeletedFalse(memberId);
	}

	@Override
	public boolean existsByContactTypeAndSourceAndDeletedFalse(ContactType type, String source) {
		return externalContactRepository.existsByContactTypeAndSourceAndDeletedFalse(type, source);
	}

	@Override
	public boolean existsByCertificationAndDeletedFalse(CertificationData certification) {
		return authenticationRepository.existsByCertificationAndDeletedFalse(certification);
	}

	@Override
	public Optional<AuthenticationEntity> findByMemberIdAndDeletedFalse(Long memberId) {
		return authenticationRepository.findByMemberIdAndDeletedFalse(memberId);
	}

	@Override
	public Optional<AuthenticationEntity> findByCertificationAndDeletedFalse(
			CertificationData certification) {
		return authenticationRepository.findByCertificationAndDeletedFalse(certification);
	}

	@Override
	public AuthenticationEntity saveAuthentication(AuthenticationEntity authenticationEntity) {
		return authenticationRepository.save(authenticationEntity);
	}

	@Override
	public ExternalContactEntity saveContact(ExternalContactEntity externalContactEntity) {
		return externalContactRepository.save(externalContactEntity);
	}
}
