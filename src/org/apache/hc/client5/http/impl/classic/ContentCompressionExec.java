/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.entity.BrotliDecompressingEntity;
/*     */ import org.apache.hc.client5.http.entity.BrotliInputStreamFactory;
/*     */ import org.apache.hc.client5.http.entity.DecompressingEntity;
/*     */ import org.apache.hc.client5.http.entity.DeflateInputStreamFactory;
/*     */ import org.apache.hc.client5.http.entity.GZIPInputStreamFactory;
/*     */ import org.apache.hc.client5.http.entity.InputStreamFactory;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.message.BasicHeaderValueParser;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.http.message.ParserCursor;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class ContentCompressionExec
/*     */   implements ExecChainHandler
/*     */ {
/*     */   private final Header acceptEncoding;
/*     */   private final Lookup<InputStreamFactory> decoderRegistry;
/*     */   private final boolean ignoreUnknown;
/*  83 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentCompressionExec(List<String> acceptEncoding, Lookup<InputStreamFactory> decoderRegistry, boolean ignoreUnknown) {
/*     */     String[] encoding;
/*  90 */     boolean brotliSupported = BrotliDecompressingEntity.isAvailable();
/*     */     
/*  92 */     if (brotliSupported) {
/*  93 */       encoding = new String[] { "gzip", "x-gzip", "deflate", "br" };
/*     */     } else {
/*  95 */       encoding = new String[] { "gzip", "x-gzip", "deflate" };
/*     */     } 
/*  97 */     this.acceptEncoding = MessageSupport.format("Accept-Encoding", (acceptEncoding != null) ? acceptEncoding
/*  98 */         .<String>toArray(EMPTY_STRING_ARRAY) : encoding);
/*     */ 
/*     */     
/* 101 */     if (decoderRegistry != null) {
/* 102 */       this.decoderRegistry = decoderRegistry;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 107 */       RegistryBuilder<InputStreamFactory> builder = RegistryBuilder.create().register("gzip", GZIPInputStreamFactory.getInstance()).register("x-gzip", GZIPInputStreamFactory.getInstance()).register("deflate", DeflateInputStreamFactory.getInstance());
/* 108 */       if (brotliSupported) {
/* 109 */         builder.register("br", BrotliInputStreamFactory.getInstance());
/*     */       }
/* 111 */       this.decoderRegistry = (Lookup<InputStreamFactory>)builder.build();
/*     */     } 
/*     */ 
/*     */     
/* 115 */     this.ignoreUnknown = ignoreUnknown;
/*     */   }
/*     */   
/*     */   public ContentCompressionExec(boolean ignoreUnknown) {
/* 119 */     this(null, null, ignoreUnknown);
/*     */   }
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
/*     */   public ContentCompressionExec() {
/* 132 */     this(null, null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/* 141 */     Args.notNull(request, "HTTP request");
/* 142 */     Args.notNull(scope, "Scope");
/*     */     
/* 144 */     HttpClientContext clientContext = scope.clientContext;
/* 145 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/*     */ 
/*     */     
/* 148 */     if (!request.containsHeader("Accept-Encoding") && requestConfig.isContentCompressionEnabled()) {
/* 149 */       request.addHeader(this.acceptEncoding);
/*     */     }
/*     */     
/* 152 */     ClassicHttpResponse response = chain.proceed(request, scope);
/*     */     
/* 154 */     HttpEntity entity = response.getEntity();
/*     */ 
/*     */     
/* 157 */     if (requestConfig.isContentCompressionEnabled() && entity != null && entity.getContentLength() != 0L) {
/* 158 */       String contentEncoding = entity.getContentEncoding();
/* 159 */       if (contentEncoding != null) {
/* 160 */         ParserCursor cursor = new ParserCursor(0, contentEncoding.length());
/* 161 */         HeaderElement[] codecs = BasicHeaderValueParser.INSTANCE.parseElements(contentEncoding, cursor);
/* 162 */         for (HeaderElement codec : codecs) {
/* 163 */           String codecname = codec.getName().toLowerCase(Locale.ROOT);
/* 164 */           InputStreamFactory decoderFactory = (InputStreamFactory)this.decoderRegistry.lookup(codecname);
/* 165 */           if (decoderFactory != null) {
/* 166 */             response.setEntity((HttpEntity)new DecompressingEntity(response.getEntity(), decoderFactory));
/* 167 */             response.removeHeaders("Content-Length");
/* 168 */             response.removeHeaders("Content-Encoding");
/* 169 */             response.removeHeaders("Content-MD5");
/*     */           }
/* 171 */           else if (!"identity".equals(codecname) && !this.ignoreUnknown) {
/* 172 */             throw new HttpException("Unsupported Content-Encoding: " + codec.getName());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return response;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ContentCompressionExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */