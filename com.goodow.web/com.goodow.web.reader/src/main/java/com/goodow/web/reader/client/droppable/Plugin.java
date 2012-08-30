package com.goodow.web.reader.client.droppable;


public interface Plugin<T extends SimpleQuery> {

  T init(SimpleQuery sQuery);

}
