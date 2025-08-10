/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.ICorsHeadersProvider;
/*     */ import com.github.utils4j.IRequestRejectNotifier;
/*     */ import com.sun.net.httpserver.Filter;
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
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
/*     */ public abstract class PreflightFilter
/*     */   extends CORSFilter
/*     */ {
/*     */   protected PreflightFilter(IRequestRejectNotifier rejector, ICorsHeadersProvider provider) {
/*  26 */     super(rejector, provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public String description() {
/*  31 */     return "Responde por requisições preflight do navegador, rejeitando ou aceitando segundo implementações sobrescritas de isAcceptable";
/*     */   }
/*     */   
/*     */   protected boolean isAcceptable(PreflightPack pck, StringBuilder whyNot) {
/*  35 */     return true;
/*     */   }
/*     */   
/*     */   protected void handleRejected(HttpExchange request, PreflightPack pck, String rejectCause) {
/*  39 */     this.notifier.notifyReject(request, "Rejeição preflight: " + rejectCause);
/*     */   }
/*     */   
/*     */   private void reply(HttpExchange request, PreflightPack pck) throws IOException {
/*  43 */     Headers responseHeaders = request.getResponseHeaders();
/*  44 */     StringBuilder whyNot = new StringBuilder();
/*  45 */     if (isAcceptable(pck, whyNot)) {
/*  46 */       accept(responseHeaders, pck.origin, true);
/*  47 */       request.sendResponseHeaders(204, -1L);
/*     */     } else {
/*  49 */       reject(responseHeaders);
/*  50 */       handleRejected(request, pck, whyNot.toString());
/*  51 */       request.sendResponseHeaders(403, -1L);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void doFilter(HttpExchange request, Filter.Chain chain) throws IOException {
/*  58 */     boolean isOptionMethod = "OPTIONS".equals(request.getRequestMethod());
/*     */ 
/*     */     
/*  61 */     if (!isOptionMethod) {
/*  62 */       chain.doFilter(request);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  68 */     Headers reqHeaders = request.getRequestHeaders();
/*     */     
/*  70 */     Optional<String> originHeader = Strings.optional(reqHeaders.getFirst("Origin"));
/*  71 */     boolean hasOriginHeader = originHeader.isPresent();
/*     */ 
/*     */     
/*  74 */     if (!hasOriginHeader) {
/*  75 */       chain.doFilter(request);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  81 */     Optional<String> acrmHeader = Strings.optional(reqHeaders.getFirst("Access-Control-Request-Method"));
/*  82 */     boolean hasAccessControlRequestMethodHeader = acrmHeader.isPresent();
/*     */ 
/*     */     
/*  85 */     if (!hasAccessControlRequestMethodHeader) {
/*  86 */       chain.doFilter(request);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  92 */     List<String> acrhHeader = Containers.nonNull(reqHeaders.get("Access-Control-Request-Headers"));
/*     */ 
/*     */     
/*  95 */     reply(request, new PreflightPack(originHeader
/*  96 */           .get(), acrmHeader
/*  97 */           .get(), acrhHeader));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class PreflightPack
/*     */   {
/*     */     public final String origin;
/*     */     public final String acrmHeader;
/*     */     public final List<String> acrhHeaders;
/*     */     
/*     */     PreflightPack(String origin, String acrmHeader, List<String> acrhHeaders) {
/* 108 */       this.origin = origin;
/* 109 */       this.acrmHeader = acrmHeader;
/* 110 */       this.acrhHeaders = acrhHeaders;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/PreflightFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */