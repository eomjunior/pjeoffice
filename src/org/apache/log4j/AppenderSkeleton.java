/*     */ package org.apache.log4j;
/*     */ 
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OnlyOnceErrorHandler;
/*     */ import org.apache.log4j.spi.ErrorHandler;
/*     */ import org.apache.log4j.spi.Filter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.OptionHandler;
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
/*     */ public abstract class AppenderSkeleton
/*     */   implements Appender, OptionHandler
/*     */ {
/*     */   protected Layout layout;
/*     */   protected String name;
/*     */   protected Priority threshold;
/*  55 */   protected ErrorHandler errorHandler = (ErrorHandler)new OnlyOnceErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter headFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Filter tailFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppenderSkeleton() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AppenderSkeleton(boolean isActive) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilter(Filter newFilter) {
/* 100 */     if (this.headFilter == null) {
/* 101 */       this.headFilter = this.tailFilter = newFilter;
/*     */     } else {
/* 103 */       this.tailFilter.setNext(newFilter);
/* 104 */       this.tailFilter = newFilter;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void append(LoggingEvent paramLoggingEvent);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearFilters() {
/* 123 */     this.headFilter = this.tailFilter = null;
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
/*     */   public void finalize() {
/* 135 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 138 */     LogLog.debug("Finalizing appender named [" + this.name + "].");
/* 139 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 148 */     return this.errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter getFilter() {
/* 157 */     return this.headFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Filter getFirstFilter() {
/* 166 */     return this.headFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layout getLayout() {
/* 173 */     return this.layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 182 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Priority getThreshold() {
/* 192 */     return this.threshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAsSevereAsThreshold(Priority priority) {
/* 201 */     return (this.threshold == null || priority.isGreaterOrEqual(this.threshold));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void doAppend(LoggingEvent event) {
/* 210 */     if (this.closed) {
/* 211 */       LogLog.error("Attempted to append to closed appender named [" + this.name + "].");
/*     */       
/*     */       return;
/*     */     } 
/* 215 */     if (!isAsSevereAsThreshold(event.getLevel())) {
/*     */       return;
/*     */     }
/*     */     
/* 219 */     Filter f = this.headFilter;
/*     */     
/* 221 */     while (f != null) {
/* 222 */       switch (f.decide(event)) {
/*     */         case -1:
/*     */           return;
/*     */         case 1:
/*     */           break;
/*     */         case 0:
/* 228 */           f = f.getNext();
/*     */       } 
/*     */     
/*     */     } 
/* 232 */     append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setErrorHandler(ErrorHandler eh) {
/* 241 */     if (eh == null) {
/*     */ 
/*     */       
/* 244 */       LogLog.warn("You have tried to set a null error-handler.");
/*     */     } else {
/* 246 */       this.errorHandler = eh;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLayout(Layout layout) {
/* 256 */     this.layout = layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 263 */     this.name = name;
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
/*     */   public void setThreshold(Priority threshold) {
/* 277 */     this.threshold = threshold;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/AppenderSkeleton.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */