/*    */ package org.apache.hc.client5.http.entity.mime;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringBody
/*    */   extends AbstractContentBody
/*    */ {
/*    */   private final byte[] content;
/*    */   
/*    */   public StringBody(String text, ContentType contentType) {
/* 56 */     super(contentType);
/* 57 */     Args.notNull(text, "Text");
/* 58 */     Charset charset = contentType.getCharset();
/* 59 */     this.content = text.getBytes((charset != null) ? charset : StandardCharsets.US_ASCII);
/*    */   }
/*    */   
/*    */   public Reader getReader() {
/* 63 */     Charset charset = getContentType().getCharset();
/* 64 */     return new InputStreamReader(new ByteArrayInputStream(this.content), (charset != null) ? charset : StandardCharsets.US_ASCII);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 71 */     Args.notNull(out, "Output stream");
/* 72 */     out.write(this.content);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 77 */     return this.content.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilename() {
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/StringBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */