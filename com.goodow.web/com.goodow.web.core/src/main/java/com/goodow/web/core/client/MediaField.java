package com.goodow.web.core.client;

import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.Media;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.inject.Inject;

import java.util.logging.Logger;

public class MediaField extends FormField<Media> {

  Logger logger = Logger.getLogger(MediaField.class.getName());

  @Inject
  FileUpload fileUpload;

  @Inject
  FormPanel formPanel;

  @Inject
  ClientJSONMarshaller marshaller;

  Media media;

  @Override
  public Media getValue() {
    return media;
  }

  @Override
  protected void start() {
    super.start();
    fileUpload.setName("attachment");
    formPanel.add(fileUpload);
    main.add(formPanel);
    formPanel.setMethod("POST");
    formPanel.setEncoding("multipart/form-data");
    formPanel.setAction(GWT.getModuleBaseURL() + "upload");
    fileUpload.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(final ChangeEvent event) {
        formPanel.submit();
        message.setText("Uploading...");
      }
    });
    formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
      @Override
      public void onSubmitComplete(final SubmitCompleteEvent event) {
        String responseText = event.getResults();
        logger.info(responseText);
        JSONValue json = JSONParser.parse(responseText);
        media = (Media) marshaller.parse(CorePackage.Media.as(), json, new ClientMessage());
      }
    });
  }
}
