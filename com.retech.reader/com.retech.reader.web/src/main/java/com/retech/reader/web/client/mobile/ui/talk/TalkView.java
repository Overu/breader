package com.retech.reader.web.client.mobile.ui.talk;

import com.goodow.wave.client.wavepanel.WavePanel;
import com.goodow.web.mvp.shared.tree.rpc.BaseReceiver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;

import com.retech.reader.web.shared.rpc.ReaderFactory;


import java.util.logging.Logger;

public class TalkView extends Composite {

  interface TalkViewUiBinder extends UiBinder<Widget, TalkView> {
  }

  @UiField
  RichTextArea content;

  @UiField
  Button send;
  private static final Logger logger = Logger.getLogger(TalkView.class.getName());
  private static TalkViewUiBinder uiBinder = GWT.create(TalkViewUiBinder.class);

  private final ReaderFactory f;

  @Inject
  TalkView(final ReaderFactory f, final WavePanel wavePanel) {
    this.f = f;
    // initWidget(uiBinder.createAndBindUi(this));
    wavePanel.setWaveContent(uiBinder.createAndBindUi(this));
    initWidget(wavePanel);
    wavePanel.getWaveTitle().setText("分享");
  }

  @UiHandler("send")
  void handlerSendClick(final ClickEvent e) {
    BaseReceiver<Void> receiver = new BaseReceiver<Void>() {

      @Override
      public void onSuccessAndCached(final Void response) {
        logger.info("发送成功");
      }

      @Override
      public Request<Void> provideRequest() {
        return f.talkContext().send(content.getHTML());
      }
    };
    receiver.fire();
  }
}
