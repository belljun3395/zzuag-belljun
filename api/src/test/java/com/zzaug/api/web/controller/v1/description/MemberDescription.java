package com.zzaug.api.web.controller.v1.description;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class MemberDescription {

	public static FieldDescriptor[] updateMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("어세스 토큰"),
		};
	}

	public static FieldDescriptor[] loginMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("어세스 토큰"),
		};
	}

	public static FieldDescriptor[] readMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("멤버 아이디"),
			fieldWithPath("data.certification").type(JsonFieldType.STRING).description("멤버 증명(아이디)"),
			fieldWithPath("data.email").type(JsonFieldType.STRING).description("멤버 이메일"),
			fieldWithPath("data.github").type(JsonFieldType.STRING).description("멤버 깃허브"),
		};
	}

	public static FieldDescriptor[] searchMember() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("멤버 아이디"),
			fieldWithPath("data.certification").type(JsonFieldType.STRING).description("멤버 증명(아이디)"),
			fieldWithPath("data.email").type(JsonFieldType.STRING).description("멤버 이메일"),
			fieldWithPath("data.github").type(JsonFieldType.STRING).description("멤버 깃허브"),
		};
	}

	public static FieldDescriptor[] renewalToken() {
		return new FieldDescriptor[] {
			fieldWithPath("data").type(JsonFieldType.OBJECT).description("data"),
			fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("어세스 토큰"),
		};
	}
}
