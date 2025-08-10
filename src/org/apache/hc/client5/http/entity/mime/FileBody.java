/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
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
/*     */ public class FileBody
/*     */   extends AbstractContentBody
/*     */ {
/*     */   private final File file;
/*     */   private final String filename;
/*     */   
/*     */   public FileBody(File file) {
/*  52 */     this(file, ContentType.DEFAULT_BINARY, (file != null) ? file.getName() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBody(File file, ContentType contentType, String filename) {
/*  59 */     super(contentType);
/*  60 */     Args.notNull(file, "File");
/*  61 */     this.file = file;
/*  62 */     this.filename = (filename == null) ? file.getName() : filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBody(File file, ContentType contentType) {
/*  69 */     this(file, contentType, (file != null) ? file.getName() : null);
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  73 */     return new FileInputStream(this.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  78 */     Args.notNull(out, "Output stream");
/*  79 */     try (InputStream in = new FileInputStream(this.file)) {
/*  80 */       byte[] tmp = new byte[4096];
/*     */       int l;
/*  82 */       while ((l = in.read(tmp)) != -1) {
/*  83 */         out.write(tmp, 0, l);
/*     */       }
/*  85 */       out.flush();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  91 */     return this.file.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  96 */     return this.filename;
/*     */   }
/*     */   
/*     */   public File getFile() {
/* 100 */     return this.file;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/FileBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */