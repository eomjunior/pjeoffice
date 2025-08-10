/*     */ package org.apache.hc.client5.http.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.io.entity.HttpEntityWrapper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecompressingEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final InputStreamFactory inputStreamFactory;
/*     */   private InputStream content;
/*     */   
/*     */   public DecompressingEntity(HttpEntity wrapped, InputStreamFactory inputStreamFactory) {
/*  65 */     super(wrapped);
/*  66 */     this.inputStreamFactory = inputStreamFactory;
/*     */   }
/*     */   
/*     */   private InputStream getDecompressingStream() throws IOException {
/*  70 */     return new LazyDecompressingInputStream(super.getContent(), this.inputStreamFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  75 */     if (isStreaming()) {
/*  76 */       if (this.content == null) {
/*  77 */         this.content = getDecompressingStream();
/*     */       }
/*  79 */       return this.content;
/*     */     } 
/*  81 */     return getDecompressingStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/*  86 */     Args.notNull(outStream, "Output stream");
/*  87 */     try (InputStream inStream = getContent()) {
/*  88 */       byte[] buffer = new byte[2048];
/*     */       int l;
/*  90 */       while ((l = inStream.read(buffer)) != -1) {
/*  91 */         outStream.write(buffer, 0, l);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 105 */     return -1L;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/DecompressingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */