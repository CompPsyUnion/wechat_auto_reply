package cn.yiming1234.wechat_auto_reply.utils;

public class SleepUtils {

    /**
     * 毫秒为单位
     */
    public static void sleep( long time ){
        try {
            Thread.sleep( time );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
