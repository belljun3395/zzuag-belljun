package com.zzaug.api.domain.member.service.history;

import com.zzaug.api.domain.member.model.auth.EmailAuthResult;
import com.zzaug.api.domain.member.model.auth.TryCountElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculateTryCountService {

	public TryCountElement execute(EmailAuthResult result, TryCountElement tryCount) {
		int originValue = tryCount.getTryCount();
		if (!result.isSuccess()) {
			tryCount.plus();
		}
		log.debug("tryCount : {} -> {}", originValue, tryCount.getTryCount());
		return tryCount;
	}
}
