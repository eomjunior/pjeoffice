/*    */ package org.apache.hc.client5.http.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.brotli.dec.BrotliInputStream;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class BrotliInputStreamFactory
/*    */   implements InputStreamFactory
/*    */ {
/* 44 */   private static final BrotliInputStreamFactory INSTANCE = new BrotliInputStreamFactory();
/*    */   
/*    */   public static BrotliInputStreamFactory getInstance() {
/* 47 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream create(InputStream inputStream) throws IOException {
/* 52 */     return (InputStream)new BrotliInputStream(inputStream);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/BrotliInputStreamFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */