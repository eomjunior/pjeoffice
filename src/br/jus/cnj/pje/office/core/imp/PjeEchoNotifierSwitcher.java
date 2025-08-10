/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeEchoNotifier;
/*    */ import br.jus.cnj.pje.office.core.IPjeEchoNotifierSwitcher;
/*    */ import com.github.utils4j.IStringDumpable;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ class PjeEchoNotifierSwitcher
/*    */   extends PjeEchoNotifierWrapper
/*    */   implements IPjeEchoNotifierSwitcher
/*    */ {
/* 14 */   private static final Logger LOGGER = LoggerFactory.getLogger(PjeEchoNotifierSwitcher.class);
/*    */   
/*    */   public PjeEchoNotifierSwitcher(IPjeEchoNotifier notifier) {
/* 17 */     super(notifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public void notifyReject(HttpExchange request, String cause) {
/* 22 */     LOGGER.warn(cause);
/* 23 */     echoN2S((IStringDumpable)new PjeHttpExchangeRequest(request, "REQUISIÇÃO REJEITADA: " + cause));
/*    */   }
/*    */ 
/*    */   
/*    */   public final synchronized boolean switchTo(IPjeEchoNotifier notifier) {
/* 28 */     if (getNotifier() == notifier) {
/* 29 */       return false;
/*    */     }
/* 31 */     close();
/* 32 */     setNotifier(notifier);
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeEchoNotifierSwitcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */