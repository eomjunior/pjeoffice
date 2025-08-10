/*    */ package org.apache.hc.core5.io;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
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
/*    */ public final class Closer
/*    */ {
/*    */   public static void close(Closeable closeable) throws IOException {
/* 47 */     if (closeable != null) {
/* 48 */       closeable.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void close(ModalCloseable closeable, CloseMode closeMode) {
/* 59 */     if (closeable != null) {
/* 60 */       closeable.close(closeMode);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeQuietly(Closeable closeable) {
/*    */     try {
/* 71 */       close(closeable);
/* 72 */     } catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/io/Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */