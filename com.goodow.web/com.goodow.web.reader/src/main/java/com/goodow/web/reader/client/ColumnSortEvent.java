package com.goodow.web.reader.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnSortEvent extends GwtEvent<ColumnSortEvent.Handler> {

  public static interface Handler extends EventHandler {

    void onColumnSort(ColumnSortEvent event);
  }

  public static class ListHandler<T> implements Handler {

    private final Map<String, Comparator<T>> comparators = new HashMap<String, Comparator<T>>();
    private List<T> list;
    private Sort curSort;

    public ListHandler(final List<T> list) {
      this.list = list;
    }

    public List<T> getList() {
      return list;
    }

    @Override
    public void onColumnSort(final ColumnSortEvent event) {
      String header = event.getHeader();
      if (header == null || header.equals("")) {
        return;
      }

      final Comparator<T> comparator = comparators.get(header);
      if (comparator == null) {
        return;
      }

      Sort sort = event.getSort();

      if (sort == null) {
        sort = curSort != Sort.ASC ? Sort.ASC : Sort.DSC;
      }
      if (curSort != null && curSort.equals(sort)) {
        return;
      }
      switch (sort) {
        case ASC:
          asc(comparator);
          break;
        case DSC:
          dsc(comparator);
          break;

        default:
          break;
      }
      curSort = sort;
    }

    public void setComparator(final String header, final Comparator<T> comparator) {
      comparators.put(header, comparator);
    }

    public void setList(final List<T> list) {
      assert list != null : "list cannot be null";
      this.list = list;
    }

    private void asc(final Comparator<T> comparator) {
      Collections.sort(list, comparator);
    }

    private void dsc(final Comparator<T> comparator) {
      Collections.sort(list, new Comparator<T>() {
        @Override
        public int compare(final T o1, final T o2) {
          return -comparator.compare(o1, o2);
        }
      });
    }
  }

  public enum Sort {
    ASC, DSC
  }

  public static Type<Handler> TYPE = new Type<Handler>();

  public static ColumnSortEvent fire(final HasHandlers source, final String header) {
    return fire(source, header, null);
  }

  public static ColumnSortEvent fire(final HasHandlers source, final String header, final Sort sort) {
    ColumnSortEvent event = new ColumnSortEvent(header, sort);
    if (TYPE != null) {
      source.fireEvent(event);
    }
    return event;
  }

  private String header;
  private Sort sort;

  public ColumnSortEvent(final String header, final Sort sort) {
    this.setSort(sort);
    this.setHeader(header);
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
    return TYPE;
  }

  public String getHeader() {
    return header;
  }

  public Sort getSort() {
    return sort;
  }

  public void setHeader(final String header) {
    this.header = header;
  }

  public void setSort(final Sort sort) {
    this.sort = sort;
  }

  @Override
  protected void dispatch(final Handler handler) {
    handler.onColumnSort(this);
  }
}
