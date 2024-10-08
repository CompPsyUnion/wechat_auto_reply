package cn.yiming1234.wechat_auto_reply.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import cn.yiming1234.wechat_auto_reply.utils.enums.OsNameEnum;

/**
 * 配置信息
 */
public class Config {

	public static final String API_WXAPPID = "API_WXAPPID";

	public static final String picDir = "D://wechat_auto_reply";
	public static final String VERSION = "1.2.18";
	public static final String BASE_URL = "https://login.weixin.qq.com";
	public static final String OS = "";
	public static final String DIR = "";
	public static final String DEFAULT_QR = "QR.jpg";
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

	public static final ArrayList<String> API_SPECIAL_USER = new ArrayList<String>(Arrays.asList("filehelper", "weibo",
			"qqmail", "fmessage", "tmessage", "qmessage", "qqsync", "floatbottle", "lbsapp", "shakeapp", "medianote",
			"qqfriend", "readerapp", "blogapp", "facebookapp", "masssendapp", "meishiapp", "feedsapp", "voip",
			"blogappweixin", "brandsessionholder", "weixin", "weixinreminder", "officialaccounts", "wxitil",
			"notification_messages", "userexperience_alarm"));

	/**
	 * 获取文件目录
	 */
	public static String getLocalPath() {
		String localPath = null;
		try {
			localPath = new File("").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localPath;
	}

	/**
	 * 获取系统平台
	 */
	public static OsNameEnum getOsNameEnum() {
		String os = System.getProperty("os.name").toUpperCase();
		if (os.indexOf(OsNameEnum.DARWIN.toString()) >= 0) {
			return OsNameEnum.DARWIN;
		} else if (os.indexOf(OsNameEnum.WINDOWS.toString()) >= 0) {
			return OsNameEnum.WINDOWS;
		} else if (os.indexOf(OsNameEnum.LINUX.toString()) >= 0) {
			return OsNameEnum.LINUX;
		} else if (os.indexOf(OsNameEnum.MAC.toString()) >= 0) {
			return OsNameEnum.MAC;
		}
		return OsNameEnum.OTHER;
	}

}
