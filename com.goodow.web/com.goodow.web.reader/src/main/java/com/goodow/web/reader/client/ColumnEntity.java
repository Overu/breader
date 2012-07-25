package com.goodow.web.reader.client;

import com.google.gwt.user.cellview.client.Column;

public class ColumnEntity<T> {

  Column<T, ?> column;

  int index;

  int width;

  public ColumnEntity(final Column<T, ?> column, final int index, final int width) {
    this.column = column;
    this.index = index;
    this.width = width;
  }

  public Column<T, ?> getColumn() {
    return column;
  }

  public int getIndex() {
    return index;
  }

  public int getWidth() {
    return width;
  }

  public void setColumn(final Column<T, ?> column) {
    this.column = column;
  }

  public void setIndex(final int index) {
    this.index = index;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

}
