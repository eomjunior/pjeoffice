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
/*    */ 
/*    */ 
/*    */ public class UnicodeUtil
/*    */ {
/*    */   public static StringBuffer EscapeUnicode(char ch) {
/* 36 */     StringBuffer unicodeBuf = new StringBuffer("u0000");
/* 37 */     String s = Integer.toHexString(ch);
/*    */     
/* 39 */     for (int i = 0; i < s.length(); i++) {
/* 40 */       unicodeBuf.setCharAt(unicodeBuf.length() - s
/* 41 */           .length() + i, s
/* 42 */           .charAt(i));
/*    */     }
/* 44 */     return unicodeBuf;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/UnicodeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */