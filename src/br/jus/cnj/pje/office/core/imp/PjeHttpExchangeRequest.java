/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeRequest;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PjeHttpExchangeRequest
/*     */   extends PjeUriRequest
/*     */   implements IPjeHttpExchangeRequest
/*     */ {
/*     */   private final HttpExchange request;
/*     */   private final String warning;
/*     */   
/*     */   public PjeHttpExchangeRequest(HttpExchange request) {
/*  54 */     this(request, Strings.empty());
/*     */   }
/*     */   
/*     */   public PjeHttpExchangeRequest(HttpExchange request, String warning) {
/*  58 */     super(request.getRequestURI());
/*  59 */     this.request = request;
/*  60 */     this.warning = warning;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getParameterR() {
/*  65 */     return isPreflightable() ? Strings.optional(this.request.getRequestBody().toString()) : super.getParameterR();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPreflightable() {
/*  70 */     return "PUT".equals(this.request.getRequestMethod());
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getOrigin() {
/*  75 */     return Strings.optional(this.request.getRequestHeaders().getFirst("Origin"));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getParameter(String key) {
/*  80 */     return super.getParameter(key);
/*     */   }
/*     */   
/*     */   final void forEachHeaders(BiConsumer<String, List<String>> biconsumer) {
/*  84 */     this.request.getRequestHeaders().forEach(biconsumer);
/*     */   }
/*     */   
/*     */   final URI toURI() {
/*  88 */     return this.request.getRequestURI();
/*     */   }
/*     */   
/*     */   final String getMethod() {
/*  92 */     return this.request.getRequestMethod();
/*     */   }
/*     */   
/*     */   final InputStream body() {
/*  96 */     return this.request.getRequestBody();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String dump() {
/* 101 */     return PjeAbstractRequestDumper.asString(new PjeHttpExchangeRequestDumper(this, this.warning));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeHttpExchangeRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */