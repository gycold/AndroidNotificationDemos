package com.easy.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

/**
 * package: com.easy.notification.NotificationActivity
 * author: gyc
 * description:
 * time: create at 2022/7/1 8:56
 */
public class NotificationActivity extends AppCompatActivity {

    private final String mNormalChannelId = "渠道id";// 唯一性
    private final String mNormalChannelName = "渠道名称";

    private final String mHighChannelId = "重要渠道id";
    private final String mHighChannelName = "重要通知";

    private final String mProgressChannelId = "进度条渠道id";
    private final String mProgressChannelName = "进度条通知";

    private final String mBigTextChannelId = "大文本渠道id";
    private final String mBigTextChannelName = "大文本通知";

    private final String mBigImageChannelId = "大图片渠道id";
    private final String mBigImageChannelName = "大图片通知";

    private final String mCustomChannelId = "自定义渠道id";
    private final String mCustomChannelName = "自定义通知";

    private NotificationReceiver mReceiver;
    private NotificationCompat.Builder mBuilder;
    private static NotificationManager mManager;

    /*------------------*/

    private static final int mNormalNotificationId = 9001; // 通知id
    private static final int mHighNotificationId = 9002; // 通知id
    private static final int mBigTextNotificationId = 9003; // 通知id
    private static final int mProgressNotificationId = 9004; // 通知id
    private static final int mBigImageNotificationId = 9005; // 通知id
    private static final int mCustomNotificationId = 9006; // 通知id
    private static final String mStopAction = "com.easy.stop"; // 暂停继续action
    private static final String mDoneAction = "com.easy.done"; // 完成action
    private static int mFlag = 0;
    private static boolean mIsStop = false; // 是否在播放 默认未开始

    MaterialToolbar mToolbar;
    MaterialButton mb_normal;
    MaterialButton mb_high;
    MaterialButton mb_progress;
    MaterialButton mb_update_progress;
    MaterialButton mb_big_text;
    MaterialButton mb_big_image;
    MaterialButton mb_custom;
    MaterialButton mb_update_custom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mToolbar = findViewById(R.id.toolbar);
        mb_normal = findViewById(R.id.mb_normal);
        mb_high = findViewById(R.id.mb_high);
        mb_progress = findViewById(R.id.mb_progress);
        mb_update_progress = findViewById(R.id.mb_update_progress);
        mb_big_text = findViewById(R.id.mb_big_text);
        mb_big_image = findViewById(R.id.mb_big_image);
        mb_custom = findViewById(R.id.mb_custom);
        mb_update_custom = findViewById(R.id.mb_update_custom);

        mToolbar.setTitle(R.string.notification);

        mManager = (NotificationManager) NotificationActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        //toolbar上back的事件处理
        mToolbar.setNavigationOnClickListener(view -> finish());

        //toolbar上menu的事件处理
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_author:
                        Snackbar.make(mToolbar.getRootView(), "作者：EasyAndroid", Snackbar.LENGTH_LONG)
                                .setAction("记住了", view -> Toast.makeText(NotificationActivity.this, "这是一条toast！", Toast.LENGTH_LONG).show())
                                .show();

                        break;
                    case R.id.menu_share:
                        Toast.makeText(NotificationActivity.this, "分享", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_settings:
                        Toast.makeText(NotificationActivity.this, "设置", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        createReceiver();

        setClickListener();
    }

    /**
     * 创建广播接收器
     */
    private void createReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        // 添加接收事件监听
        intentFilter.addAction(mStopAction);
        intentFilter.addAction(mDoneAction);
        mReceiver = new NotificationReceiver();
        // 注册广播
        registerReceiver(mReceiver, intentFilter);
    }

    private void setClickListener() {
        mb_normal.setOnClickListener(view -> {
            createNotificationForNormal();
        });

        mb_high.setOnClickListener(view -> {
            createNotificationForHigh();
        });

        mb_progress.setOnClickListener(view -> {
            createNotificationForProgress();
        });

        mb_update_progress.setOnClickListener(view -> {
            updateNotificationForProgress();
        });

        mb_big_text.setOnClickListener(view -> {
            createNotificationForBigText();
        });

        mb_big_image.setOnClickListener(view -> {
            createNotificationForBigImage();
        });

        mb_custom.setOnClickListener(view -> {
            createNotificationForCustom();
        });

        mb_update_custom.setOnClickListener(view -> {
            updateNotificationForCustom();
        });
    }

    /**
     * 普通通知
     */
    private void createNotificationForNormal() {
        // 适配8.0及以上 创建渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mNormalChannelId, mNormalChannelName, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("描述");
            channel.setShowBadge(false);// 是否在桌面显示角标
            mManager.createNotificationChannel(channel);
        }
        // 点击意图 // setDeleteIntent 移除意图
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        // 构建配置
        mBuilder = new NotificationCompat.Builder(this, mNormalChannelId)
                .setContentTitle("普通通知") // 标题
                .setContentText("普通通知内容") // 文本
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar)) // 大图标
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 7.0 设置优先级
                .setContentIntent(pendingIntent) // 跳转配置
                .setAutoCancel(true); // 是否自动消失（点击）or mManager.cancel(mNormalNotificationId)、cancelAll、setTimeoutAfter()
        // 发起通知
        mManager.notify(mNormalNotificationId, mBuilder.build());
    }

    /**
     * 重要通知
     */
    private void createNotificationForHigh() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mHighChannelId, mHighChannelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            mManager.createNotificationChannel(channel);
        }
        mBuilder = new NotificationCompat.Builder(this, mHighChannelId)
                .setContentTitle("重要通知")
                .setContentText("重要通知内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar))
                .setAutoCancel(true)
                .setNumber(999) // 自定义桌面通知数量
                .addAction(R.mipmap.ic_avatar, "去看看", pendingIntent)// 通知上的操作
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE); // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
        mManager.notify(mHighNotificationId, mBuilder.build());
    }

    /**
     * 进度条通知
     */
    private void createNotificationForProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mProgressChannelId, mProgressChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            mManager.createNotificationChannel(channel);
        }
        int progressMax = 100;
        int progressCurrent = 30;
        mBuilder = new NotificationCompat.Builder(this, mProgressChannelId)
                .setContentTitle("进度通知")
                .setContentText("下载中：" + progressCurrent + "%")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar))
                // 第3个参数indeterminate，false表示确定的进度，比如100，true表示不确定的进度，会一直显示进度动画，直到更新状态下载完成，或删除通知
                .setProgress(progressMax, progressCurrent, false);

        mManager.notify(mProgressNotificationId, mBuilder.build());
    }

    /**
     * 更新进度条通知
     * 1.更新进度：修改进度值即可
     * 2.下载完成：总进度与当前进度都设置为0即可，同时更新文案
     */
    private void updateNotificationForProgress() {
        if (mBuilder != null) {
            int progressMax = 100;
            int progressCurrent = 50;
            // 1.更新进度
            mBuilder.setContentText("下载中：" + progressCurrent + "%").setProgress(progressMax, progressCurrent, false);
            // 2.下载完成
            //mBuilder.setContentText("下载完成！").setProgress(0, 0, false)
            mManager.notify(mProgressNotificationId, mBuilder.build());
            Toast.makeText(this, "已更新进度到$progressCurrent%", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请先发一条进度条通知", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 大文本通知
     */
    private void createNotificationForBigText() {
        String bigText = "A notification is a message that Android displays outside your app's UI to provide the user with reminders, communication from other people, or other timely information from your app. Users can tap the notification to open your app or take an action directly from the notification.";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mBigTextChannelId, mBigTextChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            mManager.createNotificationChannel(channel);
        }
        mBuilder = new NotificationCompat.Builder(this, mBigTextChannelId)
                .setContentTitle("大文本通知")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar))
                .setAutoCancel(true);
        mManager.notify(mBigTextNotificationId, mBuilder.build());
    }

    /**
     * 大图片通知
     */
    private void createNotificationForBigImage() {
        Bitmap bigPic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_big_pic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mBigImageChannelId, mBigImageChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            mManager.createNotificationChannel(channel);
        }
        mBuilder = new NotificationCompat.Builder(this, mBigImageChannelId)
                .setContentTitle("大图片通知")
                .setContentText("有美女，展开看看")
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPic))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar))
                .setAutoCancel(true);
        mManager.notify(mBigImageNotificationId, mBuilder.build());
    }

    /**
     * 自定义通知
     */
    private void createNotificationForCustom() {
        // 适配8.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mCustomChannelId, mCustomChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            mManager.createNotificationChannel(channel);
        }

        // 适配12.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mFlag = PendingIntent.FLAG_IMMUTABLE;
        } else {
            mFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        }

        // 添加自定义通知view
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.layout_notification);

        // 添加暂停继续事件
        Intent intentStop = new Intent(mStopAction);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 0, intentStop, mFlag);
        views.setOnClickPendingIntent(R.id.btn_stop, pendingIntentStop);

        // 添加完成事件
        Intent intentDone = new Intent(mDoneAction);
        PendingIntent pendingIntentDone = PendingIntent.getBroadcast(this, 0, intentDone, mFlag);
        views.setOnClickPendingIntent(R.id.btn_done, pendingIntentDone);

        // 创建Builder
        mBuilder = new NotificationCompat.Builder(this, mCustomChannelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar))
                .setAutoCancel(true)
                .setCustomContentView(views)
                .setCustomBigContentView(views);// 设置自定义通知view

        // 发起通知
        mManager.notify(mCustomNotificationId, mBuilder.build());
    }


    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拦截接收事件
            if (mStopAction.equals(intent.getAction())) {
                // 改变状态
                mIsStop = !mIsStop;
                if (context instanceof NotificationActivity) {
                    ((NotificationActivity) context).updateCustomView();
                }
            } else if (mDoneAction.equals(intent.getAction())) {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新自定义通知View
     */
    private void updateCustomView() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.layout_notification);
        Intent intentUpdate = new Intent(mStopAction);
        PendingIntent pendingIntentUpdate = PendingIntent.getBroadcast(this, 0, intentUpdate, mFlag);
        views.setOnClickPendingIntent(R.id.btn_stop, pendingIntentUpdate);
        // 根据状态更新UI
        if (mIsStop) {
            views.setTextViewText(R.id.tv_status, "童年儿歌 - 停止播放");

            views.setTextViewText(R.id.btn_stop, "继续");
            mb_update_custom.setText("继续");
        } else {
            views.setTextViewText(R.id.tv_status, "童年儿歌 - 正在播放");
            views.setTextViewText(R.id.btn_stop, "暂停");
            mb_update_custom.setText("暂停");
        }

        mBuilder.setCustomContentView(views).setCustomBigContentView(views);
        // 重新发起通知更新UI，注意：必须得是同一个通知id，即mCustomNotificationId
        mManager.notify(mCustomNotificationId, mBuilder.build());
    }

    /**
     * 更新自定义通知
     */
    private void updateNotificationForCustom() {
        // 发送通知 更新状态及UI
        sendBroadcast(new Intent(mStopAction));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}