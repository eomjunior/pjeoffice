/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayBody
/*     */   extends AbstractContentBody
/*     */ {
/*     */   private final byte[] data;
/*     */   private final String filename;
/*     */   
/*     */   public ByteArrayBody(byte[] data, ContentType contentType, String filename) {
/*  58 */     super(contentType);
/*  59 */     this.data = (byte[])Args.notNull(data, "data");
/*  60 */     this.filename = filename;
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
/*     */   public ByteArrayBody(byte[] data, ContentType contentType) {
/*  72 */     this(data, contentType, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayBody(byte[] data, String filename) {
/*  82 */     this(data, ContentType.APPLICATION_OCTET_STREAM, filename);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  87 */     return this.filename;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  92 */     out.write(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharset() {
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 102 */     return this.data.length;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/ByteArrayBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */