/*     */ package org.apache.hc.core5.net;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Deprecated
/*     */ public class URLEncodedUtils
/*     */ {
/*     */   private static final char QP_SEP_A = '&';
/*     */   private static final char QP_SEP_S = ';';
/*     */   
/*     */   public static List<NameValuePair> parse(URI uri, Charset charset) {
/*  68 */     Args.notNull(uri, "URI");
/*  69 */     String query = uri.getRawQuery();
/*  70 */     if (query != null && !query.isEmpty()) {
/*  71 */       return parse(query, charset);
/*     */     }
/*  73 */     return new ArrayList<>(0);
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
/*     */   public static List<NameValuePair> parse(CharSequence s, Charset charset) {
/*  87 */     if (s == null) {
/*  88 */       return new ArrayList<>(0);
/*     */     }
/*  90 */     return parse(s, charset, new char[] { '&', ';' });
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
/*     */   public static List<NameValuePair> parse(CharSequence s, Charset charset, char... separators) {
/* 105 */     Args.notNull(s, "Char sequence");
/* 106 */     Tokenizer tokenParser = Tokenizer.INSTANCE;
/* 107 */     BitSet delimSet = new BitSet();
/* 108 */     for (char separator : separators) {
/* 109 */       delimSet.set(separator);
/*     */     }
/* 111 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/* 112 */     List<NameValuePair> list = new ArrayList<>();
/* 113 */     while (!cursor.atEnd()) {
/* 114 */       delimSet.set(61);
/* 115 */       String name = tokenParser.parseToken(s, cursor, delimSet);
/* 116 */       String value = null;
/* 117 */       if (!cursor.atEnd()) {
/* 118 */         int delim = s.charAt(cursor.getPos());
/* 119 */         cursor.updatePos(cursor.getPos() + 1);
/* 120 */         if (delim == 61) {
/* 121 */           delimSet.clear(61);
/* 122 */           value = tokenParser.parseToken(s, cursor, delimSet);
/* 123 */           if (!cursor.atEnd()) {
/* 124 */             cursor.updatePos(cursor.getPos() + 1);
/*     */           }
/*     */         } 
/*     */       } 
/* 128 */       if (!name.isEmpty()) {
/* 129 */         list.add(new BasicNameValuePair(
/* 130 */               PercentCodec.decode(name, charset, true), 
/* 131 */               PercentCodec.decode(value, charset, true)));
/*     */       }
/*     */     } 
/* 134 */     return list;
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
/*     */   public static List<String> parsePathSegments(CharSequence s, Charset charset) {
/* 147 */     return URIBuilder.parsePath(s, charset);
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
/*     */   public static List<String> parsePathSegments(CharSequence s) {
/* 159 */     return parsePathSegments(s, StandardCharsets.UTF_8);
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
/*     */   public static String formatSegments(Iterable<String> segments, Charset charset) {
/* 172 */     Args.notNull(segments, "Segments");
/* 173 */     StringBuilder buf = new StringBuilder();
/* 174 */     URIBuilder.formatPath(buf, segments, false, charset);
/* 175 */     return buf.toString();
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
/*     */   public static String formatSegments(String... segments) {
/* 187 */     return formatSegments(Arrays.asList(segments), StandardCharsets.UTF_8);
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
/*     */   public static String format(Iterable<? extends NameValuePair> parameters, char parameterSeparator, Charset charset) {
/* 205 */     Args.notNull(parameters, "Parameters");
/* 206 */     StringBuilder buf = new StringBuilder();
/* 207 */     int i = 0;
/* 208 */     for (NameValuePair parameter : parameters) {
/* 209 */       if (i > 0) {
/* 210 */         buf.append(parameterSeparator);
/*     */       }
/* 212 */       PercentCodec.encode(buf, parameter.getName(), charset, URL_ENCODER, true);
/* 213 */       if (parameter.getValue() != null) {
/* 214 */         buf.append('=');
/* 215 */         PercentCodec.encode(buf, parameter.getValue(), charset, URL_ENCODER, true);
/*     */       } 
/* 217 */       i++;
/*     */     } 
/* 219 */     return buf.toString();
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
/*     */   public static String format(Iterable<? extends NameValuePair> parameters, Charset charset) {
/* 235 */     return format(parameters, '&', charset);
/*     */   }
/*     */   
/* 238 */   private static final BitSet URL_ENCODER = new BitSet(256);
/*     */ 
/*     */   
/*     */   static {
/*     */     int i;
/* 243 */     for (i = 97; i <= 122; i++) {
/* 244 */       URL_ENCODER.set(i);
/*     */     }
/* 246 */     for (i = 65; i <= 90; i++) {
/* 247 */       URL_ENCODER.set(i);
/*     */     }
/*     */     
/* 250 */     for (i = 48; i <= 57; i++) {
/* 251 */       URL_ENCODER.set(i);
/*     */     }
/* 253 */     URL_ENCODER.set(95);
/* 254 */     URL_ENCODER.set(45);
/* 255 */     URL_ENCODER.set(46);
/* 256 */     URL_ENCODER.set(42);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/URLEncodedUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */