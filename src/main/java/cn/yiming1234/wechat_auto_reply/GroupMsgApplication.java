package cn.yiming1234.wechat_auto_reply;

import cn.yiming1234.wechat_auto_reply.method.GroupMsg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GroupMsgApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GroupMsgApplication.class, args);
        GroupMsg groupMsg = context.getBean(GroupMsg.class);
        Wechat wechat = new Wechat(groupMsg, "D://wechat_auto_reply/login");
        wechat.start();
    }
}
