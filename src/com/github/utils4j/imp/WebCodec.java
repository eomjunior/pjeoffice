/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.ISocketCodec;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpPost;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
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
/*     */ public abstract class WebCodec<R>
/*     */   implements ISocketCodec<HttpPost, R>
/*     */ {
/*     */   protected final CloseableHttpClient client;
/*     */   
/*     */   protected WebCodec(CloseableHttpClient client) {
/*  60 */     this.client = Args.<CloseableHttpClient>requireNonNull(client, "client is null");
/*     */   }
/*     */   
/*     */   protected boolean isSuccess(int code) {
/*  64 */     return ((code >= 200 && code < 300) || code == 304);
/*     */   }
/*     */   
/*     */   protected void handleSuccess(IResultChecker checkResults, String responseText, int httpCode) throws Exception {
/*  68 */     checkResults.handle(responseText, httpCode);
/*     */   }
/*     */   
/*     */   protected void handleFail(IResultChecker checkResults, String responseText, int httpCode) throws Exception {}
/*     */   
/*     */   protected final Exception launch(String message) {
/*  74 */     return launch(message, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public R post(IProvider<HttpPost> provider, IResultChecker checkResults) throws Exception {
/*     */     try {
/*  80 */       Optional<Exception> fail = (Optional<Exception>)this.client.execute((ClassicHttpRequest)provider.get(), response -> {
/*     */             try {
/*     */               int code = response.getCode();
/*     */ 
/*     */               
/*     */               String responseText = Strings.empty();
/*     */               
/*     */               HttpEntity entity = response.getEntity();
/*     */               
/*     */               if (entity != null) {
/*     */                 try {
/*     */                   responseText = EntityUtils.toString(entity, IConstants.DEFAULT_CHARSET);
/*  92 */                 } catch (ParseException|IOException e) {
/*     */                   throw launch("Falha na leitura de entity - HTTP Code: " + code, e);
/*     */                 } finally {
/*     */                   EntityUtils.consumeQuietly(entity);
/*     */                 } 
/*     */                 
/*     */                 if (responseText == null) {
/*     */                   responseText = Strings.empty();
/*     */                 }
/*     */               } 
/*     */               
/*     */               if (isSuccess(code)) {
/*     */                 handleSuccess(checkResults, responseText, code);
/*     */               } else {
/*     */                 handleFail(checkResults, responseText, code);
/*     */                 
/*     */                 throw launch("Servidor retornando - HTTP Code: " + code + " Content: \n" + responseText);
/*     */               } 
/*     */               
/*     */               return Optional.empty();
/* 112 */             } catch (Exception e) {
/*     */               return Optional.of(e);
/*     */             } 
/*     */           });
/*     */       
/* 117 */       if (fail.isPresent()) {
/* 118 */         throw (Exception)fail.get();
/*     */       }
/*     */       
/* 121 */       return success();
/*     */     }
/* 123 */     catch (CancellationException e) {
/* 124 */       throw launch("Os dados não foram enviados ao servidor. Operação cancelada!\n\tcause: " + Throwables.rootMessage(e));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void get(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/* 131 */     try (OutputStream output = status.onNewTry()) {
/*     */       
/* 133 */       Optional<Exception> fail = (Optional<Exception>)this.client.execute((ClassicHttpRequest)provider.get(), response -> {
/*     */             try {
/*     */               int code = response.getCode();
/*     */               
/*     */               HttpEntity entity = response.getEntity();
/*     */               
/*     */               if (entity == null) {
/*     */                 throw launch("Servidor não foi capaz de retornar dados. (entity is null) - HTTP Code: " + code);
/*     */               }
/*     */               
/*     */               try {
/*     */                 if (!isSuccess(code)) {
/*     */                   throw launch("Servidor retornando - HTTP Code: " + code);
/*     */                 }
/*     */                 
/*     */                 try {
/*     */                   long total = entity.getContentLength();
/*     */                   
/*     */                   InputStream input = entity.getContent();
/*     */                   
/*     */                   status.onStartDownload(total);
/*     */                   
/*     */                   byte[] buffer = new byte[131072];
/*     */                   
/*     */                   status.onStatus(total, 0L);
/*     */                   int written = 0;
/*     */                   int length;
/*     */                   while ((length = input.read(buffer)) > 0) {
/*     */                     output.write(buffer, 0, length);
/*     */                     status.onStatus(total, (written += length));
/*     */                   } 
/*     */                   status.onEndDownload();
/* 165 */                 } catch (InterruptedException e) {
/*     */                   
/*     */                   throw new InterruptedException("Download interrompido - HTTP Code: " + code + "\n\tcause: " + Throwables.rootMessage(e));
/* 168 */                 } catch (Exception e) {
/*     */                   throw launch("Falha durante o download do arquivo - HTTP Code: " + code, e);
/*     */                 } 
/*     */               } finally {
/*     */                 EntityUtils.consumeQuietly(entity);
/*     */               } 
/*     */ 
/*     */ 
/*     */               
/*     */               return Optional.empty();
/* 178 */             } catch (Exception e) {
/*     */               return Optional.of(e);
/*     */             } 
/*     */           });
/*     */       
/* 183 */       if (fail.isPresent()) {
/* 184 */         throw (Exception)fail.get();
/*     */       }
/* 186 */     } catch (Exception e) {
/* 187 */       status.onDownloadFail(e);
/* 188 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(IProvider<HttpGet> provider) throws Exception {
/* 195 */     Optional<Object> out = (Optional<Object>)this.client.execute((ClassicHttpRequest)provider.get(), response -> {
/*     */           try {
/*     */             int code = response.getCode();
/*     */ 
/*     */             
/*     */             HttpEntity entity = response.getEntity();
/*     */ 
/*     */             
/*     */             if (entity == null) {
/*     */               throw launch("Servidor não foi capaz de retornar dados. (entity is null) - HTTP Code: " + code);
/*     */             }
/*     */ 
/*     */             
/*     */             try {
/*     */               if (!isSuccess(code)) {
/*     */                 throw launch("Servidor retornando - HTTP Code: " + code);
/*     */               }
/*     */ 
/*     */               
/*     */               InputStream input = entity.getContent();
/*     */ 
/*     */               
/*     */               if (input == null) {
/*     */                 return Optional.of(Strings.empty());
/*     */               }
/* 220 */             } catch (InterruptedException e) {
/*     */               
/*     */               throw new InterruptedException("Requisição GET interrompida - HTTP Code: " + code + "\n\tcause: " + Throwables.rootMessage(e));
/* 223 */             } catch (Exception e) {
/*     */               
/*     */               throw launch("Falha durante o requisição GET - HTTP Code: " + code, e);
/*     */             } finally {
/*     */               EntityUtils.consumeQuietly(entity);
/*     */             } 
/* 229 */           } catch (Exception e) {
/*     */             return Optional.of(e);
/*     */           } 
/*     */         });
/*     */     
/* 234 */     Object response = out.orElseGet(Strings::empty);
/*     */     
/* 236 */     if (response instanceof Exception) {
/* 237 */       throw (Exception)response;
/*     */     }
/*     */     
/* 240 */     return response.toString();
/*     */   }
/*     */   
/*     */   protected abstract Exception launch(String paramString, Exception paramException);
/*     */   
/*     */   protected abstract R success();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/WebCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */