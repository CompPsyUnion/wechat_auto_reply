package cn.yiming1234.wechat_auto_reply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.yiming1234.wechat_auto_reply.controller.LoginController;
import cn.yiming1234.wechat_auto_reply.core.MsgCenter;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;

public class Wechat {
	private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
	private IMsgHandlerFace msgHandler;

	public Wechat(IMsgHandlerFace msgHandler, String qrPath) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;

		// 登录
		LoginController login = new LoginController();
		login.login(qrPath);
	}

	public void start() {
		LOG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
		new Thread(new Runnable() {
			@Override
			public void run() {
				MsgCenter.handleMsg(msgHandler);
			}
		}).start();
	}

}
