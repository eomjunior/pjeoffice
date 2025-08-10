/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.hc.core5.http.ContentType;
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
/*     */ public class InputStreamBody
/*     */   extends AbstractContentBody
/*     */ {
/*     */   private final InputStream in;
/*     */   private final String filename;
/*     */   private final long contentLength;
/*     */   
/*     */   public InputStreamBody(InputStream in, String filename) {
/*  51 */     this(in, ContentType.DEFAULT_BINARY, filename);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamBody(InputStream in, ContentType contentType, String filename) {
/*  58 */     this(in, contentType, filename, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamBody(InputStream in, ContentType contentType, String filename, long contentLength) {
/*  65 */     super(contentType);
/*  66 */     Args.notNull(in, "Input stream");
/*  67 */     this.in = in;
/*  68 */     this.filename = filename;
/*  69 */     this.contentLength = (contentLength >= 0L) ? contentLength : -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamBody(InputStream in, ContentType contentType) {
/*  76 */     this(in, contentType, null);
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() {
/*  80 */     return this.in;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  85 */     Args.notNull(out, "Output stream");
/*     */     try {
/*  87 */       byte[] tmp = new byte[4096];
/*     */       int l;
/*  89 */       while ((l = this.in.read(tmp)) != -1) {
/*  90 */         out.write(tmp, 0, l);
/*     */       }
/*  92 */       out.flush();
/*     */     } finally {
/*  94 */       this.in.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 100 */     return this.contentLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/* 105 */     return this.filename;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/InputStreamBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */