/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class BasicLineFormatter
/*    */   implements LineFormatter
/*    */ {
/* 45 */   public static final BasicLineFormatter INSTANCE = new BasicLineFormatter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void formatProtocolVersion(CharArrayBuffer buffer, ProtocolVersion version) {
/* 52 */     buffer.append(version.format());
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
/* 57 */     Args.notNull(buffer, "Char array buffer");
/* 58 */     Args.notNull(reqline, "Request line");
/* 59 */     buffer.append(reqline.getMethod());
/* 60 */     buffer.append(' ');
/* 61 */     buffer.append(reqline.getUri());
/* 62 */     buffer.append(' ');
/* 63 */     formatProtocolVersion(buffer, reqline.getProtocolVersion());
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatStatusLine(CharArrayBuffer buffer, StatusLine statusLine) {
/* 68 */     Args.notNull(buffer, "Char array buffer");
/* 69 */     Args.notNull(statusLine, "Status line");
/*    */     
/* 71 */     formatProtocolVersion(buffer, statusLine.getProtocolVersion());
/* 72 */     buffer.append(' ');
/* 73 */     buffer.append(Integer.toString(statusLine.getStatusCode()));
/* 74 */     buffer.append(' ');
/* 75 */     String reasonPhrase = statusLine.getReasonPhrase();
/* 76 */     if (reasonPhrase != null) {
/* 77 */       buffer.append(reasonPhrase);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void formatHeader(CharArrayBuffer buffer, Header header) {
/* 83 */     Args.notNull(buffer, "Char array buffer");
/* 84 */     Args.notNull(header, "Header");
/*    */     
/* 86 */     buffer.append(header.getName());
/* 87 */     buffer.append(": ");
/* 88 */     String value = header.getValue();
/* 89 */     if (value != null) {
/* 90 */       buffer.ensureCapacity(buffer.length() + value.length());
/* 91 */       for (int valueIndex = 0; valueIndex < value.length(); valueIndex++) {
/* 92 */         char valueChar = value.charAt(valueIndex);
/* 93 */         if (valueChar == '\r' || valueChar == '\n' || valueChar == '\f' || valueChar == '\013')
/*    */         {
/*    */ 
/*    */           
/* 97 */           valueChar = ' ';
/*    */         }
/* 99 */         buffer.append(valueChar);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicLineFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */