/*    */ package br.jus.cnj.pje.office.logger;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import org.apache.log4j.AppenderSkeleton;
/*    */ import org.apache.log4j.Layout;
/*    */ import org.apache.log4j.helpers.LogLog;
/*    */ import org.apache.log4j.spi.LoggingEvent;
/*    */ 
/*    */ public class PjeAppender
/*    */   extends AppenderSkeleton
/*    */ {
/*    */   private final EventBuffer buffer;
/*    */   
/*    */   public PjeAppender() {
/* 15 */     this(PjeEvents.BUFFER);
/*    */   }
/*    */ 
/*    */   
/*    */   private PjeAppender(EventBuffer buffer) {
/* 20 */     this.buffer = buffer;
/*    */   }
/*    */   
/*    */   public void setEventsWindow(String eventsWindow) {
/* 24 */     this.buffer.setMaxSize(Strings.toInt(eventsWindow, 50));
/*    */   }
/*    */ 
/*    */   
/*    */   public void append(LoggingEvent event) {
/* 29 */     if (!checkEntryConditions()) {
/*    */       return;
/*    */     }
/* 32 */     subAppend(event);
/*    */   }
/*    */   
/*    */   protected boolean checkEntryConditions() {
/* 36 */     if (this.closed) {
/* 37 */       LogLog.warn("Not allowed to write to a closed appender.");
/* 38 */       return false;
/*    */     } 
/*    */     
/* 41 */     if (this.layout == null) {
/* 42 */       this.errorHandler.error("No layout set for the appender named [" + this.name + "].");
/* 43 */       return false;
/*    */     } 
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void close() {
/* 50 */     if (this.closed)
/*    */       return; 
/* 52 */     this.closed = true;
/* 53 */     writeFooter();
/* 54 */     reset();
/*    */   }
/*    */   
/*    */   protected void reset() {
/* 58 */     this.buffer.reset();
/*    */   }
/*    */   
/*    */   protected void subAppend(LoggingEvent event) {
/* 62 */     StringBuilder builder = new StringBuilder();
/* 63 */     builder.append(this.layout.format(event));
/* 64 */     if (this.layout.ignoresThrowable()) {
/* 65 */       String[] s = event.getThrowableStrRep();
/* 66 */       if (s != null) {
/* 67 */         int len = s.length;
/* 68 */         for (int i = 0; i < len; i++) {
/* 69 */           builder.append(s[i]).append(Layout.LINE_SEP);
/*    */         }
/*    */       } 
/*    */     } 
/* 73 */     this.buffer.add(builder.toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresLayout() {
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   protected void writeFooter() {
/* 82 */     if (this.layout != null) {
/* 83 */       String f = this.layout.getFooter();
/* 84 */       if (f != null) {
/* 85 */         this.buffer.add(f);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void writeHeader() {
/* 91 */     if (this.layout != null) {
/* 92 */       String h = this.layout.getHeader();
/* 93 */       if (h != null)
/* 94 */         this.buffer.add(h); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/logger/PjeAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */