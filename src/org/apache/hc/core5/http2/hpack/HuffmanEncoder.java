/*    */ package org.apache.hc.core5.http2.hpack;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.util.ByteArrayBuffer;
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
/*    */ final class HuffmanEncoder
/*    */ {
/*    */   private final int[] codes;
/*    */   private final byte[] lengths;
/*    */   
/*    */   HuffmanEncoder(int[] codes, byte[] lengths) {
/* 44 */     this.codes = codes;
/* 45 */     this.lengths = lengths;
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ByteArrayBuffer out, ByteBuffer src) {
/* 50 */     long current = 0L;
/* 51 */     int n = 0;
/*    */     
/* 53 */     while (src.hasRemaining()) {
/* 54 */       int b = src.get() & 0xFF;
/* 55 */       int code = this.codes[b];
/* 56 */       int nbits = this.lengths[b];
/*    */       
/* 58 */       current <<= nbits;
/* 59 */       current |= code;
/* 60 */       n += nbits;
/*    */       
/* 62 */       while (n >= 8) {
/* 63 */         n -= 8;
/* 64 */         out.append((int)(current >> n));
/*    */       } 
/*    */     } 
/*    */     
/* 68 */     if (n > 0) {
/* 69 */       current <<= 8 - n;
/* 70 */       current |= (255 >>> n);
/* 71 */       out.append((int)current);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   void encode(ByteArrayBuffer out, CharSequence src, int off, int len) {
/* 77 */     long current = 0L;
/* 78 */     int n = 0;
/*    */     
/* 80 */     for (int i = 0; i < len; i++) {
/* 81 */       int b = src.charAt(off + i) & 0xFF;
/* 82 */       int code = this.codes[b];
/* 83 */       int nbits = this.lengths[b];
/*    */       
/* 85 */       current <<= nbits;
/* 86 */       current |= code;
/* 87 */       n += nbits;
/*    */       
/* 89 */       while (n >= 8) {
/* 90 */         n -= 8;
/* 91 */         out.append((int)(current >> n));
/*    */       } 
/*    */     } 
/*    */     
/* 95 */     if (n > 0) {
/* 96 */       current <<= 8 - n;
/* 97 */       current |= (255 >>> n);
/* 98 */       out.append((int)current);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HuffmanEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */