package com.zzaug.api.domain.support.entity;

import com.zzaug.api.domain.entity.member.ContactType;
import com.zzaug.api.domain.entity.member.ExternalContactEntity;
import com.zzaug.api.domain.model.member.MemberContacts;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;

@UtilityClass
public class MemberContactExtractor {

	public static MemberContacts execute(List<ExternalContactEntity> contacts) {
		String email = Strings.EMPTY;
		String github = Strings.EMPTY;
		for (ExternalContactEntity contact : contacts) {
			if (contact.getContactType().equals(ContactType.EMAIL)) {
				email = contact.getSource();
				continue;
			}
			if (contact.getContactType().equals(ContactType.GITHUB)) {
				github = contact.getSource();
			}
		}
		return MemberContacts.builder().email(email).github(github).build();
	}
}
