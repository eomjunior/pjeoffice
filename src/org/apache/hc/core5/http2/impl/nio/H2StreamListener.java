package org.apache.hc.core5.http2.impl.nio;

import java.util.List;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpConnection;
import org.apache.hc.core5.http2.frame.RawFrame;

@Contract(threading = ThreadingBehavior.STATELESS)
@Internal
public interface H2StreamListener {
  void onHeaderInput(HttpConnection paramHttpConnection, int paramInt, List<? extends Header> paramList);
  
  void onHeaderOutput(HttpConnection paramHttpConnection, int paramInt, List<? extends Header> paramList);
  
  void onFrameInput(HttpConnection paramHttpConnection, int paramInt, RawFrame paramRawFrame);
  
  void onFrameOutput(HttpConnection paramHttpConnection, int paramInt, RawFrame paramRawFrame);
  
  void onInputFlowControl(HttpConnection paramHttpConnection, int paramInt1, int paramInt2, int paramInt3);
  
  void onOutputFlowControl(HttpConnection paramHttpConnection, int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/H2StreamListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */