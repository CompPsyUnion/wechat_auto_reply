package cn.yiming1234.wechat_auto_reply;

import cn.yiming1234.wechat_auto_reply.method.Keyword;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KeywordApplication {

    public static void main(String[] args) {
//        String qrPath = "D://wechat_auto_reply//login"; // 保存登录二维码图片的路径，这里需要在本地新建目录
//        File folder = new File(qrPath);
//        if (!folder.exists()) {
//            boolean success = folder.mkdirs();
//            if (success) {
//                System.out.println("文件夹创建成功");
//            } else {
//                System.out.println("文件夹创建失败");
//            }
//        } else {
//            System.out.println("文件夹已存在");
//        }
        ApplicationContext context = SpringApplication.run(KeywordApplication.class, args);
        IMsgHandlerFace msgHandler = context.getBean(Keyword.class);
        Wechat wechat = new Wechat(msgHandler, "D://wechat_auto_reply//login");
        wechat.start();
    }
}
