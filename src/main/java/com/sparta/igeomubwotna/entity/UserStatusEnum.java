package com.sparta.igeomubwotna.entity;

public enum UserStatusEnum {
	ACTIVE(Status.ACTIVE),  // 정상
	WITHDRAWN(Status.WITHDRAWN);  // 탈퇴

	private final String status;

	UserStatusEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public static class Status {
		public static final String ACTIVE = "STATUS_ACTIVE";
		public static final String WITHDRAWN = "STATUS_WITHDRAWN";
	}
}