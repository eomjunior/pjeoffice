/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.ThrowableInformation;
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
/*     */ public class ThrowableInformationPatternConverter
/*     */   extends LoggingEventPatternConverter
/*     */ {
/*  38 */   private int maxLines = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ThrowableInformationPatternConverter(String[] options) {
/*  46 */     super("Throwable", "throwable");
/*     */     
/*  48 */     if (options != null && options.length > 0) {
/*  49 */       if ("none".equals(options[0])) {
/*  50 */         this.maxLines = 0;
/*  51 */       } else if ("short".equals(options[0])) {
/*  52 */         this.maxLines = 1;
/*     */       } else {
/*     */         try {
/*  55 */           this.maxLines = Integer.parseInt(options[0]);
/*  56 */         } catch (NumberFormatException numberFormatException) {}
/*     */       } 
/*     */     }
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
/*     */   public static ThrowableInformationPatternConverter newInstance(String[] options) {
/*  70 */     return new ThrowableInformationPatternConverter(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/*  77 */     if (this.maxLines != 0) {
/*  78 */       ThrowableInformation information = event.getThrowableInformation();
/*     */       
/*  80 */       if (information != null) {
/*  81 */         String[] stringRep = information.getThrowableStrRep();
/*     */         
/*  83 */         int length = stringRep.length;
/*  84 */         if (this.maxLines < 0) {
/*  85 */           length += this.maxLines;
/*  86 */         } else if (length > this.maxLines) {
/*  87 */           length = this.maxLines;
/*     */         } 
/*     */         
/*  90 */         for (int i = 0; i < length; i++) {
/*  91 */           String string = stringRep[i];
/*  92 */           toAppendTo.append(string).append("\n");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handlesThrowable() {
/* 104 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/ThrowableInformationPatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */