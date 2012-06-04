package com.retech.reader.web.shared.rpc;

import com.goodow.web.logging.shared.rpc.ChannelContextProvider;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler.LoggingRequestProvider;


public interface FinalRequestFactory extends ReaderFactory, LoggingRequestProvider,
    ChannelContextProvider {
}
