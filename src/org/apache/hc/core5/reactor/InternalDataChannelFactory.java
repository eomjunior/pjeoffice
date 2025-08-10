package org.apache.hc.core5.reactor;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.apache.hc.core5.net.NamedEndpoint;

interface InternalDataChannelFactory {
  InternalDataChannel create(SelectionKey paramSelectionKey, SocketChannel paramSocketChannel, NamedEndpoint paramNamedEndpoint, Object paramObject);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/InternalDataChannelFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */