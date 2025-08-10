/*    */ package org.apache.hc.client5.http.fluent;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.apache.hc.core5.http.ContentType;
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
/*    */ public class Content
/*    */ {
/* 44 */   public static final Content NO_CONTENT = new Content(new byte[0], ContentType.DEFAULT_BINARY);
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   private final ContentType type;
/*    */   
/*    */   public Content(byte[] raw, ContentType type) {
/* 51 */     this.raw = raw;
/* 52 */     this.type = type;
/*    */   }
/*    */   
/*    */   public ContentType getType() {
/* 56 */     return this.type;
/*    */   }
/*    */   
/*    */   public byte[] asBytes() {
/* 60 */     return (byte[])this.raw.clone();
/*    */   }
/*    */   
/*    */   public String asString() {
/* 64 */     Charset charset = this.type.getCharset();
/* 65 */     if (charset == null) {
/* 66 */       charset = StandardCharsets.ISO_8859_1;
/*    */     }
/* 68 */     return asString(charset);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String asString(Charset charset) {
/* 75 */     return new String(this.raw, charset);
/*    */   }
/*    */   
/*    */   public InputStream asStream() {
/* 79 */     return new ByteArrayInputStream(this.raw);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return asString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Content.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */