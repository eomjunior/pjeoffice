/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.SocketAddress;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.hc.core5.http.EndpointDetails;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.io.CloseMode;
/*    */ import org.apache.hc.core5.net.InetAddressUtils;
/*    */ import org.apache.hc.core5.reactor.IOSession;
/*    */ import org.apache.hc.core5.util.Timeout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerH2IOEventHandler
/*    */   extends AbstractH2IOEventHandler
/*    */ {
/*    */   public ServerH2IOEventHandler(ServerH2StreamMultiplexer streamMultiplexer) {
/* 42 */     super(streamMultiplexer);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     StringBuilder buf = new StringBuilder();
/* 48 */     InetAddressUtils.formatAddress(buf, getRemoteAddress());
/* 49 */     buf.append("->");
/* 50 */     InetAddressUtils.formatAddress(buf, getLocalAddress());
/* 51 */     buf.append(" [");
/* 52 */     this.streamMultiplexer.appendState(buf);
/* 53 */     buf.append("]");
/* 54 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerH2IOEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */