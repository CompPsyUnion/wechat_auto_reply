package cn.yiming1234.wechat_auto_reply.controller;

import lombok.extern.slf4j.Slf4j;

import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.core.Core;
import cn.yiming1234.wechat_auto_reply.service.ILoginService;
import cn.yiming1234.wechat_auto_reply.service.impl.LoginServiceImpl;
import cn.yiming1234.wechat_auto_reply.thread.CheckLoginStatusThread;
import cn.yiming1234.wechat_auto_reply.utils.SleepUtils;
import cn.yiming1234.wechat_auto_reply.utils.tools.CommonTools;

/**
 * 登录控制器
 */
@Slf4j
public class LoginController {
	private ILoginService loginService = new LoginServiceImpl();
	private static Core core = Core.getInstance();

	public void login(String qrPath) {
		if (core.isAlive()) { // 已登录
			log.info("wechat_auto_reply已登录");
			return;
		}
		while (true) {
			for (int count = 0; count <= 10; count++) {
				log.info("获取UUID");
				while (loginService.getUuid() == null) {
					log.info("1. 获取微信UUID");
					while (loginService.getUuid() == null) {
						log.warn("1.1. 获取微信UUID失败，两秒后重新获取");
						SleepUtils.sleep(2000);
					}
				}
				log.info("2. 获取登录二维码图片");
				if (loginService.getQR(qrPath)) {
					break;
				} else if (count == 10) {
					log.error("2.2. 获取登录二维码图片失败，系统退出");
					System.exit(0);
				}
			}
			log.info("3. 请扫描二维码图片，并在手机上确认");
			if (!core.isAlive()) {
				loginService.login();
				core.setAlive(true);
				log.info(("登录成功"));
				break;
			}
			log.info("4. 登录超时，请重新扫描二维码图片");
		}

		log.info("5. 登录成功，微信初始化");
		if (!loginService.webWxInit()) {
			log.info("6. 微信初始化异常");
			System.exit(0);
		}

		log.info("6. 开启微信状态通知");
		loginService.wxStatusNotify();

		log.info("7. 清除。。。。");
		CommonTools.clearScreen();
		log.info(String.format("欢迎回来， %s", core.getNickName()));

		log.info("8. 开始接收消息");
		loginService.startReceiving();

		log.info("9. 获取联系人信息");
		loginService.webWxGetContact();

		log.info("10. 获取群好友及群好友列表");
		loginService.WebWxBatchGetContact();

		log.info("11. 缓存本次登录好友相关消息");
		WechatTools.setUserInfo(); // 登录成功后缓存本次登录好友相关消息（NickName, UserName）

		log.info("12.开启微信状态检测线程");
		new Thread(new CheckLoginStatusThread()).start();
	}
}
