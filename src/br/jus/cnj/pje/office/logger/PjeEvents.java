/*    */ package br.jus.cnj.pje.office.logger;
/*    */ 
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public enum PjeEvents
/*    */   implements EventBuffer
/*    */ {
/* 35 */   BUFFER; private volatile int windowSize;
/*    */   PjeEvents() {
/* 37 */     this.eventsWindow = new LinkedList<>();
/*    */     
/* 39 */     this.windowSize = 50;
/*    */   }
/*    */   private final List<String> eventsWindow;
/*    */   public void setMaxSize(int value) {
/* 43 */     if (value <= 0) {
/* 44 */       reset();
/*    */       return;
/*    */     } 
/* 47 */     this.windowSize = Math.min(value, 500);
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 52 */     synchronized (this.eventsWindow) {
/* 53 */       this.eventsWindow.clear();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(String event) {
/* 59 */     synchronized (this.eventsWindow) {
/* 60 */       if (this.eventsWindow.size() >= this.windowSize) {
/* 61 */         this.eventsWindow.remove(0);
/*    */       }
/* 63 */       this.eventsWindow.add(Strings.text(event));
/*    */     } 
/*    */   }
/*    */   
/*    */   public String pack() {
/* 68 */     StringBuilder pack = new StringBuilder();
/* 69 */     synchronized (this.eventsWindow) {
/* 70 */       this.eventsWindow.stream().forEach(pack::append);
/*    */     } 
/* 72 */     return pack.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/logger/PjeEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */