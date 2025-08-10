/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class SerializableEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final Serializable serializable;
/*     */   
/*     */   public SerializableEntity(Serializable serializable, ContentType contentType, String contentEncoding) {
/*  62 */     super(contentType, contentEncoding);
/*  63 */     this.serializable = (Serializable)Args.notNull(serializable, "Source object");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializableEntity(Serializable serializable, ContentType contentType) {
/*  73 */     this(serializable, contentType, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final InputStream getContent() throws IOException, IllegalStateException {
/*  78 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/*  79 */     writeTo(buf);
/*  80 */     return new ByteArrayInputStream(buf.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/*  85 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRepeatable() {
/*  90 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStreaming() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeTo(OutputStream outStream) throws IOException {
/* 100 */     Args.notNull(outStream, "Output stream");
/* 101 */     ObjectOutputStream out = new ObjectOutputStream(outStream);
/* 102 */     out.writeObject(this.serializable);
/* 103 */     out.flush();
/*     */   }
/*     */   
/*     */   public final void close() throws IOException {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/SerializableEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */