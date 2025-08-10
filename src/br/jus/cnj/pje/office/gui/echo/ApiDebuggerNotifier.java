/*    */ package br.jus.cnj.pje.office.gui.echo;
/*    */ 
/*    */ import com.github.utils4j.echo.imp.EchoNotifierWindow;
/*    */ import com.github.utils4j.gui.imp.SimpleFrame;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class ApiDebuggerNotifier
/*    */   extends EchoNotifierWindow
/*    */   implements IDebuggerNotifier
/*    */ {
/*    */   private final String endpoint;
/*    */   
/*    */   public ApiDebuggerNotifier(String endpoint) {
/* 39 */     super("Depuração das requisições");
/* 40 */     this.endpoint = (String)Args.requireNonNull(endpoint, "endpoint is null");
/*    */   }
/*    */   
/*    */   private IDebuggerEchoNotifier frame() {
/* 44 */     return (IDebuggerEchoNotifier)this.frame;
/*    */   }
/*    */ 
/*    */   
/*    */   public void echoS2B(String message) {
/* 49 */     show();
/* 50 */     frame().echoS2B(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public void echoB2S(String message) {
/* 55 */     show();
/* 56 */     frame().echoB2S(message);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final SimpleFrame createFrame() {
/* 61 */     return new DebuggerFrame(this.title, this.headerFormat, this.endpoint);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/echo/ApiDebuggerNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */