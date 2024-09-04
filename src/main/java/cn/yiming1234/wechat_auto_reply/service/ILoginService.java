package cn.yiming1234.wechat_auto_reply.service;

/**
 * 登陆服务接口
 */
public interface ILoginService {

	/**
	 * 登陆
	 */
	boolean login();

	/**
	 * 获取UUID
	 */
	String getUuid();

	/**
	 * 获取二维码图片
	 */
	boolean getQR(String qrPath);

	/**
	 * web初始化
	 */
	boolean webWxInit();

	/**
	 * 微信状态通知
	 */
	void wxStatusNotify();

	/**
	 * 接收消息
	 */
	void startReceiving();

	/**
	 * 获取微信联系人
	 */
	void webWxGetContact();

	/**
	 * 批量获取联系人信息
	 */
	void WebWxBatchGetContact();

}
