package com.zzaug.api.security.persistence.log;

import com.zzaug.api.security.entity.log.InvalidTokenAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenAccessRepository
		extends JpaRepository<InvalidTokenAccessEntity, Long> {}
