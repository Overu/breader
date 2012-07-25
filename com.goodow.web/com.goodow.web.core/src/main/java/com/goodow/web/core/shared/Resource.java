package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_resource")
public class Resource extends WebEntity {

  private String path;

  private String fileName;

  private String mimeType;

  public String getMimeType() {
    return mimeType;
  }

  public String getFileName() {
    return fileName;
  }

  public String getPath() {
    return path;
  }

  public void setMimeType(final String contentType) {
    this.mimeType = contentType;
  }

  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  public void setPath(final String path) {
    this.path = path;
  }

}