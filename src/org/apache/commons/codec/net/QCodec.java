/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
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
/*     */ public class QCodec
/*     */   extends RFC1522Codec
/*     */   implements StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*  62 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*     */   private static final byte SPACE = 32;
/*     */   
/*     */   static {
/*  66 */     PRINTABLE_CHARS.set(32);
/*  67 */     PRINTABLE_CHARS.set(33);
/*  68 */     PRINTABLE_CHARS.set(34);
/*  69 */     PRINTABLE_CHARS.set(35);
/*  70 */     PRINTABLE_CHARS.set(36);
/*  71 */     PRINTABLE_CHARS.set(37);
/*  72 */     PRINTABLE_CHARS.set(38);
/*  73 */     PRINTABLE_CHARS.set(39);
/*  74 */     PRINTABLE_CHARS.set(40);
/*  75 */     PRINTABLE_CHARS.set(41);
/*  76 */     PRINTABLE_CHARS.set(42);
/*  77 */     PRINTABLE_CHARS.set(43);
/*  78 */     PRINTABLE_CHARS.set(44);
/*  79 */     PRINTABLE_CHARS.set(45);
/*  80 */     PRINTABLE_CHARS.set(46);
/*  81 */     PRINTABLE_CHARS.set(47); int i;
/*  82 */     for (i = 48; i <= 57; i++) {
/*  83 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  85 */     PRINTABLE_CHARS.set(58);
/*  86 */     PRINTABLE_CHARS.set(59);
/*  87 */     PRINTABLE_CHARS.set(60);
/*  88 */     PRINTABLE_CHARS.set(62);
/*  89 */     PRINTABLE_CHARS.set(64);
/*  90 */     for (i = 65; i <= 90; i++) {
/*  91 */       PRINTABLE_CHARS.set(i);
/*     */     }
/*  93 */     PRINTABLE_CHARS.set(91);
/*  94 */     PRINTABLE_CHARS.set(92);
/*  95 */     PRINTABLE_CHARS.set(93);
/*  96 */     PRINTABLE_CHARS.set(94);
/*  97 */     PRINTABLE_CHARS.set(96);
/*  98 */     for (i = 97; i <= 122; i++) {
/*  99 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 101 */     PRINTABLE_CHARS.set(123);
/* 102 */     PRINTABLE_CHARS.set(124);
/* 103 */     PRINTABLE_CHARS.set(125);
/* 104 */     PRINTABLE_CHARS.set(126);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final byte UNDERSCORE = 95;
/*     */ 
/*     */   
/*     */   private boolean encodeBlanks = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public QCodec() {
/* 117 */     this(StandardCharsets.UTF_8);
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
/*     */   public QCodec(Charset charset) {
/* 131 */     this.charset = charset;
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
/*     */   public QCodec(String charsetName) {
/* 145 */     this(Charset.forName(charsetName));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getEncoding() {
/* 150 */     return "Q";
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doEncoding(byte[] bytes) {
/* 155 */     if (bytes == null) {
/* 156 */       return null;
/*     */     }
/* 158 */     byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
/* 159 */     if (this.encodeBlanks) {
/* 160 */       for (int i = 0; i < data.length; i++) {
/* 161 */         if (data[i] == 32) {
/* 162 */           data[i] = 95;
/*     */         }
/*     */       } 
/*     */     }
/* 166 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doDecoding(byte[] bytes) throws DecoderException {
/* 171 */     if (bytes == null) {
/* 172 */       return null;
/*     */     }
/* 174 */     boolean hasUnderscores = false;
/* 175 */     for (byte b : bytes) {
/* 176 */       if (b == 95) {
/* 177 */         hasUnderscores = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 181 */     if (hasUnderscores) {
/* 182 */       byte[] tmp = new byte[bytes.length];
/* 183 */       for (int i = 0; i < bytes.length; i++) {
/* 184 */         byte b = bytes[i];
/* 185 */         if (b != 95) {
/* 186 */           tmp[i] = b;
/*     */         } else {
/* 188 */           tmp[i] = 32;
/*     */         } 
/*     */       } 
/* 191 */       return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
/*     */     } 
/* 193 */     return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
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
/*     */   public String encode(String sourceStr, Charset sourceCharset) throws EncoderException {
/* 209 */     if (sourceStr == null) {
/* 210 */       return null;
/*     */     }
/* 212 */     return encodeText(sourceStr, sourceCharset);
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
/*     */   public String encode(String sourceStr, String sourceCharset) throws EncoderException {
/* 227 */     if (sourceStr == null) {
/* 228 */       return null;
/*     */     }
/*     */     try {
/* 231 */       return encodeText(sourceStr, sourceCharset);
/* 232 */     } catch (UnsupportedEncodingException e) {
/* 233 */       throw new EncoderException(e.getMessage(), e);
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
/*     */   public String encode(String sourceStr) throws EncoderException {
/* 248 */     if (sourceStr == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     return encode(sourceStr, getCharset());
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
/*     */   public String decode(String str) throws DecoderException {
/* 266 */     if (str == null) {
/* 267 */       return null;
/*     */     }
/*     */     try {
/* 270 */       return decodeText(str);
/* 271 */     } catch (UnsupportedEncodingException e) {
/* 272 */       throw new DecoderException(e.getMessage(), e);
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 287 */     if (obj == null)
/* 288 */       return null; 
/* 289 */     if (obj instanceof String) {
/* 290 */       return encode((String)obj);
/*     */     }
/* 292 */     throw new EncoderException("Objects of type " + obj
/* 293 */         .getClass().getName() + " cannot be encoded using Q codec");
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
/*     */   public Object decode(Object obj) throws DecoderException {
/* 311 */     if (obj == null)
/* 312 */       return null; 
/* 313 */     if (obj instanceof String) {
/* 314 */       return decode((String)obj);
/*     */     }
/* 316 */     throw new DecoderException("Objects of type " + obj
/* 317 */         .getClass().getName() + " cannot be decoded using Q codec");
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
/*     */   public Charset getCharset() {
/* 329 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 338 */     return this.charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEncodeBlanks() {
/* 347 */     return this.encodeBlanks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncodeBlanks(boolean b) {
/* 357 */     this.encodeBlanks = b;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/net/QCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */