package com.zzaug.api.security.persistence.auth;

import com.zzaug.api.security.entity.auth.BlackTokenAuthEntity;
import com.zzaug.api.security.entity.auth.TokenData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackTokenAuthRepository extends JpaRepository<BlackTokenAuthEntity, Long> {

	boolean existsByTokenAndDeletedFalse(TokenData token);
}
