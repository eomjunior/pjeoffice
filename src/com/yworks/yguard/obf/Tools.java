/*    */ package com.yworks.yguard.obf;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tools
/*    */ {
/*    */   public static boolean isInArray(String s, String[] list) {
/* 34 */     for (int i = 0; i < list.length; ) { if (s.equals(list[i])) return true;  i++; }
/* 35 */      return false;
/*    */   }
/*    */ 
/*    */   
/* 39 */   private static final char[] base64 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final char pad = '=';
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toBase64(byte[] b) {
/* 54 */     StringBuffer sb = new StringBuffer();
/* 55 */     for (int ptr = 0; ptr < b.length; ptr += 3) {
/*    */       
/* 57 */       sb.append(base64[b[ptr] >> 2 & 0x3F]);
/* 58 */       if (ptr + 1 < b.length) {
/*    */         
/* 60 */         sb.append(base64[b[ptr] << 4 & 0x30 | b[ptr + 1] >> 4 & 0xF]);
/* 61 */         if (ptr + 2 < b.length)
/*    */         {
/* 63 */           sb.append(base64[b[ptr + 1] << 2 & 0x3C | b[ptr + 2] >> 6 & 0x3]);
/* 64 */           sb.append(base64[b[ptr + 2] & 0x3F]);
/*    */         }
/*    */         else
/*    */         {
/* 68 */           sb.append(base64[b[ptr + 1] << 2 & 0x3C]);
/* 69 */           sb.append('=');
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 74 */         sb.append(base64[b[ptr] << 4 & 0x30]);
/* 75 */         sb.append('=');
/* 76 */         sb.append('=');
/*    */       } 
/*    */     } 
/* 79 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/Tools.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */