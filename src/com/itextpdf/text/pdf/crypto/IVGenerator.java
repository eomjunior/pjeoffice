/*    */ package com.itextpdf.text.pdf.crypto;
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
/*    */ public final class IVGenerator
/*    */ {
/* 55 */   private static ARCFOUREncryption arcfour = new ARCFOUREncryption(); static {
/* 56 */     long time = System.currentTimeMillis();
/* 57 */     long mem = Runtime.getRuntime().freeMemory();
/* 58 */     String s = time + "+" + mem;
/* 59 */     arcfour.prepareARCFOURKey(s.getBytes());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getIV() {
/* 71 */     return getIV(16);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] getIV(int len) {
/* 80 */     byte[] b = new byte[len];
/* 81 */     synchronized (arcfour) {
/* 82 */       arcfour.encryptARCFOUR(b);
/*    */     } 
/* 84 */     return b;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/crypto/IVGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */