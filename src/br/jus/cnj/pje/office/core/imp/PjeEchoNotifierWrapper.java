/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeEchoNotifier;
/*    */ import com.github.utils4j.IStringDumpable;
/*    */ import com.github.utils4j.imp.Args;
/*    */ 
/*    */ class PjeEchoNotifierWrapper
/*    */   implements IPjeEchoNotifier
/*    */ {
/*    */   private volatile IPjeEchoNotifier notifier;
/*    */   
/*    */   public PjeEchoNotifierWrapper(IPjeEchoNotifier notifier) {
/* 13 */     setNotifier(notifier);
/*    */   }
/*    */   
/*    */   protected final IPjeEchoNotifier getNotifier() {
/* 17 */     return this.notifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpen() {
/* 22 */     return this.notifier.isOpen();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisible() {
/* 27 */     return this.notifier.isVisible();
/*    */   }
/*    */ 
/*    */   
/*    */   public void show() {
/* 32 */     this.notifier.show();
/*    */   }
/*    */ 
/*    */   
/*    */   public void open() {
/* 37 */     this.notifier.open();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 42 */     this.notifier.close();
/*    */   }
/*    */ 
/*    */   
/*    */   public void echoS2B(IStringDumpable request) {
/* 47 */     this.notifier.echoS2B(request);
/*    */   }
/*    */ 
/*    */   
/*    */   public void echoN2S(IStringDumpable request) {
/* 52 */     this.notifier.echoN2S(request);
/*    */   }
/*    */   
/*    */   protected final void setNotifier(IPjeEchoNotifier notifier) {
/* 56 */     this.notifier = (IPjeEchoNotifier)Args.requireNonNull(notifier, "notifier is null");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeEchoNotifierWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */