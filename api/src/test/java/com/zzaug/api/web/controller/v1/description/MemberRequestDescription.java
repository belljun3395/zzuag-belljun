package com.zzaug.api.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberRequestDescription {

	public static FieldDescriptor[] memberSaveRequest() {
		return new FieldDescriptor[] {
			fieldWithPath("certification")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Certification은 영문과 숫자만 가능하며, 4자 미만, 16자 초과는 불가능합니다."),
			fieldWithPath("password")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Password는 영문, 숫자, 특수문자를 포함해야하며 8자 미만, 16자 초과는 불가능합니다.")
		};
	}

	public static FieldDescriptor[] memberUpdateRequest() {
		return new FieldDescriptor[] {
			fieldWithPath("certification")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Certification은 영문과 숫자만 가능하며, 4자 미만, 16자 초과는 불가능합니다."),
			fieldWithPath("password")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Password는 영문, 숫자, 특수문자를 포함해야하며 8자 미만, 16자 초과는 불가능합니다.")
		};
	}

	public static FieldDescriptor[] loginRequest() {
		return new FieldDescriptor[] {
			fieldWithPath("certification")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Certification은 영문과 숫자만 가능하며, 4자 미만, 16자 초과는 불가능합니다."),
			fieldWithPath("password")
					.type(JsonFieldType.STRING)
					.description("회원가입시 Password는 영문, 숫자, 특수문자를 포함해야하며 8자 미만, 16자 초과는 불가능합니다.")
		};
	}
}
