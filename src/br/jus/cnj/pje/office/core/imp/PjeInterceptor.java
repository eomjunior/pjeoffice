/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeEchoNotifier;
/*    */ import com.github.utils4j.IStringDumpable;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ enum PjeInterceptor
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 20 */   NOTHING
/*    */   {
/*    */ 
/*    */     
/*    */     public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {}
/*    */   },
/* 26 */   DEVMODE
/*    */   {
/*    */     public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 29 */       this.notifier.echoS2B(IStringDumpable.from(PjeAbstractRequestDumper.asString(new PjeHttpRequestDumper(request, entity))));
/*    */     } };
/*    */   
/*    */   PjeInterceptor() {
/* 33 */     this.notifier = PjeEchoNotifier.PRODUCTION;
/*    */   }
/*    */   public void setNotifier(IPjeEchoNotifier notifier) {
/* 36 */     this.notifier = (IPjeEchoNotifier)Args.requireNonNull(notifier, "notifier is null");
/*    */   }
/*    */   
/*    */   protected volatile IPjeEchoNotifier notifier;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */