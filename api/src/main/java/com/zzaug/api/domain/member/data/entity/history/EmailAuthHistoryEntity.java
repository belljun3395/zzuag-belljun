package com.zzaug.api.domain.member.data.entity.history;

import com.zzaug.api.domain.member.data.entity.MemberAndEmailAuthFKBaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "email_auth_history")
@SQLDelete(sql = "UPDATE email_auth_history SET deleted=true where id=?")
public class EmailAuthHistoryEntity extends MemberAndEmailAuthFKBaseEntity {

	@Builder.Default
	@Column(name = "reason", nullable = false)
	private String reason = "SUCCESS";

	@Builder.Default
	@Column(name = "try_cnt", nullable = false)
	private Long tryCount = 0L;
}
