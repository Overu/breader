package com.goodow.web.reader.client;

import com.goodow.web.core.client.FormField;
import com.goodow.web.core.client.ResourceField;
import com.goodow.web.core.client.TextField;
import com.goodow.web.core.shared.AsyncResourceService;
import com.goodow.web.core.shared.AsyncSectionService;
import com.goodow.web.core.shared.Receiver;
import com.goodow.web.core.shared.Section;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.inject.Inject;

import java.util.logging.Logger;

public class SectionEditor extends FormField<Section> {

  interface Driver extends SimpleBeanEditorDriver<Section, SectionEditor> {
  }

  private static Driver driver = GWT.create(Driver.class);

  private static Logger logger = Logger.getLogger(SectionEditor.class.getName());

  private Section section;

  @Inject
  TextField titleEditor;

  @Inject
  ResourceField resourceEditor;

  @Inject
  AsyncSectionService sectionService;

  @Inject
  AsyncResourceService resourceService;

  @Override
  public Section getValue() {
    return section;
  }

  @Override
  public void setValue(final Section value) {
    this.section = value;
    driver.edit(value);
  }

  public void submit() {

    driver.flush();

    if (driver.hasErrors()) {
      // A sub-editor reported errors
    }

    sectionService.save(section).fire(new Receiver<Section>() {
      @Override
      public void onSuccess(final Section result) {
        logger.info("Saved: " + result.getId());
      }
    });
  }

  @Override
  protected void start() {
    super.start();
    main.add(titleEditor);
    main.add(resourceEditor);
    driver.initialize(this);
  }

}
