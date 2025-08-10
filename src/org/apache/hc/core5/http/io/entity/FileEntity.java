/*    */ package org.apache.hc.core5.http.io.entity;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class FileEntity
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final File file;
/*    */   
/*    */   public FileEntity(File file, ContentType contentType, String contentEncoding) {
/* 51 */     super(contentType, contentEncoding);
/* 52 */     this.file = (File)Args.notNull(file, "File");
/*    */   }
/*    */   
/*    */   public FileEntity(File file, ContentType contentType) {
/* 56 */     super(contentType, (String)null);
/* 57 */     this.file = (File)Args.notNull(file, "File");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isRepeatable() {
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getContentLength() {
/* 67 */     return this.file.length();
/*    */   }
/*    */ 
/*    */   
/*    */   public final InputStream getContent() throws IOException {
/* 72 */     return new FileInputStream(this.file);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isStreaming() {
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public final void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/FileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */