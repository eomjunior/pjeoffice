/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public enum Sizes
/*    */ {
/* 33 */   B(1L, "B"),
/* 34 */   KB(B.size * 1024L, "KB"),
/* 35 */   MB(KB.size * 1024L, "MB"),
/* 36 */   GB(MB.size * 1024L, "GB"),
/* 37 */   TB(GB.size * 1024L, "TB"),
/* 38 */   PB(TB.size * 1024L, "PB");
/*    */   
/*    */   private final long size;
/*    */   
/*    */   private final String label;
/*    */   
/*    */   Sizes(long size, String label) {
/* 45 */     this.size = size;
/* 46 */     this.label = label;
/*    */   }
/*    */   
/*    */   public static String defaultFormat(double size) {
/* 50 */     if (size < KB.size)
/* 51 */       return B.format(size); 
/* 52 */     if (size < MB.size)
/* 53 */       return KB.format(size); 
/* 54 */     if (size < GB.size)
/* 55 */       return MB.format(size); 
/* 56 */     if (size < TB.size)
/* 57 */       return GB.format(size); 
/* 58 */     return TB.format(size);
/*    */   }
/*    */   
/*    */   public long toBytes(double size) {
/* 62 */     return (long)(this.size * size);
/*    */   }
/*    */   
/*    */   private String format(double size) {
/* 66 */     double value = size / this.size;
/* 67 */     if (size % this.size == 0.0D)
/* 68 */       return String.format("%d %s", new Object[] { Long.valueOf((long)value), this.label }); 
/* 69 */     return String.format(Locale.US, "%.2f %s", new Object[] { Double.valueOf(value), this.label });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Sizes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */