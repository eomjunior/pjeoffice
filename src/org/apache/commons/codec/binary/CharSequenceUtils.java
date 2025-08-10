/*    */ package org.apache.commons.codec.binary;
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
/*    */ public class CharSequenceUtils
/*    */ {
/*    */   static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
/* 54 */     if (cs instanceof String && substring instanceof String) {
/* 55 */       return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
/*    */     }
/* 57 */     int index1 = thisStart;
/* 58 */     int index2 = start;
/* 59 */     int tmpLen = length;
/*    */     
/* 61 */     while (tmpLen-- > 0) {
/* 62 */       char c1 = cs.charAt(index1++);
/* 63 */       char c2 = substring.charAt(index2++);
/*    */       
/* 65 */       if (c1 == c2) {
/*    */         continue;
/*    */       }
/*    */       
/* 69 */       if (!ignoreCase) {
/* 70 */         return false;
/*    */       }
/*    */ 
/*    */       
/* 74 */       if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && 
/* 75 */         Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
/* 76 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/CharSequenceUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */