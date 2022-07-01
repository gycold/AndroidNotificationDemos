package com.easy.notification;


public class C {
    //Notification相关
    public static class ChannelAbout {
        public static final String CHANNEL_NAME = "channel_name_" + App.getInstance().getPackageName();
        public static final String CHANNEL_DESCRIPTION = "channel_description_" + App.getInstance().getPackageName();
        public static final String CONTENT_TITLE = "运行中";
        public static final String CHANNEL_ID = "channel_id_" + App.getInstance().getPackageName();
        public static final int NOTIFICATION_ID = 56666;
    }
}
