package com.retech.reader.web.shared.rpc;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler.LoggingRequestProvider;

import org.cloudlet.web.logging.shared.rpc.ChannelContextProvider;

public interface FinalRequestFactory extends ReaderFactory, LoggingRequestProvider,
    ChannelContextProvider {
}
