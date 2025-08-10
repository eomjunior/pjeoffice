/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeRequest;
/*    */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeResponse;
/*    */ import br.jus.cnj.pje.office.core.IPjeRequestHandler;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class PjeRequestHandler
/*    */   implements IPjeRequestHandler
/*    */ {
/*    */   private final String endpoint;
/*    */   
/*    */   protected PjeRequestHandler(String endpoint) {
/* 46 */     this.endpoint = Args.requireText(endpoint, "endpoint is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getEndPoint() {
/* 51 */     return this.endpoint;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void handle(HttpExchange exchange) throws IOException {
/*    */     try {
/* 57 */       process(new PjeHttpExchangeRequest(exchange), new PjeOneTimeWritingHttpExchangeResponse(new PjeHttpExchangeResponse(exchange)));
/*    */     } finally {
/* 59 */       exchange.close();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void process(IPjeHttpExchangeRequest paramIPjeHttpExchangeRequest, IPjeHttpExchangeResponse paramIPjeHttpExchangeResponse) throws IOException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */