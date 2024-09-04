package cn.yiming1234.wechat_auto_reply;

import cn.yiming1234.wechat_auto_reply.method.LinkAI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LinkAIApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LinkAIApplication.class, args);
        LinkAI linkAI = context.getBean(LinkAI.class);
        Wechat wechat = new Wechat(linkAI, "D://wechat_auto_reply/login");
        wechat.start();
    }
}