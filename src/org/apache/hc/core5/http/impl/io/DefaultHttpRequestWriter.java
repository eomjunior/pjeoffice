/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.HttpMessage;
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
/*    */ public class DefaultHttpRequestWriter
/*    */   extends AbstractMessageWriter<ClassicHttpRequest>
/*    */ {
/*    */   public DefaultHttpRequestWriter(LineFormatter formatter) {
/* 55 */     super(formatter);
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestWriter() {
/* 59 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(ClassicHttpRequest message, CharArrayBuffer lineBuf) throws IOException {
/* 65 */     ProtocolVersion transportVersion = message.getVersion();
/* 66 */     getLineFormatter().formatRequestLine(lineBuf, new RequestLine(message
/* 67 */           .getMethod(), message
/* 68 */           .getRequestUri(), (transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpRequestWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */