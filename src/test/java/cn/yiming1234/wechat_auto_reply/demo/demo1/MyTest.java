package cn.yiming1234.wechat_auto_reply.demo.demo1;

import cn.yiming1234.wechat_auto_reply.Wechat;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;

import java.io.File;

public class MyTest {
	public static void main(String[] args) {
		String qrPath = "D://wechat_auto_reply//login"; // 保存登录二维码图片的路径，这里需要在本地新建目录
		File folder = new File(qrPath);
		if (!folder.exists()) {
			boolean success = folder.mkdirs();
			if (success) {
				System.out.println("文件夹创建成功");
			} else {
				System.out.println("文件夹创建失败");
			}
		} else {
			System.out.println("文件夹已存在");
		}
		IMsgHandlerFace msgHandler = new SimpleDemo(); // 实现IMsgHandlerFace接口的类
		Wechat wechat = new Wechat(msgHandler, qrPath); // 【注入】
		wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登录，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能需要重新打开图片
	}
}
