package org.cloudlet.web.service.shared.rpc;

public enum MimeType {
  HTML, CSS, JAVASCRIPT() {
    @Override
    public String getExtension() {
      return "js";
    }
  },
  JPG("image/jpeg", "jpg"), PNG("image/png", "png");

  private final String extension;
  private final String type;

  MimeType() {
    this.extension = name().toLowerCase();
    this.type = "text/" + extension;
  }

  MimeType(final String type, final String extension) {
    this.type = type;
    this.extension = extension;
  }

  public String getExtension() {
    return extension;
  }

  public String getType() {
    return type;
  }
}
