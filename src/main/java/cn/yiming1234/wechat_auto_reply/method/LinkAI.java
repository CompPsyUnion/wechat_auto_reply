package cn.yiming1234.wechat_auto_reply.method;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import cn.yiming1234.wechat_auto_reply.beans.LinkAIConfig;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.yiming1234.wechat_auto_reply.beans.BaseMsg;
import cn.yiming1234.wechat_auto_reply.core.Core;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import cn.yiming1234.wechat_auto_reply.utils.MyHttpClient;
import cn.yiming1234.wechat_auto_reply.utils.enums.MsgTypeEnum;
import cn.yiming1234.wechat_auto_reply.utils.tools.DownloadTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LinkAI implements IMsgHandlerFace {
    Logger logger = Logger.getLogger("LinkAI");
    MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();
    String url = "https://api.link-ai.tech/v1/chat/completions";

    @Autowired
    private LinkAIConfig linkAIConfig;

    @Override
    public String textMsgHandle(BaseMsg msg) {
        String result = "";
        String text = msg.getText();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("app_code", linkAIConfig.getAppCode());
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", text);
        messages.add(message);
        requestBody.put("messages", messages);
        String paramStr = JSON.toJSONString(requestBody);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + linkAIConfig.getApiKey());

        try {
            HttpEntity entity = myHttpClient.doPost(url, paramStr, headers);
            String response = EntityUtils.toString(entity, "UTF-8");
            JSONObject obj = JSON.parseObject(response);
            if (obj.containsKey("choices")) {
                JSONObject choice = obj.getJSONArray("choices").getJSONObject(0);
                result = choice.getJSONObject("message").getString("content");
            } else {
                result = "处理有误";
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        return "收到图片";
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        String fileName = String.valueOf(new Date().getTime());
        String voicePath = "D://wechat_auto_reply/voice" + File.separator + fileName + ".mp3";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        return "收到语音";
    }

    @Override
    public String viedoMsgHandle(BaseMsg msg) {
        String fileName = String.valueOf(new Date().getTime());
        String viedoPath = "D://wechat_auto_reply/viedo" + File.separator + fileName + ".mp4";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        return "收到视频";
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return "收到名片消息";
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) {
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        return null;
    }
}