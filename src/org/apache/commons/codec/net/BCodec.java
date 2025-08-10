/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.commons.codec.CodecPolicy;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.codec.binary.BaseNCodec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BCodec
/*     */   extends RFC1522Codec
/*     */   implements StringEncoder, StringDecoder
/*     */ {
/*  54 */   private static final CodecPolicy DECODING_POLICY_DEFAULT = CodecPolicy.LENIENT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final CodecPolicy decodingPolicy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BCodec() {
/*  71 */     this(StandardCharsets.UTF_8);
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
/*     */   public BCodec(Charset charset) {
/*  84 */     this(charset, DECODING_POLICY_DEFAULT);
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
/*     */   public BCodec(Charset charset, CodecPolicy decodingPolicy) {
/*  98 */     this.charset = charset;
/*  99 */     this.decodingPolicy = decodingPolicy;
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
/*     */   public BCodec(String charsetName) {
/* 113 */     this(Charset.forName(charsetName));
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
/*     */   public boolean isStrictDecoding() {
/* 127 */     return (this.decodingPolicy == CodecPolicy.STRICT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getEncoding() {
/* 132 */     return "B";
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doEncoding(byte[] bytes) {
/* 137 */     if (bytes == null) {
/* 138 */       return null;
/*     */     }
/* 140 */     return Base64.encodeBase64(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] doDecoding(byte[] bytes) {
/* 145 */     if (bytes == null) {
/* 146 */       return null;
/*     */     }
/* 148 */     return (new Base64(0, BaseNCodec.getChunkSeparator(), false, this.decodingPolicy)).decode(bytes);
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
/*     */   public String encode(String strSource, Charset sourceCharset) throws EncoderException {
/* 164 */     if (strSource == null) {
/* 165 */       return null;
/*     */     }
/* 167 */     return encodeText(strSource, sourceCharset);
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
/*     */   public String encode(String strSource, String sourceCharset) throws EncoderException {
/* 182 */     if (strSource == null) {
/* 183 */       return null;
/*     */     }
/*     */     try {
/* 186 */       return encodeText(strSource, sourceCharset);
/* 187 */     } catch (UnsupportedEncodingException e) {
/* 188 */       throw new EncoderException(e.getMessage(), e);
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
/*     */   public String encode(String strSource) throws EncoderException {
/* 203 */     if (strSource == null) {
/* 204 */       return null;
/*     */     }
/* 206 */     return encode(strSource, getCharset());
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
/*     */   public String decode(String value) throws DecoderException {
/* 221 */     if (value == null) {
/* 222 */       return null;
/*     */     }
/*     */     try {
/* 225 */       return decodeText(value);
/* 226 */     } catch (UnsupportedEncodingException|IllegalArgumentException e) {
/* 227 */       throw new DecoderException(e.getMessage(), e);
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
/*     */   public Object encode(Object value) throws EncoderException {
/* 242 */     if (value == null)
/* 243 */       return null; 
/* 244 */     if (value instanceof String) {
/* 245 */       return encode((String)value);
/*     */     }
/* 247 */     throw new EncoderException("Objects of type " + value
/* 248 */         .getClass().getName() + " cannot be encoded using BCodec");
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
/*     */   public Object decode(Object value) throws DecoderException {
/* 266 */     if (value == null)
/* 267 */       return null; 
/* 268 */     if (value instanceof String) {
/* 269 */       return decode((String)value);
/*     */     }
/* 271 */     throw new DecoderException("Objects of type " + value
/* 272 */         .getClass().getName() + " cannot be decoded using BCodec");
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
/* 284 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 293 */     return this.charset.name();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/net/BCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */