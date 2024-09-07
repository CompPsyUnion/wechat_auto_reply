package cn.yiming1234.wechat_auto_reply.beans;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "yiming1234.wechat")
@Data
public class WechatGroupConfig {
    private List<String> groupNames;
    private List<String> usernames;
}
