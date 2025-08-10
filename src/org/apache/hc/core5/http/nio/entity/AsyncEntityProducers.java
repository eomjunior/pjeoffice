/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.StreamChannel;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*     */ public final class AsyncEntityProducers
/*     */ {
/*     */   public static AsyncEntityProducer create(String content, ContentType contentType) {
/*  64 */     return new BasicAsyncEntityProducer(content, contentType);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(String content, Charset charset) {
/*  68 */     return new BasicAsyncEntityProducer(content, ContentType.TEXT_PLAIN.withCharset(charset));
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(String content) {
/*  72 */     return new BasicAsyncEntityProducer(content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(byte[] content, ContentType contentType) {
/*  76 */     return new BasicAsyncEntityProducer(content, contentType);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(File content, ContentType contentType) {
/*  80 */     return new FileEntityProducer(content, contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer createUrlEncoded(Iterable<? extends NameValuePair> parameters, Charset charset) {
/*  86 */     ContentType contentType = (charset != null) ? ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset) : ContentType.APPLICATION_FORM_URLENCODED;
/*     */     
/*  88 */     return create(WWWFormCodec.format(parameters, contentType.getCharset()), contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer createBinary(final Callback<StreamChannel<ByteBuffer>> callback, ContentType contentType) {
/*  94 */     return new AbstractBinAsyncEntityProducer(0, contentType)
/*     */       {
/*     */         protected int availableData()
/*     */         {
/*  98 */           return Integer.MAX_VALUE;
/*     */         }
/*     */ 
/*     */         
/*     */         protected void produceData(StreamChannel<ByteBuffer> channel) throws IOException {
/* 103 */           callback.execute(channel);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isRepeatable() {
/* 108 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void failed(Exception cause) {}
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer createText(final Callback<StreamChannel<CharBuffer>> callback, ContentType contentType) {
/* 121 */     return new AbstractCharAsyncEntityProducer(4096, 2048, contentType)
/*     */       {
/*     */         protected int availableData()
/*     */         {
/* 125 */           return Integer.MAX_VALUE;
/*     */         }
/*     */ 
/*     */         
/*     */         protected void produceData(StreamChannel<CharBuffer> channel) throws IOException {
/* 130 */           callback.execute(channel);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isRepeatable() {
/* 135 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void failed(Exception cause) {}
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer withTrailers(AsyncEntityProducer entity, Header... trailers) {
/* 146 */     return new AsyncEntityProducerWrapper(entity)
/*     */       {
/*     */         
/*     */         public boolean isChunked()
/*     */         {
/* 151 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public long getContentLength() {
/* 156 */           return -1L;
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<String> getTrailerNames() {
/* 161 */           Set<String> names = new LinkedHashSet<>();
/* 162 */           for (Header trailer : trailers) {
/* 163 */             names.add(trailer.getName());
/*     */           }
/* 165 */           return names;
/*     */         }
/*     */ 
/*     */         
/*     */         public void produce(final DataStreamChannel channel) throws IOException {
/* 170 */           super.produce(new DataStreamChannel()
/*     */               {
/*     */                 public void requestOutput()
/*     */                 {
/* 174 */                   channel.requestOutput();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int write(ByteBuffer src) throws IOException {
/* 179 */                   return channel.write(src);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void endStream(List<? extends Header> p) throws IOException {
/*     */                   List<Header> allTrailers;
/* 185 */                   if (p != null && !p.isEmpty()) {
/* 186 */                     allTrailers = new ArrayList<>(p);
/* 187 */                     allTrailers.addAll(Arrays.asList(trailers));
/*     */                   } else {
/* 189 */                     allTrailers = Arrays.asList(trailers);
/*     */                   } 
/* 191 */                   channel.endStream(allTrailers);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void endStream() throws IOException {
/* 196 */                   channel.endStream();
/*     */                 }
/*     */               });
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer create(String content, ContentType contentType, Header... trailers) {
/* 205 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(String content, Charset charset, Header... trailers) {
/* 209 */     return withTrailers(create(content, charset), trailers);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(String content, Header... trailers) {
/* 213 */     return withTrailers(create(content), trailers);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(byte[] content, ContentType contentType, Header... trailers) {
/* 217 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */   
/*     */   public static AsyncEntityProducer create(File content, ContentType contentType, Header... trailers) {
/* 221 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer create(Path content, ContentType contentType, Header... trailers) throws IOException {
/* 228 */     return withTrailers(new PathEntityProducer(content, contentType, new OpenOption[] { StandardOpenOption.READ }), trailers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer create(Path content, ContentType contentType, OpenOption... options) throws IOException {
/* 235 */     return new PathEntityProducer(content, contentType, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer createBinary(Callback<StreamChannel<ByteBuffer>> callback, ContentType contentType, Header... trailers) {
/* 242 */     return withTrailers(createBinary(callback, contentType), trailers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AsyncEntityProducer createText(Callback<StreamChannel<CharBuffer>> callback, ContentType contentType, Header... trailers) {
/* 249 */     return withTrailers(createText(callback, contentType), trailers);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AsyncEntityProducers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */