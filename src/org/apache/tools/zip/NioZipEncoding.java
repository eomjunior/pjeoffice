/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ class NioZipEncoding
/*     */   implements ZipEncoding
/*     */ {
/*     */   private final Charset charset;
/*     */   
/*     */   public NioZipEncoding(Charset charset) {
/*  50 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(String name) {
/*  57 */     CharsetEncoder enc = this.charset.newEncoder();
/*  58 */     enc.onMalformedInput(CodingErrorAction.REPORT);
/*  59 */     enc.onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     
/*  61 */     return enc.canEncode(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer encode(String name) {
/*  68 */     CharsetEncoder enc = this.charset.newEncoder();
/*     */     
/*  70 */     enc.onMalformedInput(CodingErrorAction.REPORT);
/*  71 */     enc.onUnmappableCharacter(CodingErrorAction.REPORT);
/*     */     
/*  73 */     CharBuffer cb = CharBuffer.wrap(name);
/*  74 */     ByteBuffer out = ByteBuffer.allocate(name.length() + (name
/*  75 */         .length() + 1) / 2);
/*     */     
/*  77 */     while (cb.remaining() > 0) {
/*  78 */       CoderResult res = enc.encode(cb, out, true);
/*     */       
/*  80 */       if (res.isUnmappable() || res.isMalformed()) {
/*     */ 
/*     */ 
/*     */         
/*  84 */         if (res.length() * 6 > out.remaining()) {
/*  85 */           out = ZipEncodingHelper.growBuffer(out, out.position() + res
/*  86 */               .length() * 6);
/*     */         }
/*     */         
/*  89 */         for (int i = 0; i < res.length(); i++)
/*  90 */           ZipEncodingHelper.appendSurrogate(out, cb.get()); 
/*     */         continue;
/*     */       } 
/*  93 */       if (res.isOverflow()) {
/*     */         
/*  95 */         out = ZipEncodingHelper.growBuffer(out, 0); continue;
/*     */       } 
/*  97 */       if (res.isUnderflow()) {
/*     */         
/*  99 */         enc.flush(out);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     ZipEncodingHelper.prepareBufferForRead(out);
/*     */     
/* 107 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decode(byte[] data) throws IOException {
/* 114 */     return this.charset.newDecoder()
/* 115 */       .onMalformedInput(CodingErrorAction.REPORT)
/* 116 */       .onUnmappableCharacter(CodingErrorAction.REPORT)
/* 117 */       .decode(ByteBuffer.wrap(data)).toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/NioZipEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */