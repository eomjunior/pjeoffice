/*     */ package com.itextpdf.text.log;
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
/*     */ public class SysoLogger
/*     */   implements Logger
/*     */ {
/*     */   private String name;
/*     */   private final int shorten;
/*     */   
/*     */   public SysoLogger() {
/*  59 */     this(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SysoLogger(int packageReduce) {
/*  67 */     this.shorten = packageReduce;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SysoLogger(String klass, int shorten) {
/*  75 */     this.shorten = shorten;
/*  76 */     this.name = klass;
/*     */   }
/*     */   
/*     */   public Logger getLogger(Class<?> klass) {
/*  80 */     return new SysoLogger(klass.getName(), this.shorten);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getLogger(String name) {
/*  87 */     return new SysoLogger("[itext]", 0);
/*     */   }
/*     */   
/*     */   public boolean isLogging(Level level) {
/*  91 */     return true;
/*     */   }
/*     */   
/*     */   public void warn(String message) {
/*  95 */     System.out.println(String.format("%s WARN  %s", new Object[] { shorten(this.name), message }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String shorten(String className) {
/* 103 */     if (this.shorten != 0) {
/* 104 */       StringBuilder target = new StringBuilder();
/* 105 */       String name = className;
/* 106 */       int fromIndex = className.indexOf('.');
/* 107 */       while (fromIndex != -1) {
/* 108 */         int parseTo = (fromIndex < this.shorten) ? fromIndex : this.shorten;
/* 109 */         target.append(name.substring(0, parseTo));
/* 110 */         target.append('.');
/* 111 */         name = name.substring(fromIndex + 1);
/* 112 */         fromIndex = name.indexOf('.');
/*     */       } 
/* 114 */       target.append(className.substring(className.lastIndexOf('.') + 1));
/* 115 */       return target.toString();
/*     */     } 
/* 117 */     return className;
/*     */   }
/*     */   
/*     */   public void trace(String message) {
/* 121 */     System.out.println(String.format("%s TRACE %s", new Object[] { shorten(this.name), message }));
/*     */   }
/*     */   
/*     */   public void debug(String message) {
/* 125 */     System.out.println(String.format("%s DEBUG %s", new Object[] { shorten(this.name), message }));
/*     */   }
/*     */   
/*     */   public void info(String message) {
/* 129 */     System.out.println(String.format("%s INFO  %s", new Object[] { shorten(this.name), message }));
/*     */   }
/*     */   
/*     */   public void error(String message) {
/* 133 */     System.out.println(String.format("%s ERROR %s", new Object[] { this.name, message }));
/*     */   }
/*     */   
/*     */   public void error(String message, Exception e) {
/* 137 */     System.out.println(String.format("%s ERROR %s", new Object[] { this.name, message }));
/* 138 */     e.printStackTrace(System.out);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/log/SysoLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */