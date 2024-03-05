package com.zzaug.api.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberCheckRequestDescription {

	public static FieldDescriptor[] checkEmailAuthRequest() {
		return new FieldDescriptor[] {
			fieldWithPath("code").type(JsonFieldType.STRING).description("인증코드"),
			fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
			fieldWithPath("nonce").type(JsonFieldType.STRING).description("nonce")
		};
	}
}
