package cn.yiming1234.wechat_auto_reply.utils.enums;

/**
 * 确认添加好友Enum
 */
public enum VerifyFriendEnum {

	ADD(2, "添加"),
	ACCEPT(3, "接受");

	private int code;
	private String desc;

	private VerifyFriendEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

}
