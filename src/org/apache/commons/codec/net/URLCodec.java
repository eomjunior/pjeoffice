/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URLCodec
/*     */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*     */ {
/*     */   @Deprecated
/*     */   protected volatile String charset;
/*     */   protected static final byte ESCAPE_CHAR = 37;
/*     */   @Deprecated
/*     */   protected static final BitSet WWW_FORM_URL;
/*  73 */   private static final BitSet WWW_FORM_URL_SAFE = new BitSet(256);
/*     */ 
/*     */   
/*     */   static {
/*     */     int i;
/*  78 */     for (i = 97; i <= 122; i++) {
/*  79 */       WWW_FORM_URL_SAFE.set(i);
/*     */     }
/*  81 */     for (i = 65; i <= 90; i++) {
/*  82 */       WWW_FORM_URL_SAFE.set(i);
/*     */     }
/*     */     
/*  85 */     for (i = 48; i <= 57; i++) {
/*  86 */       WWW_FORM_URL_SAFE.set(i);
/*     */     }
/*     */     
/*  89 */     WWW_FORM_URL_SAFE.set(45);
/*  90 */     WWW_FORM_URL_SAFE.set(95);
/*  91 */     WWW_FORM_URL_SAFE.set(46);
/*  92 */     WWW_FORM_URL_SAFE.set(42);
/*     */     
/*  94 */     WWW_FORM_URL_SAFE.set(32);
/*     */ 
/*     */     
/*  97 */     WWW_FORM_URL = (BitSet)WWW_FORM_URL_SAFE.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLCodec() {
/* 105 */     this("UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URLCodec(String charset) {
/* 115 */     this.charset = charset;
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
/*     */   public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
/* 128 */     if (bytes == null) {
/* 129 */       return null;
/*     */     }
/* 131 */     if (urlsafe == null) {
/* 132 */       urlsafe = WWW_FORM_URL_SAFE;
/*     */     }
/*     */     
/* 135 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 136 */     for (byte c : bytes) {
/* 137 */       int b = c;
/* 138 */       if (b < 0) {
/* 139 */         b = 256 + b;
/*     */       }
/* 141 */       if (urlsafe.get(b)) {
/* 142 */         if (b == 32) {
/* 143 */           b = 43;
/*     */         }
/* 145 */         buffer.write(b);
/*     */       } else {
/* 147 */         buffer.write(37);
/* 148 */         char hex1 = Utils.hexDigit(b >> 4);
/* 149 */         char hex2 = Utils.hexDigit(b);
/* 150 */         buffer.write(hex1);
/* 151 */         buffer.write(hex2);
/*     */       } 
/*     */     } 
/* 154 */     return buffer.toByteArray();
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
/*     */   public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
/* 168 */     if (bytes == null) {
/* 169 */       return null;
/*     */     }
/* 171 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 172 */     for (int i = 0; i < bytes.length; i++) {
/* 173 */       int b = bytes[i];
/* 174 */       if (b == 43) {
/* 175 */         buffer.write(32);
/* 176 */       } else if (b == 37) {
/*     */         try {
/* 178 */           int u = Utils.digit16(bytes[++i]);
/* 179 */           int l = Utils.digit16(bytes[++i]);
/* 180 */           buffer.write((char)((u << 4) + l));
/* 181 */         } catch (ArrayIndexOutOfBoundsException e) {
/* 182 */           throw new DecoderException("Invalid URL encoding: ", e);
/*     */         } 
/*     */       } else {
/* 185 */         buffer.write(b);
/*     */       } 
/*     */     } 
/* 188 */     return buffer.toByteArray();
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
/*     */   public byte[] encode(byte[] bytes) {
/* 200 */     return encodeUrl(WWW_FORM_URL_SAFE, bytes);
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
/*     */   public byte[] decode(byte[] bytes) throws DecoderException {
/* 216 */     return decodeUrl(bytes);
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
/*     */   public String encode(String str, String charsetName) throws UnsupportedEncodingException {
/* 231 */     if (str == null) {
/* 232 */       return null;
/*     */     }
/* 234 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charsetName)));
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
/*     */   public String encode(String str) throws EncoderException {
/* 250 */     if (str == null) {
/* 251 */       return null;
/*     */     }
/*     */     try {
/* 254 */       return encode(str, getDefaultCharset());
/* 255 */     } catch (UnsupportedEncodingException e) {
/* 256 */       throw new EncoderException(e.getMessage(), e);
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
/*     */   
/*     */   public String decode(String str, String charsetName) throws DecoderException, UnsupportedEncodingException {
/* 277 */     if (str == null) {
/* 278 */       return null;
/*     */     }
/* 280 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charsetName);
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
/*     */   public String decode(String str) throws DecoderException {
/* 296 */     if (str == null) {
/* 297 */       return null;
/*     */     }
/*     */     try {
/* 300 */       return decode(str, getDefaultCharset());
/* 301 */     } catch (UnsupportedEncodingException e) {
/* 302 */       throw new DecoderException(e.getMessage(), e);
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
/* 317 */     if (obj == null)
/* 318 */       return null; 
/* 319 */     if (obj instanceof byte[])
/* 320 */       return encode((byte[])obj); 
/* 321 */     if (obj instanceof String) {
/* 322 */       return encode((String)obj);
/*     */     }
/* 324 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
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
/* 342 */     if (obj == null)
/* 343 */       return null; 
/* 344 */     if (obj instanceof byte[])
/* 345 */       return decode((byte[])obj); 
/* 346 */     if (obj instanceof String) {
/* 347 */       return decode((String)obj);
/*     */     }
/* 349 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 360 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getEncoding() {
/* 372 */     return this.charset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/net/URLCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */