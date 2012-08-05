package com.goodow.web.core.client;

import com.goodow.web.core.shared.CorePackage;
import com.goodow.web.core.shared.Resource;
import com.goodow.web.core.shared.ResourceUploadedEvent;
import com.goodow.web.core.shared.ResourceUploadedHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import java.util.logging.Logger;

public class ResourceField extends FormField<Resource> {

  Logger logger = Logger.getLogger(ResourceField.class.getName());

  @Inject
  FileUpload fileUpload;

  @Inject
  FormPanel formPanel;

  @Inject
  SimplePanel editorPanel;

  @Inject
  ClientJSONMarshaller marshaller;

  @Inject
  UIRegistry registry;

  Resource resource;

  /** {@inheritDoc} */
  public HandlerRegistration addResourceUploadedHandler(final ResourceUploadedHandler handler) {
    return addHandler(handler, ResourceUploadedEvent.TYPE);
  }

  @Override
  public Resource getValue() {
    return resource;
  }

  @Override
  public void setValue(final Resource value) {
    this.resource = value;
    if (resource != null && registry.showWidget(editorPanel, resource)) {
      main.add(editorPanel);
    } else {
      main.add(formPanel);
    }
  }

  @Override
  protected void start() {
    super.start();
    fileUpload.setName("attachment");
    formPanel.add(fileUpload);

    formPanel.setMethod("POST");
    formPanel.setEncoding("multipart/form-data");
    formPanel.setAction(GWT.getModuleBaseURL() + "resources");
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
        int begin = responseText.indexOf(">");
        int end = responseText.lastIndexOf("<");
        responseText = responseText.substring(begin + 1, end);
        logger.info(responseText);
        JSONValue json = JSONParser.parse(responseText);
        Resource resource =
            (Resource) marshaller.parse(CorePackage.Resource.as(), json, new ClientMessage());
        setValue(resource);
        message.setText("Uploaded");
        fireEvent(new ResourceUploadedEvent(resource));
      }
    });

  }
}
