/*    */ package org.apache.hc.core5.http.io.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class PathEntity
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final Path path;
/*    */   
/*    */   public PathEntity(Path path, ContentType contentType, String contentEncoding) {
/* 49 */     super(contentType, contentEncoding);
/* 50 */     this.path = (Path)Args.notNull(path, "Path");
/*    */   }
/*    */   
/*    */   public PathEntity(Path path, ContentType contentType) {
/* 54 */     super(contentType, (String)null);
/* 55 */     this.path = (Path)Args.notNull(path, "Path");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isRepeatable() {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getContentLength() {
/*    */     try {
/* 66 */       return Files.size(this.path);
/* 67 */     } catch (IOException e) {
/* 68 */       throw new IllegalStateException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final InputStream getContent() throws IOException {
/* 74 */     return Files.newInputStream(this.path, new java.nio.file.OpenOption[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isStreaming() {
/* 79 */     return false;
/*    */   }
/*    */   
/*    */   public final void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/PathEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */