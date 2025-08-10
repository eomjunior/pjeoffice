/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.math.BigInteger;
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
/*    */ public class ArrayTools
/*    */ {
/*    */   public static byte[] convert(byte[] input, int offset, int len) {
/* 35 */     if (offset == 0 && len == input.length) {
/* 36 */       return input;
/*    */     }
/* 38 */     byte[] t = new byte[len];
/* 39 */     System.arraycopy(input, offset, t, 0, len);
/* 40 */     return t;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] subarray(byte[] b, int ofs, int len) {
/* 45 */     byte[] out = new byte[len];
/* 46 */     System.arraycopy(b, ofs, out, 0, len);
/* 47 */     return out;
/*    */   }
/*    */   
/*    */   public static byte[] concat(byte[] b1, byte[] b2) {
/* 51 */     byte[] b = new byte[b1.length + b2.length];
/* 52 */     System.arraycopy(b1, 0, b, 0, b1.length);
/* 53 */     System.arraycopy(b2, 0, b, b1.length, b2.length);
/* 54 */     return b;
/*    */   }
/*    */   
/*    */   public static long[] concat(long[] b1, long[] b2) {
/* 58 */     if (b1.length == 0) {
/* 59 */       return b2;
/*    */     }
/* 61 */     long[] b = new long[b1.length + b2.length];
/* 62 */     System.arraycopy(b1, 0, b, 0, b1.length);
/* 63 */     System.arraycopy(b2, 0, b, b1.length, b2.length);
/* 64 */     return b;
/*    */   }
/*    */   
/*    */   public static byte[] getMagnitude(BigInteger bi) {
/* 68 */     byte[] b = bi.toByteArray();
/* 69 */     if (b.length > 1 && b[0] == 0) {
/* 70 */       int n = b.length - 1;
/* 71 */       byte[] newarray = new byte[n];
/* 72 */       System.arraycopy(b, 1, newarray, 0, n);
/* 73 */       b = newarray;
/*    */     } 
/* 75 */     return b;
/*    */   }
/*    */   
/*    */   public static byte[] getBytesUTF8(String s) {
/*    */     try {
/* 80 */       return s.getBytes("UTF8");
/* 81 */     } catch (UnsupportedEncodingException e) {
/* 82 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/ArrayTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */