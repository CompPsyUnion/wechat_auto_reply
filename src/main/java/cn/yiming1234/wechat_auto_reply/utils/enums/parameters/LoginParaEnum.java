package cn.yiming1234.wechat_auto_reply.utils.enums.parameters;

/**
 * 登陆
 * <p>
 */
public enum LoginParaEnum {

    LOGIN_ICON("loginicon", "true"),
    UUID("uuid", ""),
    TIP("tip", "0"),
    R("r", ""),
    _("_", "");

    private String para;
    private String value;

    LoginParaEnum(String para, String value) {
        this.para = para;
        this.value = value;
    }

    public String para() {
        return para;
    }

    public String value() {
        return value;
    }
}
