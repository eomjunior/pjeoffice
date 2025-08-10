/*     */ package org.apache.tools.ant.util.regexp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegexpUtil
/*     */ {
/*     */   public static boolean hasFlag(int options, int flag) {
/*  36 */     return ((options & flag) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int removeFlag(int options, int flag) {
/*  47 */     return options & -1 - flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int asOptions(String flags) {
/*  63 */     int options = 0;
/*  64 */     if (flags != null) {
/*  65 */       options = asOptions(!flags.contains("i"), flags
/*  66 */           .contains("m"), flags
/*  67 */           .contains("s"));
/*  68 */       if (flags.contains("g")) {
/*  69 */         options |= 0x10;
/*     */       }
/*     */     } 
/*  72 */     return options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int asOptions(boolean caseSensitive) {
/*  83 */     return asOptions(caseSensitive, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int asOptions(boolean caseSensitive, boolean multiLine, boolean singleLine) {
/*  97 */     int options = 0;
/*  98 */     if (!caseSensitive) {
/*  99 */       options |= 0x100;
/*     */     }
/* 101 */     if (multiLine) {
/* 102 */       options |= 0x1000;
/*     */     }
/* 104 */     if (singleLine) {
/* 105 */       options |= 0x10000;
/*     */     }
/* 107 */     return options;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/RegexpUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */