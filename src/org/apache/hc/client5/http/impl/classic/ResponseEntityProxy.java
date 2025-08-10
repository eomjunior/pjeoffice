/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.impl.io.ChunkedInputStream;
/*     */ import org.apache.hc.core5.http.io.EofSensorInputStream;
/*     */ import org.apache.hc.core5.http.io.EofSensorWatcher;
/*     */ import org.apache.hc.core5.http.io.entity.HttpEntityWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ResponseEntityProxy
/*     */   extends HttpEntityWrapper
/*     */   implements EofSensorWatcher
/*     */ {
/*     */   private final ExecRuntime execRuntime;
/*     */   
/*     */   public static void enhance(ClassicHttpResponse response, ExecRuntime execRuntime) {
/*  52 */     HttpEntity entity = response.getEntity();
/*  53 */     if (entity != null && entity.isStreaming() && execRuntime != null) {
/*  54 */       response.setEntity((HttpEntity)new ResponseEntityProxy(entity, execRuntime));
/*     */     }
/*     */   }
/*     */   
/*     */   ResponseEntityProxy(HttpEntity entity, ExecRuntime execRuntime) {
/*  59 */     super(entity);
/*  60 */     this.execRuntime = execRuntime;
/*     */   }
/*     */   
/*     */   private void cleanup() throws IOException {
/*  64 */     if (this.execRuntime != null) {
/*  65 */       if (this.execRuntime.isEndpointConnected()) {
/*  66 */         this.execRuntime.disconnectEndpoint();
/*     */       }
/*  68 */       this.execRuntime.discardEndpoint();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void discardConnection() {
/*  73 */     if (this.execRuntime != null) {
/*  74 */       this.execRuntime.discardEndpoint();
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseConnection() {
/*  79 */     if (this.execRuntime != null) {
/*  80 */       this.execRuntime.releaseEndpoint();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  91 */     return (InputStream)new EofSensorInputStream(super.getContent(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/*     */     try {
/*  97 */       super.writeTo((outStream != null) ? outStream : NullOutputStream.INSTANCE);
/*  98 */       releaseConnection();
/*  99 */     } catch (IOException|RuntimeException ex) {
/* 100 */       discardConnection();
/* 101 */       throw ex;
/*     */     } finally {
/* 103 */       cleanup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eofDetected(InputStream wrapped) throws IOException {
/*     */     try {
/* 112 */       if (wrapped != null) {
/* 113 */         wrapped.close();
/*     */       }
/* 115 */       releaseConnection();
/* 116 */     } catch (IOException|RuntimeException ex) {
/* 117 */       discardConnection();
/* 118 */       throw ex;
/*     */     } finally {
/* 120 */       cleanup();
/*     */     } 
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamClosed(InputStream wrapped) throws IOException {
/*     */     try {
/* 128 */       boolean open = (this.execRuntime != null && this.execRuntime.isEndpointAcquired());
/*     */ 
/*     */       
/*     */       try {
/* 132 */         if (wrapped != null) {
/* 133 */           wrapped.close();
/*     */         }
/* 135 */         releaseConnection();
/* 136 */       } catch (SocketException ex) {
/* 137 */         if (open) {
/* 138 */           throw ex;
/*     */         }
/*     */       } 
/* 141 */     } catch (IOException|RuntimeException ex) {
/* 142 */       discardConnection();
/* 143 */       throw ex;
/*     */     } finally {
/* 145 */       cleanup();
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamAbort(InputStream wrapped) throws IOException {
/* 152 */     cleanup();
/* 153 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/*     */     try {
/* 159 */       InputStream underlyingStream = super.getContent();
/* 160 */       return () -> {
/*     */           Header[] footers;
/*     */           if (underlyingStream instanceof ChunkedInputStream) {
/*     */             ChunkedInputStream chunkedInputStream = (ChunkedInputStream)underlyingStream;
/*     */             footers = chunkedInputStream.getFooters();
/*     */           } else {
/*     */             footers = new Header[0];
/*     */           } 
/*     */           return Arrays.asList(footers);
/*     */         };
/* 170 */     } catch (IOException e) {
/* 171 */       throw new IllegalStateException("Unable to retrieve input stream", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 180 */       super.close();
/* 181 */       releaseConnection();
/* 182 */     } catch (IOException|RuntimeException ex) {
/* 183 */       discardConnection();
/* 184 */       throw ex;
/*     */     } finally {
/* 186 */       cleanup();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class NullOutputStream extends OutputStream {
/* 191 */     private static final NullOutputStream INSTANCE = new NullOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(int byteValue) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] buffer) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] buffer, int off, int len) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 225 */       return "NullOutputStream{}";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ResponseEntityProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */