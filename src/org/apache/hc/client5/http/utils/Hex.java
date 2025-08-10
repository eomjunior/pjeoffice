/*    */ package org.apache.hc.client5.http.utils;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
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
/*    */ @Internal
/*    */ public class Hex
/*    */ {
/*    */   public static String encodeHexString(byte[] bytes) {
/* 41 */     char[] out = new char[bytes.length * 2];
/*    */     
/* 43 */     encodeHex(bytes, 0, bytes.length, DIGITS_LOWER, out, 0);
/* 44 */     return new String(out);
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
/* 55 */   private static final char[] DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
/*    */   private static void encodeHex(byte[] data, int dataOffset, int dataLen, char[] toDigits, char[] out, int outOffset) {
/* 73 */     for (int i = dataOffset, j = outOffset; i < dataOffset + dataLen; i++) {
/* 74 */       out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
/* 75 */       out[j++] = toDigits[0xF & data[i]];
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/utils/Hex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */