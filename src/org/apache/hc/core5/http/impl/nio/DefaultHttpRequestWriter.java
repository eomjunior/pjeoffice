/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.http.message.LineFormatter;
/*    */ import org.apache.hc.core5.http.message.RequestLine;
/*    */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*    */ public class DefaultHttpRequestWriter<T extends HttpRequest>
/*    */   extends AbstractMessageWriter<T>
/*    */ {
/*    */   public DefaultHttpRequestWriter(LineFormatter formatter) {
/* 55 */     super(formatter);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestWriter() {
/* 62 */     super(null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(T message, CharArrayBuffer lineBuf) throws IOException {
/* 67 */     lineBuf.clear();
/* 68 */     ProtocolVersion transportVersion = message.getVersion();
/* 69 */     getLineFormatter().formatRequestLine(lineBuf, new RequestLine(message
/* 70 */           .getMethod(), message
/* 71 */           .getRequestUri(), (transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpRequestWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */