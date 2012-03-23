package com.goodow.android.ioc;

import com.google.inject.Module;


import java.util.List;

import android.app.Instrumentation;
import roboguice.application.RoboApplication;

public class AndroidApplication extends RoboApplication {

  public AndroidApplication() {
    super();
  }

  public AndroidApplication(final Instrumentation instrumentation) {
    super();
    attachBaseContext(instrumentation.getTargetContext());
  }

  @Override
  protected void addApplicationModules(final List<Module> modules) {
    modules.add(new CommonModule());
  }

}
