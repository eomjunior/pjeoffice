/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.slf4j.Logger;
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
/*    */ final class LogAppendable
/*    */   implements Appendable
/*    */ {
/*    */   private final Logger log;
/*    */   private final String prefix;
/*    */   private final StringBuilder buffer;
/*    */   
/*    */   public LogAppendable(Logger log, String prefix) {
/* 41 */     this.log = log;
/* 42 */     this.prefix = prefix;
/* 43 */     this.buffer = new StringBuilder();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Appendable append(CharSequence text) throws IOException {
/* 49 */     return append(text, 0, text.length());
/*    */   }
/*    */ 
/*    */   
/*    */   public Appendable append(CharSequence text, int start, int end) throws IOException {
/* 54 */     for (int i = start; i < end; i++) {
/* 55 */       append(text.charAt(i));
/*    */     }
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Appendable append(char ch) throws IOException {
/* 62 */     if (ch == '\n') {
/* 63 */       this.log.debug("{} {}", this.prefix, this.buffer);
/* 64 */       this.buffer.setLength(0);
/* 65 */     } else if (ch != '\r') {
/* 66 */       this.buffer.append(ch);
/*    */     } 
/* 68 */     return this;
/*    */   }
/*    */   
/*    */   public void flush() {
/* 72 */     if (this.buffer.length() > 0) {
/* 73 */       this.log.debug("{} {}", this.prefix, this.buffer);
/* 74 */       this.buffer.setLength(0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/LogAppendable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */