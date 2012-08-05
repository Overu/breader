package com.goodow.web.core.client;

import static com.google.gwt.user.client.rpc.RpcRequestBuilder.STRONG_NAME_HEADER;

import com.goodow.web.core.client.text.RichTextToolbar;
import com.goodow.web.core.shared.AsyncResourceService;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.TakesValueEditor;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TextResourceEditor extends FlowView implements TakesValue<Resource>,
    IsEditor<TakesValueEditor<Resource>>, RequestCallback {

  protected TakesValueEditor<Resource> editor;

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Inject
  private RichTextArea textArea;

  private RichTextToolbar toolbar;

  private Resource resource;

  @Inject
  AsyncResourceService resourceSerivce;

  @Override
  public TakesValueEditor<Resource> asEditor() {
    if (editor == null) {
      editor = TakesValueEditor.of(this);
    }
    return editor;
  }

  public String getHTML() {
    return textArea.getHTML();
  }

  @Override
  public Resource getValue() {
    return resource;
  }

  public boolean isChanged() {
    return !textArea.getHTML().equals(resource.getTextContent());
  }

  @Override
  public void onError(final Request request, final Throwable exception) {
  }

  @Override
  public void onResponseReceived(final Request request, final Response response) {
    String textContent = response.getText();
    resource.setTextContent(textContent);
    textArea.setHTML(textContent);
  }

  @Override
  public void setValue(final Resource value) {
    this.resource = value;
    if (resource.getTextContent() != null) {
      textArea.setHTML(resource.getTextContent());
    } else {
      String id = resource.getId();
      if (id != null) {
        String contentUrl = GWT.getModuleBaseURL() + "resources/" + id;
        try {
          RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, contentUrl);
          configureRequestBuilder(builder);
          builder.setCallback(this);
          logger.finest("Sending fire request");
          builder.send();
        } catch (RequestException e) {
          logger.log(Level.SEVERE, "Server Error (" + e.getMessage() + ")", e);
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Serialization Error (" + e.getMessage() + ")", e);
        }
      }
    }
  }

  protected void configureRequestBuilder(final RequestBuilder builder) {
    builder.setHeader("Content-Type", RequestFactory.JSON_CONTENT_TYPE_UTF8);
    builder.setHeader("pageurl", Location.getHref());
    builder.setHeader(STRONG_NAME_HEADER, GWT.getPermutationStrongName());
  }

  @Override
  protected void start() {
    toolbar = new RichTextToolbar(textArea);
    main.add(toolbar);
    main.add(textArea);

    Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

      @Override
      public boolean execute() {
        saveResource();
        return true;
      }
    }, 20000);
  }

  private void saveResource() {
    if (isChanged()) {
      resource.setTextContent(textArea.getHTML());
      resourceSerivce.save(resource).fire(new Receiver<Resource>() {
        @Override
        public void onSuccess(final Resource result) {
          logger.info("Saved");
        }
      });
    }
  }
}
