package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_resource")
public class Resource extends WebContent {

  private String fileName;

  private String mimeType;

  private String textContent;

  public String getFileName() {
    return fileName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getTextContent() {
    return textContent;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public void setMimeType(final String contentType) {
    this.mimeType = contentType;
  }

  public void setTextContent(final String textContent) {
    this.textContent = textContent;
  }

}