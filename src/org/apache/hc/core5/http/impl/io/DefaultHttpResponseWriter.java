/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.http.message.LineFormatter;
/*    */ import org.apache.hc.core5.http.message.StatusLine;
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
/*    */ public class DefaultHttpResponseWriter
/*    */   extends AbstractMessageWriter<ClassicHttpResponse>
/*    */ {
/*    */   public DefaultHttpResponseWriter(LineFormatter formatter) {
/* 55 */     super(formatter);
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseWriter() {
/* 59 */     super(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(ClassicHttpResponse message, CharArrayBuffer lineBuf) throws IOException {
/* 65 */     ProtocolVersion transportVersion = message.getVersion();
/* 66 */     getLineFormatter().formatStatusLine(lineBuf, new StatusLine((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1, message
/*    */           
/* 68 */           .getCode(), message
/* 69 */           .getReasonPhrase()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpResponseWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */