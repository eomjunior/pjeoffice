/*    */ package br.jus.cnj.pje.office.core.imp.security;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeHttpVerbs;
/*    */ import com.github.utils4j.IRequestRejectNotifier;
/*    */ import com.github.utils4j.echo.imp.Networks;
/*    */ import com.github.utils4j.imp.RejectableFilter;
/*    */ import com.github.utils4j.imp.Streams;
/*    */ import com.sun.net.httpserver.Filter;
/*    */ import com.sun.net.httpserver.Headers;
/*    */ import com.sun.net.httpserver.HttpExchange;
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PjeComplianceFilter
/*    */   extends RejectableFilter
/*    */ {
/*    */   public PjeComplianceFilter(IRequestRejectNotifier notifier) {
/* 22 */     super(notifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public String description() {
/* 27 */     return "Rejeita ataques remotos, dns rebinding e verbos HTTP irrelevantes ao assinador";
/*    */   }
/*    */   
/*    */   private boolean isRemoteAttack(InetAddress ip) {
/* 31 */     return !ip.isLoopbackAddress();
/*    */   }
/*    */   
/*    */   protected boolean acceptRequest(HttpExchange request) {
/* 35 */     return IPjeHttpVerbs.isComplianceable(request.getRequestMethod(), request.getRequestURI().getPath());
/*    */   }
/*    */   
/*    */   private boolean isDnsRebindingAttack(Headers reqHeaders) {
/* 39 */     return !Networks.isLoopbackEndpoint(reqHeaders.getFirst("Host"));
/*    */   }
/*    */ 
/*    */   
/*    */   public final void doFilter(HttpExchange request, Filter.Chain chain) throws IOException {
/* 44 */     StringBuilder whyNot = new StringBuilder();
/* 45 */     if (checkCompliance(request, whyNot)) {
/* 46 */       chain.doFilter(wrapInput(request));
/*    */     } else {
/* 48 */       this.notifier.notifyReject(request, "Rejeição compliance: " + whyNot.toString());
/* 49 */       request.sendResponseHeaders(401, -1L);
/* 50 */       request.close();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static HttpExchange wrapInput(HttpExchange request) throws IOException {
/* 55 */     request.setStreams(Streams.stringByteArray(request.getRequestBody()), null);
/* 56 */     return request;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final boolean checkCompliance(HttpExchange request, StringBuilder whyNot) {
/* 61 */     InetAddress remoteIp = request.getRemoteAddress().getAddress();
/* 62 */     if (isRemoteAttack(remoteIp)) {
/* 63 */       whyNot.append("Tentativa de ataque por requisição remota vinda do ip: " + remoteIp);
/* 64 */       return false;
/*    */     } 
/*    */     
/* 67 */     Headers reqHeaders = request.getRequestHeaders();
/*    */     
/* 69 */     if (isDnsRebindingAttack(reqHeaders)) {
/* 70 */       whyNot.append("Tentativa de ataque por DNS rebinding");
/* 71 */       return false;
/*    */     } 
/*    */     
/* 74 */     if (!acceptRequest(request)) {
/* 75 */       whyNot.append("O verbo utilizado para esta requisição não é aceito");
/* 76 */       return false;
/*    */     } 
/*    */     
/* 79 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/PjeComplianceFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */