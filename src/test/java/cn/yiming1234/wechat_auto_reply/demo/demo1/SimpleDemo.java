package cn.yiming1234.wechat_auto_reply.demo.demo1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import cn.yiming1234.wechat_auto_reply.api.MessageTools;
import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.beans.BaseMsg;
import cn.yiming1234.wechat_auto_reply.beans.RecommendInfo;
import cn.yiming1234.wechat_auto_reply.core.Core;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import cn.yiming1234.wechat_auto_reply.utils.enums.MsgTypeEnum;
import cn.yiming1234.wechat_auto_reply.utils.tools.DownloadTools;

/**
 * 简单示例程序，收到文本信息自动回复原信息，收到图片、语音、小视频后根据路径自动保存
 */
@Slf4j
public class SimpleDemo implements IMsgHandlerFace {

	/**
	 * 消息处理
	 */
	@Override
	public String textMsgHandle(BaseMsg msg) {
		// String docFilePath = "D:/wechat_auto_reply/pic/1.jpg"; // 这里是需要发送的文件的路径
		if (!msg.isGroupMsg()) { // 群消息不处理
			// String userId = msg.getString("FromUserName");
			// MessageTools.sendFileMsgByUserId(userId, docFilePath); // 发送文件
			// MessageTools.sendPicMsgByUserId(userId, docFilePath);
			String text = msg.getText(); // 发送文本消息，也可调用MessageTools.sendFileMsgByUserId(userId,text);
			log.info(text);
			if (text.equals("111")) {
				WechatTools.logout();
			}
			if (text.equals("222")) {
				WechatTools.remarkNameByNickName("yaphone", "Hello");
			}
			if (text.equals("333")) { // 测试群列表
				System.out.print(WechatTools.getGroupNickNameList());
				System.out.print(WechatTools.getGroupIdList());
				System.out.print(Core.getInstance().getGroupMemeberMap());
			}
			return text;
		}
		return null;
	}

	/**
	 * 处理图片消息
	 */
	@Override
	public String picMsgHandle(BaseMsg msg) {
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
		String picPath = "D://wechat_auto_reply/pic" + File.separator + fileName + ".jpg"; // 调用此方法来保存图片
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath); // 保存图片的路径
		return "图片保存成功";
	}

	/**
	 * 处理声音消息
	 */
	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String voicePath = "D://wechat_auto_reply/voice" + File.separator + fileName + ".mp3";
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
		return "声音保存成功";
	}

	/**
	 * 处理小视频消息
	 */
	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
		String viedoPath = "D://wechat_auto_reply/viedo" + File.separator + fileName + ".mp4";
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
		return "视频保存成功";
	}

	/**
	 * 处理名片消息
	 */
	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		return "收到名片消息";
	}

	/**
	 * 处理系统消息
	 */
	@Override
	public void sysMsgHandle(BaseMsg msg) { // 收到系统消息
		String text = msg.getContent();
		log.info(text);
	}

	/**
	 * 收到请求添加好友消息
	 */
	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		MessageTools.addFriend(msg, true); // 同意好友请求，false为不接受好友请求
		RecommendInfo recommendInfo = msg.getRecommendInfo();
		String nickName = recommendInfo.getNickName();
		String province = recommendInfo.getProvince();
		String city = recommendInfo.getCity();
		String text = "你好，来自" + province + city + "的" + nickName + "， 欢迎添加我为好友！";
		return text;
	}

	/**
	 * 收到请求添加群消息
	 */
	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		String fileName = msg.getFileName();
		String filePath = "D://wechat_auto_reply/file" + File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
		DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(), filePath);
		return "文件" + fileName + "保存成功";
	}

}
