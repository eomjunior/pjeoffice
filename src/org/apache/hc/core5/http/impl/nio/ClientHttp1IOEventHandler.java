/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.SocketAddress;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.hc.core5.http.EndpointDetails;
/*    */ import org.apache.hc.core5.http.HttpVersion;
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
/*    */ 
/*    */ public class ClientHttp1IOEventHandler
/*    */   extends AbstractHttp1IOEventHandler
/*    */ {
/*    */   public ClientHttp1IOEventHandler(ClientHttp1StreamDuplexer streamDuplexer) {
/* 44 */     super(streamDuplexer);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     StringBuilder buf = new StringBuilder();
/* 50 */     InetAddressUtils.formatAddress(buf, getLocalAddress());
/* 51 */     buf.append("->");
/* 52 */     InetAddressUtils.formatAddress(buf, getRemoteAddress());
/* 53 */     buf.append(" [");
/* 54 */     this.streamDuplexer.appendState(buf);
/* 55 */     buf.append("]");
/* 56 */     return buf.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public ProtocolVersion getProtocolVersion() {
/* 61 */     ProtocolVersion protocolVersion = super.getProtocolVersion();
/* 62 */     return (protocolVersion != null) ? protocolVersion : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ClientHttp1IOEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */