package com.zzaug.api.domain.member.exception.state;

public class DuplicateCertificationException extends IllegalStateException {

	public DuplicateCertificationException() {
		super("Duplicate Certification");
	}
}
