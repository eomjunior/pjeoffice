/*     */ package org.apache.log4j;
/*     */ 
/*     */ import org.apache.log4j.helpers.DateLayout;
/*     */ import org.apache.log4j.spi.LoggingEvent;
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
/*     */ public class TTCCLayout
/*     */   extends DateLayout
/*     */ {
/*     */   private boolean threadPrinting = true;
/*     */   private boolean categoryPrefixing = true;
/*     */   private boolean contextPrinting = true;
/*  84 */   protected final StringBuffer buf = new StringBuffer(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TTCCLayout() {
/*  94 */     setDateFormat("RELATIVE", null);
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
/*     */   public TTCCLayout(String dateFormatType) {
/* 106 */     setDateFormat(dateFormatType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadPrinting(boolean threadPrinting) {
/* 114 */     this.threadPrinting = threadPrinting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getThreadPrinting() {
/* 121 */     return this.threadPrinting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCategoryPrefixing(boolean categoryPrefixing) {
/* 129 */     this.categoryPrefixing = categoryPrefixing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCategoryPrefixing() {
/* 136 */     return this.categoryPrefixing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContextPrinting(boolean contextPrinting) {
/* 145 */     this.contextPrinting = contextPrinting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getContextPrinting() {
/* 152 */     return this.contextPrinting;
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
/*     */   
/*     */   public String format(LoggingEvent event) {
/* 169 */     this.buf.setLength(0);
/*     */     
/* 171 */     dateFormat(this.buf, event);
/*     */     
/* 173 */     if (this.threadPrinting) {
/* 174 */       this.buf.append('[');
/* 175 */       this.buf.append(event.getThreadName());
/* 176 */       this.buf.append("] ");
/*     */     } 
/* 178 */     this.buf.append(event.getLevel().toString());
/* 179 */     this.buf.append(' ');
/*     */     
/* 181 */     if (this.categoryPrefixing) {
/* 182 */       this.buf.append(event.getLoggerName());
/* 183 */       this.buf.append(' ');
/*     */     } 
/*     */     
/* 186 */     if (this.contextPrinting) {
/* 187 */       String ndc = event.getNDC();
/*     */       
/* 189 */       if (ndc != null) {
/* 190 */         this.buf.append(ndc);
/* 191 */         this.buf.append(' ');
/*     */       } 
/*     */     } 
/* 194 */     this.buf.append("- ");
/* 195 */     this.buf.append(event.getRenderedMessage());
/* 196 */     this.buf.append(LINE_SEP);
/* 197 */     return this.buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoresThrowable() {
/* 207 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/TTCCLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */