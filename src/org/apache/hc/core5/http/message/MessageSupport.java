/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ public class MessageSupport
/*     */ {
/*  60 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void formatTokens(CharArrayBuffer dst, String... tokens) {
/*  67 */     Args.notNull(dst, "Destination");
/*  68 */     for (int i = 0; i < tokens.length; i++) {
/*  69 */       String element = tokens[i];
/*  70 */       if (i > 0) {
/*  71 */         dst.append(", ");
/*     */       }
/*  73 */       dst.append(element);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void formatTokens(CharArrayBuffer dst, Set<String> tokens) {
/*  78 */     Args.notNull(dst, "Destination");
/*  79 */     if (tokens == null || tokens.isEmpty()) {
/*     */       return;
/*     */     }
/*  82 */     formatTokens(dst, tokens.<String>toArray(EMPTY_STRING_ARRAY));
/*     */   }
/*     */   
/*     */   public static Header format(String name, Set<String> tokens) {
/*  86 */     Args.notBlank(name, "Header name");
/*  87 */     if (tokens == null || tokens.isEmpty()) {
/*  88 */       return null;
/*     */     }
/*  90 */     CharArrayBuffer buffer = new CharArrayBuffer(256);
/*  91 */     buffer.append(name);
/*  92 */     buffer.append(": ");
/*  93 */     formatTokens(buffer, tokens);
/*  94 */     return (Header)BufferedHeader.create(buffer);
/*     */   }
/*     */   
/*     */   public static Header format(String name, String... tokens) {
/*  98 */     Args.notBlank(name, "Header name");
/*  99 */     if (tokens == null || tokens.length == 0) {
/* 100 */       return null;
/*     */     }
/* 102 */     CharArrayBuffer buffer = new CharArrayBuffer(256);
/* 103 */     buffer.append(name);
/* 104 */     buffer.append(": ");
/* 105 */     formatTokens(buffer, tokens);
/* 106 */     return (Header)BufferedHeader.create(buffer);
/*     */   }
/*     */   
/* 109 */   private static final BitSet COMMA = Tokenizer.INIT_BITSET(new int[] { 44 });
/*     */   
/*     */   public static Set<String> parseTokens(CharSequence src, ParserCursor cursor) {
/* 112 */     Args.notNull(src, "Source");
/* 113 */     Args.notNull(cursor, "Cursor");
/* 114 */     Set<String> tokens = new LinkedHashSet<>();
/* 115 */     while (!cursor.atEnd()) {
/* 116 */       int pos = cursor.getPos();
/* 117 */       if (src.charAt(pos) == ',') {
/* 118 */         cursor.updatePos(pos + 1);
/*     */       }
/* 120 */       String token = Tokenizer.INSTANCE.parseToken(src, cursor, COMMA);
/* 121 */       if (!TextUtils.isBlank(token)) {
/* 122 */         tokens.add(token);
/*     */       }
/*     */     } 
/* 125 */     return tokens;
/*     */   }
/*     */   
/*     */   public static Set<String> parseTokens(Header header) {
/* 129 */     Args.notNull(header, "Header");
/* 130 */     if (header instanceof FormattedHeader) {
/* 131 */       CharArrayBuffer buf = ((FormattedHeader)header).getBuffer();
/* 132 */       ParserCursor parserCursor = new ParserCursor(0, buf.length());
/* 133 */       parserCursor.updatePos(((FormattedHeader)header).getValuePos());
/* 134 */       return parseTokens((CharSequence)buf, parserCursor);
/*     */     } 
/* 136 */     String value = header.getValue();
/* 137 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 138 */     return parseTokens(value, cursor);
/*     */   }
/*     */   
/*     */   public static void addContentTypeHeader(HttpMessage message, EntityDetails entity) {
/* 142 */     if (entity != null && entity.getContentType() != null && !message.containsHeader("Content-Type")) {
/* 143 */       message.addHeader(new BasicHeader("Content-Type", entity.getContentType()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addContentEncodingHeader(HttpMessage message, EntityDetails entity) {
/* 148 */     if (entity != null && entity.getContentEncoding() != null && !message.containsHeader("Content-Encoding")) {
/* 149 */       message.addHeader(new BasicHeader("Content-Encoding", entity.getContentEncoding()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addTrailerHeader(HttpMessage message, EntityDetails entity) {
/* 154 */     if (entity != null && !message.containsHeader("Trailer")) {
/* 155 */       Set<String> trailerNames = entity.getTrailerNames();
/* 156 */       if (trailerNames != null && !trailerNames.isEmpty()) {
/* 157 */         message.setHeader(format("Trailer", trailerNames));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Iterator<HeaderElement> iterate(MessageHeaders headers, String name) {
/* 163 */     Args.notNull(headers, "Message headers");
/* 164 */     Args.notBlank(name, "Header name");
/* 165 */     return new BasicHeaderElementIterator(headers.headerIterator(name));
/*     */   }
/*     */   
/*     */   public static HeaderElement[] parse(Header header) {
/* 169 */     Args.notNull(header, "Headers");
/* 170 */     String value = header.getValue();
/* 171 */     if (value == null) {
/* 172 */       return new HeaderElement[0];
/*     */     }
/* 174 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 175 */     return BasicHeaderValueParser.INSTANCE.parseElements(value, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canResponseHaveBody(String method, HttpResponse response) {
/* 182 */     if (Method.HEAD.isSame(method)) {
/* 183 */       return false;
/*     */     }
/* 185 */     int status = response.getCode();
/* 186 */     if (Method.CONNECT.isSame(method) && status == 200) {
/* 187 */       return false;
/*     */     }
/* 189 */     return (status >= 200 && status != 204 && status != 304);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/MessageSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */