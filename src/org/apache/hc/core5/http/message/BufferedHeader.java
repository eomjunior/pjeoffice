/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ import org.apache.hc.core5.util.Tokenizer;
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
/*     */ 
/*     */ public class BufferedHeader
/*     */   implements FormattedHeader, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2768352615787625448L;
/*     */   private final String name;
/*     */   private final CharArrayBuffer buffer;
/*     */   private final int valuePos;
/*     */   
/*     */   public static BufferedHeader create(CharArrayBuffer buffer) {
/*     */     try {
/*  68 */       return new BufferedHeader(buffer);
/*  69 */     } catch (ParseException ex) {
/*  70 */       throw new IllegalArgumentException(ex.getMessage());
/*     */     } 
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
/*     */   
/*     */   public BufferedHeader(CharArrayBuffer buffer) throws ParseException {
/*  84 */     this(buffer, true);
/*     */   }
/*     */ 
/*     */   
/*     */   BufferedHeader(CharArrayBuffer buffer, boolean strict) throws ParseException {
/*  89 */     Args.notNull(buffer, "Char array buffer");
/*  90 */     int colon = buffer.indexOf(58);
/*  91 */     if (colon <= 0) {
/*  92 */       throw new ParseException("Invalid header", buffer, 0, buffer.length());
/*     */     }
/*  94 */     if (strict && Tokenizer.isWhitespace(buffer.charAt(colon - 1))) {
/*  95 */       throw new ParseException("Invalid header", buffer, 0, buffer.length(), colon - 1);
/*     */     }
/*  97 */     String s = buffer.substringTrimmed(0, colon);
/*  98 */     if (s.isEmpty()) {
/*  99 */       throw new ParseException("Invalid header", buffer, 0, buffer.length(), colon);
/*     */     }
/* 101 */     this.buffer = buffer;
/* 102 */     this.name = s;
/* 103 */     this.valuePos = colon + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 108 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 113 */     return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getValuePos() {
/* 123 */     return this.valuePos;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArrayBuffer getBuffer() {
/* 128 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return this.buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BufferedHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */