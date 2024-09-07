package cn.yiming1234.wechat_auto_reply.demo.demo3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

import cn.yiming1234.wechat_auto_reply.Wechat;
import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.beans.BaseMsg;
import cn.yiming1234.wechat_auto_reply.core.Core;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import cn.yiming1234.wechat_auto_reply.utils.MyHttpClient;
import cn.yiming1234.wechat_auto_reply.utils.enums.StorageLoginInfoEnum;

/**
 * 此示例演示如何获取所有好友的头像
 */
@Slf4j
public class PicYourFriends implements IMsgHandlerFace {
	private static final Core core = Core.getInstance();
	private static final MyHttpClient myHttpClient = core.getMyHttpClient();
	private static final String path = "D://wechat_auto_reply//head"; // 这里需要设置保存头像的路径

	@Override
	public String textMsgHandle(BaseMsg msg) {

		if (!msg.isGroupMsg()) { // 群消息不处理
			String text = msg.getText(); // 发送文本消息，也可调用MessageTools.sendFileMsgByUserId(userId,text);
			String baseUrl = "https://" + core.getIndexUrl(); // 基础URL
			String skey = (String) core.getLoginInfo().get(StorageLoginInfoEnum.skey.getKey());
			if (text.equals("111")) {
				log.info("开始下载好友头像");
				List<JSONObject> friends = WechatTools.getContactList();
				for (int i = 0; i < friends.size(); i++) {
					JSONObject friend = friends.get(i);
					String url = baseUrl + friend.getString("HeadImgUrl") + skey;
					// String fileName = friend.getString("NickName");
					String headPicPath = path + File.separator + i + ".jpg";

					HttpEntity entity = myHttpClient.doGet(url, null, true, null);
					try {
						OutputStream out = new FileOutputStream(headPicPath);
						byte[] bytes = EntityUtils.toByteArray(entity);
						out.write(bytes);
						out.flush();
						out.close();

					} catch (Exception e) {
						log.info(e.getMessage());
					}

				}
			}
		}
		return null;
	}

	@Override
	public String picMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String voiceMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String viedoMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String nameCardMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sysMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		String qrPath = "D://wechat_auto_reply//login"; // 保存登录二维码图片的路径，这里需要在本地新建目录
		IMsgHandlerFace msgHandler = new PicYourFriends(); // 实现IMsgHandlerFace接口的类
		Wechat wechat = new Wechat(msgHandler, qrPath); // 【注入】
		wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登录，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
	}

	@Override
	public String verifyAddFriendMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mediaMsgHandle(BaseMsg msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
