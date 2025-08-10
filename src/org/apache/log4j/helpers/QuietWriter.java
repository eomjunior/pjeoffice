/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.FilterWriter;
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
/*    */ public class QuietWriter
/*    */   extends FilterWriter
/*    */ {
/*    */   protected ErrorHandler errorHandler;
/*    */   
/*    */   public QuietWriter(Writer writer, ErrorHandler errorHandler) {
/* 38 */     super(writer);
/* 39 */     setErrorHandler(errorHandler);
/*    */   }
/*    */   
/*    */   public void write(String string) {
/* 43 */     if (string != null) {
/*    */       try {
/* 45 */         this.out.write(string);
/* 46 */       } catch (Exception e) {
/* 47 */         this.errorHandler.error("Failed to write [" + string + "].", e, 1);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void flush() {
/*    */     try {
/* 54 */       this.out.flush();
/* 55 */     } catch (Exception e) {
/* 56 */       this.errorHandler.error("Failed to flush writer,", e, 2);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setErrorHandler(ErrorHandler eh) {
/* 61 */     if (eh == null)
/*    */     {
/* 63 */       throw new IllegalArgumentException("Attempted to set null ErrorHandler.");
/*    */     }
/* 65 */     this.errorHandler = eh;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/QuietWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */