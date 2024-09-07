package cn.yiming1234.wechat_auto_reply.method;

import cn.yiming1234.wechat_auto_reply.api.MessageTools;
import cn.yiming1234.wechat_auto_reply.api.WechatTools;
import cn.yiming1234.wechat_auto_reply.beans.BaseMsg;
import cn.yiming1234.wechat_auto_reply.beans.RecommendInfo;
import cn.yiming1234.wechat_auto_reply.beans.WechatGroupConfig;
import cn.yiming1234.wechat_auto_reply.face.IMsgHandlerFace;
import cn.yiming1234.wechat_auto_reply.utils.enums.MsgTypeEnum;
import cn.yiming1234.wechat_auto_reply.utils.tools.DownloadTools;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GroupMsg implements IMsgHandlerFace{

    @Autowired
    private WechatGroupConfig wechatGroupConfig;

    private Map<String, String> keywords;

    public GroupMsg() {
        loadKeywords();
    }

    /**
     * 加载关键词
     */
    private void loadKeywords() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("src/main/resources/keywords.json"));
            keywords = mapper.readValue(jsonData, HashMap.class);
        } catch (IOException e) {
            log.info("Failed to load keywords from JSON file", e);
            keywords = new HashMap<>();
        }
    }

    /**
     * 判断是否来自特定群
     */
    private boolean isFromSpecificGroup(BaseMsg msg) {
        if (wechatGroupConfig.getGroupNames() == null) {
            log.error("Group names configuration is missing");
            return false;
        }
        List<JSONObject> groupList = WechatTools.getGroupList();
        log.info("Configured group names: " + wechatGroupConfig.getGroupNames());
        boolean result = wechatGroupConfig.getGroupNames().stream()
                .anyMatch(groupName -> groupList.stream()
                        .anyMatch(group -> group.getString("UserName").equals(msg.getFromUserName()) && group.getString("NickName").equals(groupName)));
        log.info("isFromSpecificGroup result: " + result);
        return result;
    }

    /**
     * 判断是否被@了
     */
    private boolean isMentioned(BaseMsg msg) {
        if (wechatGroupConfig.getUsernames() == null) {
            log.error("Usernames configuration is missing");
            return false;
        }
        log.info("Configured usernames: " + wechatGroupConfig.getUsernames());
        boolean result = wechatGroupConfig.getUsernames().stream().anyMatch(username -> msg.getText().contains("@" + username));
        log.info("isMentioned result: " + result);
        return result;
    }

    /**
     * 处理文本消息
     */
    @Override
    public String textMsgHandle(BaseMsg msg) {
        if (msg.isGroupMsg() && isFromSpecificGroup(msg) && isMentioned(msg)) {
            String text = msg.getText();

            // 遍历所有配置的用户名，移除前缀
            for (String username : wechatGroupConfig.getUsernames()) {
                if (text.contains("@" + username)) {
                    text = text.replace("@" + username, "").trim(); // 去除@username，并去掉前后的空白
                    break;  // 一旦找到匹配的用户名，立即退出循环
                }
            }

            // 打印每个字符的Unicode编码，帮助确定不可见字符
            log.info("Original text with Unicode: ");
            for (char c : text.toCharArray()) {
                log.info(String.format("Character: '%c' (Unicode: \\u%04x)", c, (int) c));
            }

            // 移除所有不可见字符，包括常见和非常见的空白字符
            text = text.replaceAll("[\\p{Z}\\s\\u00A0\\u2007\\u202F\\u200B]+", "");  // 进一步覆盖更多类型的空白字符

            log.info("Cleaned text: " + text);
            log.info("Keywords: " + keywords.toString());

            if (keywords.containsKey(text)) {
                String reply = keywords.get(text);
                if (text.equals("退出登录")) {
                    WechatTools.logout();
                } else if (text.equals("备注")) {
                    WechatTools.remarkNameByNickName("自由的科学家", "Pleasurecruise");
                } else if (text.equals("返回群列表")) {
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
        log.info(text);
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
