package org.cloudlet.web.security.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.place.shared.PlaceHistoryHandler.DefaultHistorian;
import com.google.inject.Inject;

public class SecurityHistorian extends DefaultHistorian {

  private final Subject subject;

  @Inject
  SecurityHistorian(final Subject subject) {
    this.subject = subject;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(
      final ValueChangeHandler<String> valueChangeHandler) {
    return super.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(final ValueChangeEvent<String> event) {
        String value = event.getValue();
        filter(value);
        valueChangeHandler.onValueChange(event);
      }
    });
  }

  @Override
  public void newItem(final String token, final boolean issueEvent) {
    filter(token);
    super.newItem(token, issueEvent);
  }

  private String filter(final String value) {
    subject.checkPermission(value);
    return value;
  }
}
