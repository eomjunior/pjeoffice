/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IGetCodec;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.function.Supplier;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpPost;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.json.JSONObject;
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
/*     */ class PjeStdioClient
/*     */   extends PjeClientWrapper
/*     */ {
/*     */   PjeStdioClient(Supplier<IPjeJsonCodec> codec, Supplier<ICanceller> canceller) {
/*  52 */     super(new PJeJsonClient(codec, canceller));
/*     */   }
/*     */ 
/*     */   
/*     */   static class PjeStdioJsonCodec
/*     */     extends CloseablePjeSocketCodec<JSONObject>
/*     */     implements IPjeJsonCodec
/*     */   {
/*     */     private final Charset charset;
/*     */     
/*     */     private final Supplier<IGetCodec> codec;
/*     */     
/*     */     PjeStdioJsonCodec(Charset charset, Supplier<IGetCodec> codec) {
/*  65 */       this.charset = (Charset)Args.requireNonNull(charset, "charset is null");
/*  66 */       this.codec = (Supplier<IGetCodec>)Args.requireNonNull(codec, "codec is null");
/*     */     }
/*     */ 
/*     */     
/*     */     protected PjeTaskResponse doPost(IProvider<JSONObject> provider, IResultChecker checker) throws Exception {
/*  71 */       return new PjeStdioTaskResponse(((JSONObject)provider.get()).toString(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doGet(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/*  76 */       ((IGetCodec)this.codec.get()).get(provider, status);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doGet(IProvider<HttpGet> provider) throws Exception {
/*  81 */       return ((IGetCodec)this.codec.get()).get(provider);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PjeStdioWebCodec
/*     */     extends PjeWebCodec
/*     */   {
/*     */     private PjeStdioWebCodec(CloseableHttpClient client) {
/*  93 */       super(client);
/*     */     }
/*     */ 
/*     */     
/*     */     public PjeTaskResponse post(IProvider<HttpPost> provider, IResultChecker checkResults) throws Exception {
/*     */       try {
/*  99 */         super.post(provider, checkResults);
/* 100 */         return PjeClientProtocol.STDIO.success().apply("success");
/* 101 */       } catch (Exception e) {
/* 102 */         return PjeClientProtocol.STDIO.fail().apply(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public static class Builder
/*     */       extends PjeWebCodec.Builder<PjeStdioWebCodec>
/*     */     {
/*     */       protected PjeWebCodec newInstance(CloseableHttpClient client, final HttpClientConnectionManager manager) {
/* 111 */         return new PjeStdioClient.PjeStdioWebCodec(client)
/*     */           {
/*     */             public void doClose() throws IOException
/*     */             {
/* 115 */               manager.close();
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStdioClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */