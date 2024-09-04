package cn.yiming1234.wechat_auto_reply.method;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.yiming1234.wechat_auto_reply.api.MessageTools;
import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.beans.BaseMsg;
import cn.yiming1234.wechat_auto_reply.beans.RecommendInfo;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import cn.yiming1234.wechat_auto_reply.utils.enums.MsgTypeEnum;
import cn.yiming1234.wechat_auto_reply.utils.tools.DownloadTools;
import org.springframework.stereotype.Component;

@Component
public class Keyword implements IMsgHandlerFace {
    private static final Logger LOG = LoggerFactory.getLogger(Keyword.class);
    private Map<String, String> keywords;

    public Keyword() {
        loadKeywords();
    }

    private void loadKeywords() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/keywords.json"));
            keywords = mapper.readValue(jsonData, HashMap.class);
        } catch (IOException e) {
            LOG.error("Failed to load keywords from JSON file", e);
            keywords = new HashMap<>();
        }
    }

    /**
     * 处理文本消息
     */
    @Override
    public String textMsgHandle(BaseMsg msg) {
        if (!msg.isGroupMsg()) {
            String text = msg.getText();
            LOG.info(text);
            if (keywords.containsKey(text)) {
                String reply = keywords.get(text);
                if (text.equals("111")) {
                    WechatTools.logout();
                } else if (text.equals("222")) {
                    WechatTools.remarkNameByNickName("yaphone", "Hello");
                } else if (text.equals("333")) {
                    reply += WechatTools.getGroupNickNameList().toString();
                }
                return reply;
            }
            return "未找到匹配的关键词";
        }
        return null;
    }

    /**
     * 处理图片消息
     */
    @Override
    public String picMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String picPath = "D://wechat_auto_reply/pic" + File.separator + fileName + ".jpg";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath);
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
     * 处理视频消息
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
    public void sysMsgHandle(BaseMsg msg) {
        String text = msg.getContent();
        LOG.info(text);
    }

    /**
     * 处理确认添加好友消息
     */
    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        MessageTools.addFriend(msg, true);
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String nickName = recommendInfo.getNickName();
        String province = recommendInfo.getProvince();
        String city = recommendInfo.getCity();
        String text = "你好，来自" + province + city + "的" + nickName + "， 欢迎添加我为好友！";
        return text;
    }

    /**
     * 处理收到的文件消息
     */
    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        String fileName = msg.getFileName();
        String filePath = "D://wechat_auto_reply/file" + File.separator + fileName;
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getType(), filePath);
        return "文件" + fileName + "保存成功";
    }
}