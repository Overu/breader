package com.goodow.wave.server.media;

public enum MimeType {
  TEXT_HTML("html"), TEXT_CSS("css"), TEXT_JAVASCRIPT("js"), //
  APPLICATION_PDF("pdf"), //
  IMAGE_JPEG("jpg"), IMAGE_PNG("png");

  private final String extension;

  MimeType(final String extension) {
    this.extension = extension;
  }

  public String getExtension() {
    return extension;
  }

  public String getType() {
    return name().toLowerCase().replaceAll("_", "/");
  }
}
