package com.retech.reader.web.shared;

import com.goodow.web.core.shared.WebContent;

import com.gooodow.wave.shared.media.MimeType;

import org.apache.commons.lang.StringUtils;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Resource extends WebContent {

  @Enumerated(EnumType.STRING)
  private MimeType mimeType;

  private String name;

  @Transient
  private String dataString;

  @ManyToOne
  private Page page;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] data;

  private boolean lightWeight;

  public void becomeLightWeight() {
    dataString = null;
    lightWeight = true;
  }

  public byte[] getData() {
    return data;
  }

  public String getDataString() {
    if (!lightWeight && dataString == null && data != null) {
      dataString = org.apache.commons.codec.binary.StringUtils.newStringUtf8(data);
    }
    return dataString;
  }

  public String getFilename() {
    return (StringUtils.isEmpty(name) ? "temp" : name) + "." + mimeType.getExtension();
  }

  public MimeType getMimeType() {
    return mimeType;
  }

  public String getName() {
    return name;
  }

  public Page getPage() {
    return page;
  }

  public boolean isImage() {
    return getMimeType().getType().startsWith("image/");
  }

  public Resource setData(final byte[] data) {
    this.data = data;
    return this;
  }

  public Resource setDataString(final String dataString) {
    this.dataString = dataString;
    return this;
  }

  public Resource setLightWeight(final boolean lightWeight) {
    this.lightWeight = lightWeight;
    return this;
  }

  public Resource setMimeType(final MimeType mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  public Resource setName(final String name) {
    this.name = name;
    return this;
  }

  public void setPage(final Page page) {
    this.page = page;
  }

}
