/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.InputStreamFactory;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
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
/*    */ public class OpenByteArrayOutputStream
/*    */   extends ByteArrayOutputStream
/*    */   implements InputStreamFactory
/*    */ {
/*    */   public OpenByteArrayOutputStream() {}
/*    */   
/*    */   public OpenByteArrayOutputStream(long size) {
/* 47 */     super((size > 2147483647L) ? 32 : (int)size);
/*    */   }
/*    */   
/*    */   public final synchronized String asString() {
/* 51 */     return new String(this.buf, 0, size());
/*    */   }
/*    */   
/*    */   public final synchronized String asString(String charset) {
/* 55 */     return asString(Charset.forName(charset));
/*    */   }
/*    */   
/*    */   public final synchronized String asString(Charset charset) {
/* 59 */     return new String(this.buf, 0, size(), charset);
/*    */   }
/*    */ 
/*    */   
/*    */   public final synchronized InputStream toInputStream() {
/* 64 */     return new ByteArrayInputStream(this.buf, 0, size());
/*    */   }
/*    */   
/*    */   public final synchronized <T, E extends Throwable> T process(IBufferProcessor<T, E> processor) throws E {
/* 68 */     return processor.process(this.buf, 0, size());
/*    */   }
/*    */ 
/*    */   
/*    */   public final synchronized void close() {
/* 73 */     this.buf = new byte[32];
/* 74 */     this.count = 0;
/*    */   }
/*    */   
/*    */   public static interface IBufferProcessor<T, E extends Throwable> {
/*    */     T process(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws E;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/OpenByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */