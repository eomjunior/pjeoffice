/*     */ package br.jus.cnj.pje.office.core.imp.security;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import br.jus.cnj.pje.office.core.imp.security.bypass.IPjeRequestPathBypasser;
/*     */ import br.jus.cnj.pje.office.core.imp.security.bypass.PjeRequestPathBypassers;
/*     */ import com.github.signer4j.gui.alert.PermissionDeniedAlert;
/*     */ import com.github.utils4j.IRequestRejectNotifier;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.CORSFilter;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
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
/*     */ public class PjePreviousPreflightCheckingFilter
/*     */   extends CORSFilter
/*     */ {
/*     */   protected final IPjeRequestPathBypasser bypasser;
/*     */   
/*     */   public PjePreviousPreflightCheckingFilter(IRequestRejectNotifier notifier) {
/*  35 */     this(notifier, (IPjeRequestPathBypasser)PjeRequestPathBypassers.DEFAULT);
/*     */   }
/*     */   
/*     */   public PjePreviousPreflightCheckingFilter(IRequestRejectNotifier notifier, IPjeRequestPathBypasser bypasser) {
/*  39 */     super(notifier, PjeHeaders.RESPONSE);
/*  40 */     this.bypasser = (IPjeRequestPathBypasser)Args.requireNonNull(bypasser, "bypasser is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final String description() {
/*  45 */     return "Garante que toda requisição PUT (PREFLIGHTABLE) seja validada por um preflight anterior";
/*     */   }
/*     */   
/*     */   private boolean isBypassableRequest(String requestPath) {
/*  49 */     return this.bypasser.isBypassable(Strings.trim(requestPath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean acceptGetMethod(HttpExchange request, StringBuilder whyNot) {
/*  60 */     boolean isSafe = PjeSecurity.GRANTOR.isSafe();
/*  61 */     if (isSafe)
/*     */     {
/*  63 */       whyNot.append("Assinador operando em modo seguro, portanto rejeitará requisições GET não idempotentes/bypassadas. ");
/*     */     }
/*  65 */     return !isSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void doFilter(HttpExchange request, Filter.Chain chain) throws IOException {
/*  71 */     Headers reqHeaders = request.getRequestHeaders();
/*  72 */     Headers respHeaders = request.getResponseHeaders();
/*     */     
/*  74 */     Optional<String> originHeader = Strings.optional(reqHeaders.getFirst("Origin"));
/*     */     
/*  76 */     StringBuilder whyNot = new StringBuilder();
/*  77 */     if (thereWasAPreviousPreflight(request, originHeader, reqHeaders, whyNot)) {
/*  78 */       originHeader.ifPresent(origin -> accept(respHeaders, origin));
/*  79 */       chain.doFilter(request);
/*     */     } else {
/*  81 */       this.notifier.notifyReject(request, "Rejeição previous preflight: " + whyNot.toString());
/*  82 */       reject(respHeaders);
/*  83 */       request.sendResponseHeaders(403, -1L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean thereWasAPreviousPreflight(HttpExchange request, Optional<String> origin, Headers reqHeaders, StringBuilder whyNot) {
/*  89 */     String method = request.getRequestMethod();
/*     */     
/*  91 */     boolean isOptionRequest = "OPTIONS".equals(method);
/*     */     
/*  93 */     if (isOptionRequest) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       whyNot.append("Ignorada requisição com verbo OPTIONS por NÃO se tratar de requisição preflight. Verifique se a ordem dos filtros não foi equivocadamente alterada.");
/*     */       
/* 104 */       return false;
/*     */     } 
/*     */     
/* 107 */     String requestPath = request.getRequestURI().getPath();
/*     */     
/* 109 */     if (isBypassableRequest(requestPath))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 114 */       return true;
/*     */     }
/*     */     
/* 117 */     boolean isGetRequest = "GET".equals(method);
/*     */     
/* 119 */     if (isGetRequest) {
/*     */       
/* 121 */       if (!acceptGetMethod(request, whyNot)) {
/*     */ 
/*     */ 
/*     */         
/* 125 */         PermissionDeniedAlert.showInfo("A origem da requisição é desconhecida e foi rejeitada por segurança.");
/*     */         
/* 127 */         whyNot.append("O filtro rejeitou o verbo GET porque não é possível admitir que houve uma requisição preflight anterior que garanta bloqueio de requisição posterior. O assinador só aceitará este verbo se for colocado explicitamente em modo inseguro.");
/*     */ 
/*     */ 
/*     */         
/* 131 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 140 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 145 */     boolean isPreflightableRequest = "PUT".equals(method);
/*     */     
/* 147 */     if (!isPreflightableRequest) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       whyNot.append("Ignorada requisição com verbo '" + method + "'. Verifique se a ordem dos filtros não foi equivocadamente alterada.");
/*     */       
/* 154 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 158 */     boolean hasOriginHeader = origin.isPresent();
/*     */     
/* 160 */     if (!hasOriginHeader) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       whyNot.append("Requisição PUT ignorada porque o cabeçalho ORIGIN está ausente.");
/* 167 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     boolean hasForcePreflightHeader = Strings.isTrue(reqHeaders.getFirst("Force-Preflight"));
/*     */     
/* 176 */     if (!hasForcePreflightHeader) {
/*     */       
/* 178 */       whyNot.append("Requisição PUT ignorada porque o cabeçalho Force-Preflight está ausente.");
/* 179 */       return false;
/*     */     } 
/*     */     
/* 182 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/security/PjePreviousPreflightCheckingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */