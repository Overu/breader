package com.goodow.web.core.shared;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity
@Table(name = "t_media")
public class Media extends WebEntity {

  private String path;

  private String fileName;

  private String contentType;

  /**
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * @return the fileName
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  /**
   * @param fileName the fileName to set
   */
  public void setFileName(final String fileName) {
    this.fileName = fileName;
  }

  /**
   * @param path the path to set
   */
  public void setPath(final String path) {
    this.path = path;
  }

}