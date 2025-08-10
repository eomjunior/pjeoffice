/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeHttpExchangeResponse;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.ContentTypes;
/*     */ import com.sun.net.httpserver.Headers;
/*     */ import com.sun.net.httpserver.HttpExchange;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
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
/*     */ public class PjeHttpExchangeResponse
/*     */   implements IPjeHttpExchangeResponse
/*     */ {
/*     */   private final HttpExchange response;
/*     */   
/*     */   public PjeHttpExchangeResponse(HttpExchange response) {
/*  51 */     this.response = (HttpExchange)Args.requireNonNull(response, "response is null");
/*     */   }
/*     */   
/*     */   private void setContentType(String contentType) {
/*  55 */     Args.requireNonNull(contentType, "contentType is null");
/*  56 */     this.response.getResponseHeaders().set("Content-Type", contentType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] data) throws IOException {
/*  61 */     Args.requireNonNull(data, "data is null");
/*  62 */     Headers responseHeaders = this.response.getResponseHeaders();
/*  63 */     responseHeaders.set("Cache-Control", "no-cache, no-store, must-revalidate");
/*  64 */     responseHeaders.set("Pragma", "no-cache");
/*  65 */     responseHeaders.set("Expires", "0");
/*  66 */     this.response.sendResponseHeaders(200, data.length);
/*  67 */     this.response.getResponseBody().write(data);
/*  68 */     flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  73 */     this.response.getResponseBody().flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] data, String contentType) throws IOException {
/*  78 */     setContentType(contentType);
/*  79 */     write(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void notFound() throws IOException {
/*  84 */     this.response.sendResponseHeaders(404, -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fail(int code) throws IOException {
/*  89 */     this.response.sendResponseHeaders(code, -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void success() throws IOException {
/*  94 */     this.response.sendResponseHeaders(200, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(File file) throws IOException {
/* 102 */     Args.requireNonNull(file, "file is null");
/* 103 */     write(Files.readAllBytes(file.toPath()), ContentTypes.fromExtension(file.getName()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeHttpExchangeResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */