/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.ByteArrayBuffer;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EntityUtils
/*     */ {
/*     */   private static final int DEFAULT_ENTITY_RETURN_MAX_LENGTH = 2147483647;
/*  62 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_CHAR_BUFFER_SIZE = 1024;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_BYTE_BUFFER_SIZE = 4096;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Map<String, ContentType> CONTENT_TYPE_MAP;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void consumeQuietly(HttpEntity entity) {
/*     */     try {
/*  80 */       consume(entity);
/*  81 */     } catch (IOException iOException) {}
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
/*     */   public static void consume(HttpEntity entity) throws IOException {
/*  96 */     if (entity == null) {
/*     */       return;
/*     */     }
/*  99 */     if (entity.isStreaming()) {
/* 100 */       Closer.close(entity.getContent());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int toContentLength(int contentLength) {
/* 111 */     return (contentLength < 0) ? 4096 : contentLength;
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
/*     */   public static byte[] toByteArray(HttpEntity entity) throws IOException {
/* 124 */     Args.notNull(entity, "HttpEntity");
/* 125 */     int contentLength = toContentLength((int)Args.checkContentLength((EntityDetails)entity));
/* 126 */     try (InputStream inStream = entity.getContent()) {
/* 127 */       if (inStream == null) {
/* 128 */         return null;
/*     */       }
/* 130 */       ByteArrayBuffer buffer = new ByteArrayBuffer(contentLength);
/* 131 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 133 */       while ((l = inStream.read(tmp)) != -1) {
/* 134 */         buffer.append(tmp, 0, l);
/*     */       }
/* 136 */       return buffer.toByteArray();
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
/*     */   public static byte[] toByteArray(HttpEntity entity, int maxResultLength) throws IOException {
/* 152 */     Args.notNull(entity, "HttpEntity");
/* 153 */     int contentLength = toContentLength((int)Args.checkContentLength((EntityDetails)entity));
/* 154 */     try (InputStream inStream = entity.getContent()) {
/* 155 */       if (inStream == null) {
/* 156 */         return null;
/*     */       }
/* 158 */       ByteArrayBuffer buffer = new ByteArrayBuffer(Math.min(maxResultLength, contentLength));
/* 159 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 161 */       while ((l = inStream.read(tmp)) != -1 && buffer.length() < maxResultLength) {
/* 162 */         buffer.append(tmp, 0, l);
/*     */       }
/* 164 */       buffer.setLength(Math.min(buffer.length(), maxResultLength));
/* 165 */       return buffer.toByteArray();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static CharArrayBuffer toCharArrayBuffer(InputStream inStream, int contentLength, Charset charset, int maxResultLength) throws IOException {
/* 171 */     Args.notNull(inStream, "InputStream");
/* 172 */     Args.positive(maxResultLength, "maxResultLength");
/* 173 */     Charset actualCharset = (charset == null) ? DEFAULT_CHARSET : charset;
/*     */     
/* 175 */     CharArrayBuffer buf = new CharArrayBuffer(Math.min(maxResultLength, (contentLength > 0) ? contentLength : 1024));
/* 176 */     Reader reader = new InputStreamReader(inStream, actualCharset);
/* 177 */     char[] tmp = new char[1024];
/*     */     int chReadCount;
/* 179 */     while ((chReadCount = reader.read(tmp)) != -1 && buf.length() < maxResultLength) {
/* 180 */       buf.append(tmp, 0, chReadCount);
/*     */     }
/* 182 */     buf.setLength(Math.min(buf.length(), maxResultLength));
/* 183 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 188 */     ContentType[] contentTypes = { ContentType.APPLICATION_ATOM_XML, ContentType.APPLICATION_FORM_URLENCODED, ContentType.APPLICATION_JSON, ContentType.APPLICATION_SVG_XML, ContentType.APPLICATION_XHTML_XML, ContentType.APPLICATION_XML, ContentType.MULTIPART_FORM_DATA, ContentType.TEXT_HTML, ContentType.TEXT_PLAIN, ContentType.TEXT_XML };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     HashMap<String, ContentType> map = new HashMap<>();
/* 200 */     for (ContentType contentType : contentTypes) {
/* 201 */       map.put(contentType.getMimeType(), contentType);
/*     */     }
/* 203 */     CONTENT_TYPE_MAP = Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toString(HttpEntity entity, ContentType contentType, int maxResultLength) throws IOException {
/* 208 */     Args.notNull(entity, "HttpEntity");
/* 209 */     int contentLength = toContentLength((int)Args.checkContentLength((EntityDetails)entity));
/* 210 */     try (InputStream inStream = entity.getContent()) {
/* 211 */       if (inStream == null) {
/* 212 */         return null;
/*     */       }
/* 214 */       Charset charset = null;
/* 215 */       if (contentType != null) {
/* 216 */         charset = contentType.getCharset();
/* 217 */         if (charset == null) {
/* 218 */           ContentType defaultContentType = CONTENT_TYPE_MAP.get(contentType.getMimeType());
/* 219 */           charset = (defaultContentType != null) ? defaultContentType.getCharset() : null;
/*     */         } 
/*     */       } 
/* 222 */       return toCharArrayBuffer(inStream, contentLength, charset, maxResultLength).toString();
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
/*     */   
/*     */   public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
/* 244 */     return toString(entity, defaultCharset, 2147483647);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(HttpEntity entity, Charset defaultCharset, int maxResultLength) throws IOException, ParseException {
/* 267 */     Args.notNull(entity, "HttpEntity");
/* 268 */     ContentType contentType = null;
/*     */     try {
/* 270 */       contentType = ContentType.parse(entity.getContentType());
/* 271 */     } catch (UnsupportedCharsetException ex) {
/* 272 */       if (defaultCharset == null) {
/* 273 */         throw new UnsupportedEncodingException(ex.getMessage());
/*     */       }
/*     */     } 
/* 276 */     if (contentType != null) {
/* 277 */       if (contentType.getCharset() == null) {
/* 278 */         contentType = contentType.withCharset(defaultCharset);
/*     */       }
/*     */     } else {
/* 281 */       contentType = ContentType.DEFAULT_TEXT.withCharset(defaultCharset);
/*     */     } 
/* 283 */     return toString(entity, contentType, maxResultLength);
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
/*     */   public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
/* 303 */     return toString(entity, defaultCharset, 2147483647);
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
/*     */ 
/*     */   
/*     */   public static String toString(HttpEntity entity, String defaultCharset, int maxResultLength) throws IOException, ParseException {
/* 325 */     return toString(entity, (defaultCharset != null) ? Charset.forName(defaultCharset) : null, maxResultLength);
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
/*     */   public static String toString(HttpEntity entity) throws IOException, ParseException {
/* 342 */     return toString(entity, 2147483647);
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
/*     */   public static String toString(HttpEntity entity, int maxResultLength) throws IOException, ParseException {
/* 361 */     Args.notNull(entity, "HttpEntity");
/* 362 */     return toString(entity, ContentType.parse(entity.getContentType()), maxResultLength);
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
/*     */   public static List<NameValuePair> parse(HttpEntity entity) throws IOException {
/* 379 */     return parse(entity, 2147483647);
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
/*     */   public static List<NameValuePair> parse(HttpEntity entity, int maxStreamLength) throws IOException {
/*     */     CharArrayBuffer buf;
/* 398 */     Args.notNull(entity, "HttpEntity");
/* 399 */     int contentLength = toContentLength((int)Args.checkContentLength((EntityDetails)entity));
/* 400 */     ContentType contentType = ContentType.parse(entity.getContentType());
/* 401 */     if (!ContentType.APPLICATION_FORM_URLENCODED.isSameMimeType(contentType)) {
/* 402 */       return Collections.emptyList();
/*     */     }
/* 404 */     Charset charset = contentType.getCharset(DEFAULT_CHARSET);
/*     */     
/* 406 */     try (InputStream inStream = entity.getContent()) {
/* 407 */       if (inStream == null) {
/* 408 */         return (List)Collections.emptyList();
/*     */       }
/* 410 */       buf = toCharArrayBuffer(inStream, contentLength, charset, maxStreamLength);
/*     */     } 
/*     */     
/* 413 */     if (buf.isEmpty()) {
/* 414 */       return Collections.emptyList();
/*     */     }
/* 416 */     return WWWFormCodec.parse((CharSequence)buf, charset);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/EntityUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */