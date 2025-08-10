/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.NotImplementedException;
/*    */ import org.apache.hc.core5.http.ProtocolException;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class DefaultContentLengthStrategy
/*    */   implements ContentLengthStrategy
/*    */ {
/* 55 */   public static final DefaultContentLengthStrategy INSTANCE = new DefaultContentLengthStrategy();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long determineLength(HttpMessage message) throws HttpException {
/* 66 */     Args.notNull(message, "HTTP message");
/*    */ 
/*    */ 
/*    */     
/* 70 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/* 71 */     if (transferEncodingHeader != null) {
/* 72 */       String headerValue = transferEncodingHeader.getValue();
/* 73 */       if ("chunked".equalsIgnoreCase(headerValue)) {
/* 74 */         return -1L;
/*    */       }
/* 76 */       throw new NotImplementedException("Unsupported transfer encoding: " + headerValue);
/*    */     } 
/* 78 */     if (message.countHeaders("Content-Length") > 1) {
/* 79 */       throw new ProtocolException("Multiple Content-Length headers");
/*    */     }
/* 81 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 82 */     if (contentLengthHeader != null) {
/* 83 */       String s = contentLengthHeader.getValue();
/*    */       try {
/* 85 */         long len = Long.parseLong(s);
/* 86 */         if (len < 0L) {
/* 87 */           throw new ProtocolException("Negative content length: " + s);
/*    */         }
/* 89 */         return len;
/* 90 */       } catch (NumberFormatException e) {
/* 91 */         throw new ProtocolException("Invalid content length: " + s);
/*    */       } 
/*    */     } 
/* 94 */     return -9223372036854775807L;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/DefaultContentLengthStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */