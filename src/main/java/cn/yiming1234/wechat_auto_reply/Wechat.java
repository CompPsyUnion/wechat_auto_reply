package cn.yiming1234.wechat_auto_reply;

import lombok.extern.slf4j.Slf4j;

import cn.yiming1234.wechat_auto_reply.controller.LoginController;
import cn.yiming1234.wechat_auto_reply.core.MsgCenter;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;

@Slf4j
public class Wechat {

	private IMsgHandlerFace msgHandler;

	public Wechat(IMsgHandlerFace msgHandler, String qrPath) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;
		// 登录
		LoginController login = new LoginController();
		login.login(qrPath);
	}

	public void start() {
		log.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
		new Thread(new Runnable() {
			@Override
			public void run() {
				MsgCenter.handleMsg(msgHandler);
			}
		}).start();
	}

}
