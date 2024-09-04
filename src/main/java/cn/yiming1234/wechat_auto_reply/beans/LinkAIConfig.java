package cn.yiming1234.wechat_auto_reply.beans;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yiming1234.linkai")
@Data
public class LinkAIConfig {
    private String apiKey;
    private String appCode;
}
