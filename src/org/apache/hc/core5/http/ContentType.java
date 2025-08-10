/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.hc.core5.http.message.BasicHeaderValueParser;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.http.message.ParserCursor;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class ContentType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7768694718232371896L;
/*     */   private static final String CHARSET = "charset";
/*  71 */   public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", StandardCharsets.UTF_8);
/*     */   
/*  73 */   public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", StandardCharsets.ISO_8859_1);
/*     */   
/*  75 */   public static final ContentType APPLICATION_JSON = create("application/json", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final ContentType APPLICATION_NDJSON = create("application/x-ndjson", StandardCharsets.UTF_8);
/*     */ 
/*     */   
/*  85 */   public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset)null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static final ContentType APPLICATION_PDF = create("application/pdf", StandardCharsets.UTF_8);
/*     */ 
/*     */   
/*  94 */   public static final ContentType APPLICATION_SOAP_XML = create("application/soap+xml", StandardCharsets.UTF_8);
/*     */   
/*  96 */   public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", StandardCharsets.UTF_8);
/*     */   
/*  98 */   public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", StandardCharsets.UTF_8);
/*     */   
/* 100 */   public static final ContentType APPLICATION_XML = create("application/xml", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static final ContentType APPLICATION_PROBLEM_JSON = create("application/problem+json", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public static final ContentType APPLICATION_PROBLEM_XML = create("application/problem+xml", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final ContentType APPLICATION_RSS_XML = create("application/rss+xml", StandardCharsets.UTF_8);
/*     */ 
/*     */   
/* 124 */   public static final ContentType IMAGE_BMP = create("image/bmp");
/*     */   
/* 126 */   public static final ContentType IMAGE_GIF = create("image/gif");
/*     */   
/* 128 */   public static final ContentType IMAGE_JPEG = create("image/jpeg");
/*     */   
/* 130 */   public static final ContentType IMAGE_PNG = create("image/png");
/*     */   
/* 132 */   public static final ContentType IMAGE_SVG = create("image/svg+xml");
/*     */   
/* 134 */   public static final ContentType IMAGE_TIFF = create("image/tiff");
/*     */   
/* 136 */   public static final ContentType IMAGE_WEBP = create("image/webp");
/*     */   
/* 138 */   public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", StandardCharsets.ISO_8859_1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static final ContentType MULTIPART_MIXED = create("multipart/mixed", StandardCharsets.ISO_8859_1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static final ContentType MULTIPART_RELATED = create("multipart/related", StandardCharsets.ISO_8859_1);
/*     */ 
/*     */   
/* 155 */   public static final ContentType TEXT_HTML = create("text/html", StandardCharsets.ISO_8859_1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public static final ContentType TEXT_MARKDOWN = create("text/markdown", StandardCharsets.UTF_8);
/*     */ 
/*     */   
/* 165 */   public static final ContentType TEXT_PLAIN = create("text/plain", StandardCharsets.ISO_8859_1);
/*     */   
/* 167 */   public static final ContentType TEXT_XML = create("text/xml", StandardCharsets.UTF_8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public static final ContentType TEXT_EVENT_STREAM = create("text/event-stream", StandardCharsets.UTF_8);
/*     */ 
/*     */   
/* 177 */   public static final ContentType WILDCARD = create("*/*", (Charset)null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   private static final NameValuePair[] EMPTY_NAME_VALUE_PAIR_ARRAY = new NameValuePair[0];
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   private static final Map<String, ContentType> CONTENT_TYPE_MAP;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 192 */     ContentType[] contentTypes = { APPLICATION_ATOM_XML, APPLICATION_FORM_URLENCODED, APPLICATION_JSON, APPLICATION_SVG_XML, APPLICATION_XHTML_XML, APPLICATION_XML, IMAGE_BMP, IMAGE_GIF, IMAGE_JPEG, IMAGE_PNG, IMAGE_SVG, IMAGE_TIFF, IMAGE_WEBP, MULTIPART_FORM_DATA, TEXT_HTML, TEXT_PLAIN, TEXT_XML };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     HashMap<String, ContentType> map = new HashMap<>();
/* 211 */     for (ContentType contentType : contentTypes) {
/* 212 */       map.put(contentType.getMimeType(), contentType);
/*     */     }
/* 214 */     CONTENT_TYPE_MAP = Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */   
/* 218 */   public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
/* 219 */   public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
/*     */   
/*     */   private final String mimeType;
/*     */   
/*     */   private final Charset charset;
/*     */   
/*     */   private final NameValuePair[] params;
/*     */   
/*     */   ContentType(String mimeType, Charset charset) {
/* 228 */     this.mimeType = mimeType;
/* 229 */     this.charset = charset;
/* 230 */     this.params = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ContentType(String mimeType, Charset charset, NameValuePair[] params) {
/* 237 */     this.mimeType = mimeType;
/* 238 */     this.charset = charset;
/* 239 */     this.params = params;
/*     */   }
/*     */   
/*     */   public String getMimeType() {
/* 243 */     return this.mimeType;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 247 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset(Charset defaultCharset) {
/* 258 */     return (this.charset != null) ? this.charset : defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 265 */     Args.notEmpty(name, "Parameter name");
/* 266 */     if (this.params == null) {
/* 267 */       return null;
/*     */     }
/* 269 */     for (NameValuePair param : this.params) {
/* 270 */       if (param.getName().equalsIgnoreCase(name)) {
/* 271 */         return param.getValue();
/*     */       }
/*     */     } 
/* 274 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 283 */     CharArrayBuffer buf = new CharArrayBuffer(64);
/* 284 */     buf.append(this.mimeType);
/* 285 */     if (this.params != null) {
/* 286 */       buf.append("; ");
/* 287 */       BasicHeaderValueFormatter.INSTANCE.formatParameters(buf, this.params, false);
/* 288 */     } else if (this.charset != null) {
/* 289 */       buf.append("; charset=");
/* 290 */       buf.append(this.charset.name());
/*     */     } 
/* 292 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static boolean valid(String s) {
/* 296 */     for (int i = 0; i < s.length(); i++) {
/* 297 */       char ch = s.charAt(i);
/* 298 */       if (ch == '"' || ch == ',' || ch == ';') {
/* 299 */         return false;
/*     */       }
/*     */     } 
/* 302 */     return true;
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
/*     */   public static ContentType create(String mimeType, Charset charset) {
/* 314 */     String normalizedMimeType = TextUtils.toLowerCase((String)Args.notBlank(mimeType, "MIME type"));
/* 315 */     Args.check(valid(normalizedMimeType), "MIME type may not contain reserved characters");
/* 316 */     return new ContentType(normalizedMimeType, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentType create(String mimeType) {
/* 327 */     return create(mimeType, (Charset)null);
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
/*     */   public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
/* 343 */     return create(mimeType, !TextUtils.isBlank(charset) ? Charset.forName(charset) : null);
/*     */   }
/*     */   
/*     */   private static ContentType create(HeaderElement helem, boolean strict) {
/* 347 */     String mimeType = helem.getName();
/* 348 */     if (TextUtils.isBlank(mimeType)) {
/* 349 */       return null;
/*     */     }
/* 351 */     return create(helem.getName(), helem.getParameters(), strict);
/*     */   }
/*     */   
/*     */   private static ContentType create(String mimeType, NameValuePair[] params, boolean strict) {
/* 355 */     Charset charset = null;
/* 356 */     if (params != null) {
/* 357 */       for (NameValuePair param : params) {
/* 358 */         if (param.getName().equalsIgnoreCase("charset")) {
/* 359 */           String s = param.getValue();
/* 360 */           if (!TextUtils.isBlank(s)) {
/*     */             try {
/* 362 */               charset = Charset.forName(s);
/* 363 */             } catch (UnsupportedCharsetException ex) {
/* 364 */               if (strict) {
/* 365 */                 throw ex;
/*     */               }
/*     */             } 
/*     */           }
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 373 */     return new ContentType(mimeType, charset, (params != null && params.length > 0) ? params : null);
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
/*     */   public static ContentType create(String mimeType, NameValuePair... params) throws UnsupportedCharsetException {
/* 388 */     String type = TextUtils.toLowerCase((String)Args.notBlank(mimeType, "MIME type"));
/* 389 */     Args.check(valid(type), "MIME type may not contain reserved characters");
/* 390 */     return create(mimeType, params, true);
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
/*     */   public static ContentType parse(CharSequence s) throws UnsupportedCharsetException {
/* 402 */     return parse(s, true);
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
/*     */   public static ContentType parseLenient(CharSequence s) throws UnsupportedCharsetException {
/* 414 */     return parse(s, false);
/*     */   }
/*     */   
/*     */   private static ContentType parse(CharSequence s, boolean strict) throws UnsupportedCharsetException {
/* 418 */     if (TextUtils.isBlank(s)) {
/* 419 */       return null;
/*     */     }
/* 421 */     ParserCursor cursor = new ParserCursor(0, s.length());
/* 422 */     HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(s, cursor);
/* 423 */     if (elements.length > 0) {
/* 424 */       return create(elements[0], strict);
/*     */     }
/* 426 */     return null;
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
/*     */   @Deprecated
/*     */   public static ContentType getByMimeType(String mimeType) {
/* 441 */     if (mimeType == null) {
/* 442 */       return null;
/*     */     }
/* 444 */     return CONTENT_TYPE_MAP.get(mimeType);
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
/*     */   public static Charset getCharset(ContentType contentType, Charset defaultCharset) {
/* 456 */     return (contentType != null) ? contentType.getCharset(defaultCharset) : defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentType withCharset(Charset charset) {
/* 467 */     return create(getMimeType(), charset);
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
/*     */   public ContentType withCharset(String charset) {
/* 480 */     return create(getMimeType(), charset);
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
/*     */   public ContentType withParameters(NameValuePair... params) throws UnsupportedCharsetException {
/* 492 */     if (params.length == 0) {
/* 493 */       return this;
/*     */     }
/* 495 */     Map<String, String> paramMap = new LinkedHashMap<>();
/* 496 */     if (this.params != null) {
/* 497 */       for (NameValuePair param : this.params) {
/* 498 */         paramMap.put(param.getName(), param.getValue());
/*     */       }
/*     */     }
/* 501 */     for (NameValuePair param : params) {
/* 502 */       paramMap.put(param.getName(), param.getValue());
/*     */     }
/* 504 */     List<NameValuePair> newParams = new ArrayList<>(paramMap.size() + 1);
/* 505 */     if (this.charset != null && !paramMap.containsKey("charset")) {
/* 506 */       newParams.add(new BasicNameValuePair("charset", this.charset.name()));
/*     */     }
/* 508 */     for (Map.Entry<String, String> entry : paramMap.entrySet()) {
/* 509 */       newParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
/*     */     }
/* 511 */     return create(getMimeType(), newParams.<NameValuePair>toArray(EMPTY_NAME_VALUE_PAIR_ARRAY), true);
/*     */   }
/*     */   
/*     */   public boolean isSameMimeType(ContentType contentType) {
/* 515 */     return (contentType != null && this.mimeType.equalsIgnoreCase(contentType.getMimeType()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ContentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */