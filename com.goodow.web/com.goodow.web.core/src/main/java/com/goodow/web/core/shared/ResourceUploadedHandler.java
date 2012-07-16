package com.goodow.web.core.shared;

import com.google.gwt.event.shared.EventHandler;

public interface ResourceUploadedHandler extends EventHandler {
  void onResourceUpload(ResourceUploadedEvent event);
}