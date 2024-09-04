package cn.yiming1234.wechat_auto_reply.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.core.Core;
import cn.yiming1234.wechat_auto_reply.service.ILoginService;
import cn.yiming1234.wechat_auto_reply.service.impl.LoginServiceImpl;
import cn.yiming1234.wechat_auto_reply.thread.CheckLoginStatusThread;
import cn.yiming1234.wechat_auto_reply.utils.SleepUtils;
import cn.yiming1234.wechat_auto_reply.utils.tools.CommonTools;

/**
 * 登陆控制器
 */
public class LoginController {
	private static Logger LOG = LoggerFactory.getLogger(LoginController.class);
	private ILoginService loginService = new LoginServiceImpl();
	private static Core core = Core.getInstance();

	public void login(String qrPath) {
		if (core.isAlive()) { // 已登陆
			LOG.info("wechat_auto_reply已登陆");
			return;
		}
		while (true) {
			for (int count = 0; count <= 10; count++) {
				LOG.info("获取UUID");
				while (loginService.getUuid() == null) {
					LOG.info("1. 获取微信UUID");
					while (loginService.getUuid() == null) {
						LOG.warn("1.1. 获取微信UUID失败，两秒后重新获取");
						SleepUtils.sleep(2000);
					}
				}
				LOG.info("2. 获取登陆二维码图片");
				if (loginService.getQR(qrPath)) {
					break;
				} else if (count == 10) {
					LOG.error("2.2. 获取登陆二维码图片失败，系统退出");
					System.exit(0);
				}
			}
			LOG.info("3. 请扫描二维码图片，并在手机上确认");
			if (!core.isAlive()) {
				loginService.login();
				core.setAlive(true);
				LOG.info(("登陆成功"));
				break;
			}
			LOG.info("4. 登陆超时，请重新扫描二维码图片");
		}

		LOG.info("5. 登陆成功，微信初始化");
		if (!loginService.webWxInit()) {
			LOG.info("6. 微信初始化异常");
			System.exit(0);
		}

		LOG.info("6. 开启微信状态通知");
		loginService.wxStatusNotify();

		LOG.info("7. 清除。。。。");
		CommonTools.clearScreen();
		LOG.info(String.format("欢迎回来， %s", core.getNickName()));

		LOG.info("8. 开始接收消息");
		loginService.startReceiving();

		LOG.info("9. 获取联系人信息");
		loginService.webWxGetContact();

		LOG.info("10. 获取群好友及群好友列表");
		loginService.WebWxBatchGetContact();

		LOG.info("11. 缓存本次登陆好友相关消息");
		WechatTools.setUserInfo(); // 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）

		LOG.info("12.开启微信状态检测线程");
		new Thread(new CheckLoginStatusThread()).start();
	}
}
