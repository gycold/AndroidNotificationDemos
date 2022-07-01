package com.easy.notification;

import android.app.Application;

/**
 * package: com.easy.notification.App
 * author: gyc
 * description:
 * time: create at 2022/7/1 11:03
 */
public class App extends Application {

  private static App instance;

  public static App getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }
}