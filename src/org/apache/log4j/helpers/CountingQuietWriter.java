/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import org.apache.log4j.spi.ErrorHandler;
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
/*    */ public class CountingQuietWriter
/*    */   extends QuietWriter
/*    */ {
/*    */   protected long count;
/*    */   
/*    */   public CountingQuietWriter(Writer writer, ErrorHandler eh) {
/* 38 */     super(writer, eh);
/*    */   }
/*    */   
/*    */   public void write(String string) {
/*    */     try {
/* 43 */       this.out.write(string);
/* 44 */       this.count += string.length();
/* 45 */     } catch (IOException e) {
/* 46 */       this.errorHandler.error("Write failure.", e, 1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public long getCount() {
/* 51 */     return this.count;
/*    */   }
/*    */   
/*    */   public void setCount(long count) {
/* 55 */     this.count = count;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/CountingQuietWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */