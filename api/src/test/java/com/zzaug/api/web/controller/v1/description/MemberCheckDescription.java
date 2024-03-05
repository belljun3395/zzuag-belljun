package com.zzaug.api.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberCheckDescription {

	public static FieldDescriptor[] checkMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.duplication").type(JsonFieldType.BOOLEAN).description("요청 증명(아이디) 중복 여부"),
		};
	}

	public static FieldDescriptor[] emailAuth() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.duplication").type(JsonFieldType.BOOLEAN).description("요청 이메일 중복 여부"),
		};
	}

	public static FieldDescriptor[] checkEmailAuth() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.authentication").type(JsonFieldType.BOOLEAN).description("인증 번호 확인 결과"),
			fieldWithPath("data.tryCount").type(JsonFieldType.NUMBER).description("인증 번호 확인 시도 횟수"),
		};
	}
}
