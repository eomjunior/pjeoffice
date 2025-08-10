/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Native2AsciiUtils
/*    */ {
/*    */   private static final int MAX_ASCII = 127;
/*    */   
/*    */   public static String native2ascii(String line) {
/* 36 */     StringBuilder sb = new StringBuilder();
/* 37 */     for (char c : line.toCharArray()) {
/* 38 */       if (c <= '') {
/* 39 */         sb.append(c);
/*    */       } else {
/* 41 */         sb.append(String.format("\\u%04x", new Object[] { Integer.valueOf(c) }));
/*    */       } 
/*    */     } 
/* 44 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String ascii2native(String line) {
/* 54 */     StringBuilder sb = new StringBuilder();
/* 55 */     int inputLen = line.length();
/* 56 */     for (int i = 0; i < inputLen; i++) {
/* 57 */       char c = line.charAt(i);
/* 58 */       if (c != '\\' || i >= inputLen - 5) {
/* 59 */         sb.append(c); continue;
/*    */       } 
/* 61 */       char u = line.charAt(++i);
/* 62 */       if (u == 'u') {
/* 63 */         int unescaped = tryParse(line, i + 1);
/* 64 */         if (unescaped >= 0) {
/* 65 */           sb.append((char)unescaped);
/* 66 */           i += 4;
/*    */           
/*    */           continue;
/*    */         } 
/*    */       } 
/* 71 */       sb.append(c).append(u);
/*    */       continue;
/*    */     } 
/* 74 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private static int tryParse(String line, int startIdx) {
/*    */     try {
/* 79 */       return Integer.parseInt(line.substring(startIdx, startIdx + 4), 16);
/* 80 */     } catch (NumberFormatException ex) {
/* 81 */       return -1;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/Native2AsciiUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */