/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.message.HttpResponseWrapper;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ public abstract class AbstractClassicServerExchangeHandler
/*     */   implements AsyncServerExchangeHandler
/*     */ {
/*     */   private final int initialBufferSize;
/*     */   private final Executor executor;
/*     */   private final AtomicReference<State> state;
/*     */   private final AtomicReference<Exception> exception;
/*     */   private volatile SharedInputBuffer inputBuffer;
/*     */   private volatile SharedOutputBuffer outputBuffer;
/*     */   
/*     */   private enum State
/*     */   {
/*  68 */     IDLE, ACTIVE, COMPLETED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractClassicServerExchangeHandler(int initialBufferSize, Executor executor) {
/*  79 */     this.initialBufferSize = Args.positive(initialBufferSize, "Initial buffer size");
/*  80 */     this.executor = (Executor)Args.notNull(executor, "Executor");
/*  81 */     this.exception = new AtomicReference<>();
/*  82 */     this.state = new AtomicReference<>(State.IDLE);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getException() {
/* 102 */     return this.exception.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handleRequest(HttpRequest request, EntityDetails entityDetails, final ResponseChannel responseChannel, final HttpContext context) throws HttpException, IOException {
/*     */     InputStream inputStream;
/* 111 */     final AtomicBoolean responseCommitted = new AtomicBoolean(false);
/*     */     
/* 113 */     final BasicHttpResponse response = new BasicHttpResponse(200);
/* 114 */     HttpResponseWrapper httpResponseWrapper = new HttpResponseWrapper((HttpResponse)basicHttpResponse)
/*     */       {
/*     */         private void ensureNotCommitted() {
/* 117 */           Asserts.check(!responseCommitted.get(), "Response already committed");
/*     */         }
/*     */ 
/*     */         
/*     */         public void addHeader(String name, Object value) {
/* 122 */           ensureNotCommitted();
/* 123 */           super.addHeader(name, value);
/*     */         }
/*     */ 
/*     */         
/*     */         public void setHeader(String name, Object value) {
/* 128 */           ensureNotCommitted();
/* 129 */           super.setHeader(name, value);
/*     */         }
/*     */ 
/*     */         
/*     */         public void setVersion(ProtocolVersion version) {
/* 134 */           ensureNotCommitted();
/* 135 */           super.setVersion(version);
/*     */         }
/*     */ 
/*     */         
/*     */         public void setCode(int code) {
/* 140 */           ensureNotCommitted();
/* 141 */           super.setCode(code);
/*     */         }
/*     */ 
/*     */         
/*     */         public void setReasonPhrase(String reason) {
/* 146 */           ensureNotCommitted();
/* 147 */           super.setReasonPhrase(reason);
/*     */         }
/*     */ 
/*     */         
/*     */         public void setLocale(Locale locale) {
/* 152 */           ensureNotCommitted();
/* 153 */           super.setLocale(locale);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 159 */     if (entityDetails != null) {
/* 160 */       this.inputBuffer = new SharedInputBuffer(this.initialBufferSize);
/* 161 */       inputStream = new ContentInputStream(this.inputBuffer);
/*     */     } else {
/* 163 */       inputStream = null;
/*     */     } 
/* 165 */     this.outputBuffer = new SharedOutputBuffer(this.initialBufferSize);
/*     */     
/* 167 */     OutputStream outputStream = new ContentOutputStream(this.outputBuffer)
/*     */       {
/*     */         private void triggerResponse() throws IOException {
/*     */           try {
/* 171 */             if (responseCommitted.compareAndSet(false, true)) {
/* 172 */               responseChannel.sendResponse(response, new EntityDetails()
/*     */                   {
/*     */                     public long getContentLength()
/*     */                     {
/* 176 */                       return -1L;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public String getContentType() {
/* 181 */                       Header h = response.getFirstHeader("Content-Type");
/* 182 */                       return (h != null) ? h.getValue() : null;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public String getContentEncoding() {
/* 187 */                       Header h = response.getFirstHeader("Content-Encoding");
/* 188 */                       return (h != null) ? h.getValue() : null;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public boolean isChunked() {
/* 193 */                       return false;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public Set<String> getTrailerNames() {
/* 198 */                       return null;
/*     */                     }
/*     */                   }, 
/*     */                   context);
/*     */             }
/* 203 */           } catch (HttpException ex) {
/* 204 */             throw new IOException(ex.getMessage(), ex);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 210 */           triggerResponse();
/* 211 */           super.close();
/*     */         }
/*     */ 
/*     */         
/*     */         public void write(byte[] b, int off, int len) throws IOException {
/* 216 */           triggerResponse();
/* 217 */           super.write(b, off, len);
/*     */         }
/*     */ 
/*     */         
/*     */         public void write(byte[] b) throws IOException {
/* 222 */           triggerResponse();
/* 223 */           super.write(b);
/*     */         }
/*     */ 
/*     */         
/*     */         public void write(int b) throws IOException {
/* 228 */           triggerResponse();
/* 229 */           super.write(b);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 234 */     if (this.state.compareAndSet(State.IDLE, State.ACTIVE)) {
/* 235 */       this.executor.execute(() -> {
/*     */             try {
/*     */               handle(request, inputStream, responseWrapper, outputStream, context);
/*     */               Closer.close(inputStream);
/*     */               outputStream.close();
/* 240 */             } catch (Exception ex) {
/*     */               this.exception.compareAndSet(null, ex);
/*     */               if (this.inputBuffer != null) {
/*     */                 this.inputBuffer.abort();
/*     */               }
/*     */               this.outputBuffer.abort();
/*     */             } finally {
/*     */               this.state.set(State.COMPLETED);
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 255 */     if (this.inputBuffer != null) {
/* 256 */       this.inputBuffer.updateCapacity(capacityChannel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 262 */     Asserts.notNull(this.inputBuffer, "Input buffer");
/* 263 */     this.inputBuffer.fill(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 268 */     Asserts.notNull(this.inputBuffer, "Input buffer");
/* 269 */     this.inputBuffer.markEndStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/* 274 */     Asserts.notNull(this.outputBuffer, "Output buffer");
/* 275 */     return this.outputBuffer.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void produce(DataStreamChannel channel) throws IOException {
/* 280 */     Asserts.notNull(this.outputBuffer, "Output buffer");
/* 281 */     this.outputBuffer.flush(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 286 */     this.exception.compareAndSet(null, cause);
/* 287 */     releaseResources();
/*     */   }
/*     */   
/*     */   public void releaseResources() {}
/*     */   
/*     */   protected abstract void handle(HttpRequest paramHttpRequest, InputStream paramInputStream, HttpResponse paramHttpResponse, OutputStream paramOutputStream, HttpContext paramHttpContext) throws IOException, HttpException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/AbstractClassicServerExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */