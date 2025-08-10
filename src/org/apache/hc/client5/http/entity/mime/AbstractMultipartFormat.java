/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractMultipartFormat
/*     */ {
/*     */   static ByteArrayBuffer encode(Charset charset, CharSequence string) {
/*  51 */     ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
/*  52 */     ByteArrayBuffer bab = new ByteArrayBuffer(encoded.remaining());
/*  53 */     bab.append(encoded.array(), encoded.arrayOffset() + encoded.position(), encoded.remaining());
/*  54 */     return bab;
/*     */   }
/*     */ 
/*     */   
/*     */   static void writeBytes(ByteArrayBuffer b, OutputStream out) throws IOException {
/*  59 */     out.write(b.array(), 0, b.length());
/*     */   }
/*     */ 
/*     */   
/*     */   static void writeBytes(CharSequence s, Charset charset, OutputStream out) throws IOException {
/*  64 */     ByteArrayBuffer b = encode(charset, s);
/*  65 */     writeBytes(b, out);
/*     */   }
/*     */ 
/*     */   
/*     */   static void writeBytes(CharSequence s, OutputStream out) throws IOException {
/*  70 */     ByteArrayBuffer b = encode(StandardCharsets.ISO_8859_1, s);
/*  71 */     writeBytes(b, out);
/*     */   }
/*     */   
/*     */   static boolean isLineBreak(char ch) {
/*  75 */     return (ch == '\r' || ch == '\n' || ch == '\f' || ch == '\013');
/*     */   }
/*     */   
/*     */   static CharSequence stripLineBreaks(CharSequence s) {
/*  79 */     if (s == null) {
/*  80 */       return null;
/*     */     }
/*  82 */     boolean requiresRewrite = false;
/*  83 */     int n = 0;
/*  84 */     for (; n < s.length(); n++) {
/*  85 */       char ch = s.charAt(n);
/*  86 */       if (isLineBreak(ch)) {
/*  87 */         requiresRewrite = true;
/*     */         break;
/*     */       } 
/*     */     } 
/*  91 */     if (!requiresRewrite) {
/*  92 */       return s;
/*     */     }
/*  94 */     StringBuilder buf = new StringBuilder();
/*  95 */     buf.append(s, 0, n);
/*  96 */     for (; n < s.length(); n++) {
/*  97 */       char ch = s.charAt(n);
/*  98 */       if (isLineBreak(ch)) {
/*  99 */         buf.append(' ');
/*     */       } else {
/* 101 */         buf.append(ch);
/*     */       } 
/*     */     } 
/* 104 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static void writeField(MimeField field, OutputStream out) throws IOException {
/* 109 */     writeBytes(stripLineBreaks(field.getName()), out);
/* 110 */     writeBytes(FIELD_SEP, out);
/* 111 */     writeBytes(stripLineBreaks(field.getBody()), out);
/* 112 */     writeBytes(CR_LF, out);
/*     */   }
/*     */ 
/*     */   
/*     */   static void writeField(MimeField field, Charset charset, OutputStream out) throws IOException {
/* 117 */     writeBytes(stripLineBreaks(field.getName()), charset, out);
/* 118 */     writeBytes(FIELD_SEP, out);
/* 119 */     writeBytes(stripLineBreaks(field.getBody()), charset, out);
/* 120 */     writeBytes(CR_LF, out);
/*     */   }
/*     */   
/* 123 */   static final ByteArrayBuffer FIELD_SEP = encode(StandardCharsets.ISO_8859_1, ": ");
/* 124 */   static final ByteArrayBuffer CR_LF = encode(StandardCharsets.ISO_8859_1, "\r\n");
/* 125 */   static final ByteArrayBuffer TWO_HYPHENS = encode(StandardCharsets.ISO_8859_1, "--");
/*     */ 
/*     */ 
/*     */   
/*     */   final Charset charset;
/*     */ 
/*     */ 
/*     */   
/*     */   final String boundary;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMultipartFormat(Charset charset, String boundary) {
/* 139 */     Args.notNull(boundary, "Multipart boundary");
/* 140 */     this.charset = (charset != null) ? charset : StandardCharsets.ISO_8859_1;
/* 141 */     this.boundary = boundary;
/*     */   }
/*     */   
/*     */   public AbstractMultipartFormat(String boundary) {
/* 145 */     this(null, boundary);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract List<MultipartPart> getParts();
/*     */ 
/*     */   
/*     */   void doWriteTo(OutputStream out, boolean writeContent) throws IOException {
/* 154 */     ByteArrayBuffer boundaryEncoded = encode(this.charset, this.boundary);
/* 155 */     for (MultipartPart part : getParts()) {
/* 156 */       writeBytes(TWO_HYPHENS, out);
/* 157 */       writeBytes(boundaryEncoded, out);
/* 158 */       writeBytes(CR_LF, out);
/*     */       
/* 160 */       formatMultipartHeader(part, out);
/*     */       
/* 162 */       writeBytes(CR_LF, out);
/*     */       
/* 164 */       if (writeContent) {
/* 165 */         part.getBody().writeTo(out);
/*     */       }
/* 167 */       writeBytes(CR_LF, out);
/*     */     } 
/* 169 */     writeBytes(TWO_HYPHENS, out);
/* 170 */     writeBytes(boundaryEncoded, out);
/* 171 */     writeBytes(TWO_HYPHENS, out);
/* 172 */     writeBytes(CR_LF, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void formatMultipartHeader(MultipartPart paramMultipartPart, OutputStream paramOutputStream) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 188 */     doWriteTo(out, true);
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
/*     */   public long getTotalLength() {
/* 206 */     long contentLen = 0L;
/* 207 */     for (MultipartPart part : getParts()) {
/* 208 */       ContentBody body = part.getBody();
/* 209 */       long len = body.getContentLength();
/* 210 */       if (len >= 0L) {
/* 211 */         contentLen += len; continue;
/*     */       } 
/* 213 */       return -1L;
/*     */     } 
/*     */     
/* 216 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/* 218 */       doWriteTo(out, false);
/* 219 */       byte[] extra = out.toByteArray();
/* 220 */       return contentLen + extra.length;
/* 221 */     } catch (IOException ex) {
/*     */       
/* 223 */       return -1L;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/AbstractMultipartFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */