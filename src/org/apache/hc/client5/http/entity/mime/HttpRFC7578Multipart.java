/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.ByteArrayBuffer;
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
/*     */ class HttpRFC7578Multipart
/*     */   extends AbstractMultipartFormat
/*     */ {
/*  44 */   private static final PercentCodec PERCENT_CODEC = new PercentCodec();
/*     */   
/*     */   private final List<MultipartPart> parts;
/*     */   
/*     */   private static final int RADIX = 16;
/*     */ 
/*     */   
/*     */   public HttpRFC7578Multipart(Charset charset, String boundary, List<MultipartPart> parts) {
/*  52 */     super(charset, boundary);
/*  53 */     this.parts = parts;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MultipartPart> getParts() {
/*  58 */     return this.parts;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void formatMultipartHeader(MultipartPart part, OutputStream out) throws IOException {
/*  63 */     for (MimeField field : part.getHeader()) {
/*  64 */       if ("Content-Disposition".equalsIgnoreCase(field.getName())) {
/*  65 */         writeBytes(field.getName(), this.charset, out);
/*  66 */         writeBytes(FIELD_SEP, out);
/*  67 */         writeBytes(field.getValue(), out);
/*  68 */         List<NameValuePair> parameters = field.getParameters();
/*  69 */         for (int i = 0; i < parameters.size(); i++) {
/*  70 */           NameValuePair parameter = parameters.get(i);
/*  71 */           String name = parameter.getName();
/*  72 */           String value = parameter.getValue();
/*  73 */           writeBytes("; ", out);
/*  74 */           writeBytes(name, out);
/*  75 */           writeBytes("=\"", out);
/*  76 */           if (value != null) {
/*  77 */             if (name.equalsIgnoreCase("filename")) {
/*  78 */               out.write(PERCENT_CODEC.encode(value.getBytes(this.charset)));
/*     */             } else {
/*  80 */               writeBytes(value, out);
/*     */             } 
/*     */           }
/*  83 */           writeBytes("\"", out);
/*     */         } 
/*  85 */         writeBytes(CR_LF, out); continue;
/*     */       } 
/*  87 */       writeField(field, this.charset, out);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class PercentCodec
/*     */   {
/*     */     private static final byte ESCAPE_CHAR = 37;
/*     */     
/*  96 */     private static final BitSet ALWAYSENCODECHARS = new BitSet();
/*     */     
/*     */     static {
/*  99 */       ALWAYSENCODECHARS.set(32);
/* 100 */       ALWAYSENCODECHARS.set(37);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] encode(byte[] bytes) {
/* 107 */       if (bytes == null) {
/* 108 */         return null;
/*     */       }
/*     */       
/* 111 */       CharsetEncoder characterSetEncoder = StandardCharsets.US_ASCII.newEncoder();
/* 112 */       ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 113 */       for (byte c : bytes) {
/* 114 */         int b = c;
/* 115 */         if (b < 0) {
/* 116 */           b = 256 + b;
/*     */         }
/* 118 */         if (characterSetEncoder.canEncode((char)b) && !ALWAYSENCODECHARS.get(c)) {
/* 119 */           buffer.write(b);
/*     */         } else {
/* 121 */           buffer.write(37);
/* 122 */           char hex1 = HttpRFC7578Multipart.hexDigit(b >> 4);
/* 123 */           char hex2 = HttpRFC7578Multipart.hexDigit(b);
/* 124 */           buffer.write(hex1);
/* 125 */           buffer.write(hex2);
/*     */         } 
/*     */       } 
/* 128 */       return buffer.toByteArray();
/*     */     }
/*     */     
/*     */     public byte[] decode(byte[] bytes) {
/* 132 */       if (bytes == null) {
/* 133 */         return null;
/*     */       }
/* 135 */       ByteArrayBuffer buffer = new ByteArrayBuffer(bytes.length);
/* 136 */       for (int i = 0; i < bytes.length; i++) {
/* 137 */         int b = bytes[i];
/* 138 */         if (b == 37) {
/* 139 */           if (i >= bytes.length - 2) {
/* 140 */             throw new IllegalArgumentException("Invalid encoding: too short");
/*     */           }
/* 142 */           int u = HttpRFC7578Multipart.digit16(bytes[++i]);
/* 143 */           int l = HttpRFC7578Multipart.digit16(bytes[++i]);
/* 144 */           buffer.append((char)((u << 4) + l));
/*     */         } else {
/* 146 */           buffer.append(b);
/*     */         } 
/*     */       } 
/* 149 */       return buffer.toByteArray();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int digit16(byte b) {
/* 169 */     int i = Character.digit((char)b, 16);
/* 170 */     if (i == -1) {
/* 171 */       throw new IllegalArgumentException("Invalid encoding: not a valid digit (radix 16): " + b);
/*     */     }
/* 173 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static char hexDigit(int b) {
/* 183 */     return Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/HttpRFC7578Multipart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */