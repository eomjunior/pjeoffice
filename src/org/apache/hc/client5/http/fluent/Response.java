/*     */ package org.apache.hc.client5.http.fluent;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.client5.http.ClientProtocolException;
/*     */ import org.apache.hc.client5.http.HttpResponseException;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.io.support.ClassicResponseBuilder;
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
/*     */ public class Response
/*     */ {
/*     */   private final ClassicHttpResponse response;
/*     */   private boolean consumed;
/*     */   
/*     */   Response(ClassicHttpResponse response) {
/*  59 */     this.response = response;
/*     */   }
/*     */   
/*     */   private void assertNotConsumed() {
/*  63 */     if (this.consumed) {
/*  64 */       throw new IllegalStateException("Response content has been already consumed");
/*     */     }
/*     */   }
/*     */   
/*     */   private void dispose() throws IOException {
/*  69 */     if (this.consumed) {
/*     */       return;
/*     */     }
/*     */     try {
/*  73 */       HttpEntity entity = this.response.getEntity();
/*  74 */       if (entity != null) {
/*  75 */         InputStream content = entity.getContent();
/*  76 */         if (content != null) {
/*  77 */           content.close();
/*     */         }
/*     */       } 
/*     */     } finally {
/*  81 */       this.consumed = true;
/*  82 */       this.response.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void discardContent() {
/*     */     try {
/*  91 */       dispose();
/*  92 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T handleResponse(HttpClientResponseHandler<T> handler) throws IOException {
/* 100 */     assertNotConsumed();
/*     */     try {
/* 102 */       return (T)handler.handleResponse(this.response);
/* 103 */     } catch (HttpException ex) {
/* 104 */       throw new ClientProtocolException(ex);
/*     */     } finally {
/* 106 */       dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Content returnContent() throws IOException {
/* 111 */     return handleResponse((HttpClientResponseHandler<Content>)new ContentResponseHandler());
/*     */   }
/*     */   
/*     */   public HttpResponse returnResponse() throws IOException {
/* 115 */     assertNotConsumed();
/*     */     try {
/* 117 */       HttpEntity entity = this.response.getEntity();
/* 118 */       return (HttpResponse)ClassicResponseBuilder.copy(this.response)
/* 119 */         .setEntity((entity != null) ? (HttpEntity)new ByteArrayEntity(
/*     */             
/* 121 */             EntityUtils.toByteArray(entity), 
/* 122 */             ContentType.parse(entity.getContentType())) : null)
/*     */         
/* 124 */         .build();
/*     */     } finally {
/* 126 */       this.consumed = true;
/* 127 */       this.response.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveContent(File file) throws IOException {
/* 132 */     assertNotConsumed();
/*     */     try {
/* 134 */       int status = this.response.getCode();
/* 135 */       if (status >= 300) {
/* 136 */         throw new HttpResponseException(status, this.response.getReasonPhrase());
/*     */       }
/* 138 */       try (FileOutputStream out = new FileOutputStream(file)) {
/* 139 */         HttpEntity entity = this.response.getEntity();
/* 140 */         if (entity != null) {
/* 141 */           entity.writeTo(out);
/*     */         }
/*     */       } 
/*     */     } finally {
/* 145 */       this.consumed = true;
/* 146 */       this.response.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */