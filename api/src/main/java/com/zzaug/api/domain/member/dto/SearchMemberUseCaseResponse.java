package com.zzaug.api.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SearchMemberUseCaseResponse {

	private Long id;
	private String email;
	private String github;

	/** 검색 대상이 존재하지 않을 때 반환하는 응답 */
	public static SearchMemberUseCaseResponse notExistSearchTarget() {
		return SearchMemberUseCaseResponse.builder()
				.id(Long.MIN_VALUE)
				.email(Strings.EMPTY)
				.github(Strings.EMPTY)
				.build();
	}
}
