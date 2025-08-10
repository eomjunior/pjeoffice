/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IDownloader;
/*     */ import com.github.utils4j.IGetCodec;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.subjects.BehaviorSubject;
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
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
/*     */ public class Downloader
/*     */   implements IDownloader
/*     */ {
/*     */   private final String rootUri;
/*     */   private final IGetCodec codec;
/*  51 */   private final BehaviorSubject<HttpUriRequest> status = BehaviorSubject.create();
/*     */   
/*     */   public Downloader(String rootUri, CloseableHttpClient client) {
/*  54 */     this.rootUri = ((String)Args.<String>requireNonNull(rootUri, "root Uri is null")).trim();
/*  55 */     this.codec = (IGetCodec)new DownloaderCodec(client);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String match(String path) {
/*  60 */     return Strings.encodeHtmlSpace(this.rootUri + Strings.trim(path));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<HttpUriRequest> newRequest() {
/*  65 */     return (Observable<HttpUriRequest>)this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void download(String uri, IDownloadStatus status) throws IOException, InterruptedException {
/*  70 */     Args.requireNonNull(uri, "uri is null");
/*  71 */     Args.requireNonNull(status, "status is null");
/*  72 */     String fullUrl = match(uri);
/*     */     try {
/*  74 */       this.codec.get(() -> createRequest(fullUrl), status);
/*  75 */     } catch (InterruptedException e) {
/*  76 */       throw e;
/*  77 */     } catch (IOException e) {
/*  78 */       checkInterrupt(e);
/*  79 */       throw e;
/*  80 */     } catch (Exception e) {
/*  81 */       checkInterrupt(e);
/*  82 */       throw new IOException("Unabled to download from url: " + fullUrl, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void checkInterrupt(Exception e) throws InterruptedException {
/*  87 */     if (Thread.currentThread().isInterrupted() || "Request aborted".equals(e.getMessage())) {
/*  88 */       throw new InterruptedException("Download cancelado! \n\tcause: " + Throwables.rootMessage(e));
/*     */     }
/*     */   }
/*     */   
/*     */   private HttpGet createRequest(String fullUrl) throws URISyntaxException {
/*  93 */     HttpGet r = new HttpGet(fullUrl);
/*     */     
/*  95 */     r.setHeader("Accept-Encoding", "gzip,deflate");
/*  96 */     r.setHeader("Cache-Control", "no-cache");
/*  97 */     this.status.onNext(r);
/*  98 */     return r;
/*     */   }
/*     */   
/*     */   private static class DownloaderCodec
/*     */     extends WebCodec<Void> {
/*     */     public DownloaderCodec(CloseableHttpClient client) {
/* 104 */       super(client);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Exception launch(String message, Exception e) {
/* 109 */       return new IOException(message, e);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Void success() {
/* 114 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Downloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */