/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.net.URIAuthority;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicClassicHttpRequest
/*     */   extends BasicHttpRequest
/*     */   implements ClassicHttpRequest
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private HttpEntity entity;
/*     */   
/*     */   public BasicClassicHttpRequest(String method, String scheme, URIAuthority authority, String path) {
/*  60 */     super(method, scheme, authority, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(String method, String path) {
/*  70 */     super(method, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(String method, HttpHost host, String path) {
/*  81 */     super(method, host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(String method, URI requestUri) {
/*  91 */     super(method, requestUri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(Method method, String path) {
/* 101 */     super(method, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(Method method, HttpHost host, String path) {
/* 112 */     super(method, host, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClassicHttpRequest(Method method, URI requestUri) {
/* 122 */     super(method, requestUri);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/* 127 */     return this.entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/* 132 */     this.entity = entity;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicClassicHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */