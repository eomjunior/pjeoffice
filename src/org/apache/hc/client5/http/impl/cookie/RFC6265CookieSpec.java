/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.Cookie;
/*     */ import org.apache.hc.client5.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookiePriorityComparator;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.message.BufferedHeader;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class RFC6265CookieSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private static final char PARAM_DELIMITER = ';';
/*     */   private static final char COMMA_CHAR = ',';
/*     */   private static final char EQUAL_CHAR = '=';
/*     */   private static final char DQUOTE_CHAR = '"';
/*     */   private static final char ESCAPE_CHAR = '\\';
/*  73 */   private static final BitSet TOKEN_DELIMS = Tokenizer.INIT_BITSET(new int[] { 61, 59 });
/*  74 */   private static final BitSet VALUE_DELIMS = Tokenizer.INIT_BITSET(new int[] { 59 });
/*  75 */   private static final BitSet SPECIAL_CHARS = Tokenizer.INIT_BITSET(new int[] { 32, 34, 44, 59, 92 });
/*     */   
/*     */   private final CookieAttributeHandler[] attribHandlers;
/*     */   
/*     */   private final Map<String, CookieAttributeHandler> attribHandlerMap;
/*     */   
/*     */   private final Tokenizer tokenParser;
/*     */   
/*     */   protected RFC6265CookieSpec(CommonCookieAttributeHandler... handlers) {
/*  84 */     this.attribHandlers = (CookieAttributeHandler[])handlers.clone();
/*  85 */     this.attribHandlerMap = new ConcurrentHashMap<>(handlers.length);
/*  86 */     for (CommonCookieAttributeHandler handler : handlers) {
/*  87 */       this.attribHandlerMap.put(handler.getAttributeName().toLowerCase(Locale.ROOT), handler);
/*     */     }
/*  89 */     this.tokenParser = Tokenizer.INSTANCE;
/*     */   }
/*     */   
/*     */   static String getDefaultPath(CookieOrigin origin) {
/*  93 */     String defaultPath = origin.getPath();
/*  94 */     int lastSlashIndex = defaultPath.lastIndexOf('/');
/*  95 */     if (lastSlashIndex >= 0) {
/*  96 */       if (lastSlashIndex == 0)
/*     */       {
/*  98 */         lastSlashIndex = 1;
/*     */       }
/* 100 */       defaultPath = defaultPath.substring(0, lastSlashIndex);
/*     */     } 
/* 102 */     return defaultPath;
/*     */   }
/*     */   
/*     */   static String getDefaultDomain(CookieOrigin origin) {
/* 106 */     return origin.getHost();
/*     */   }
/*     */   public final List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*     */     CharArrayBuffer buffer;
/*     */     Tokenizer.Cursor cursor;
/* 111 */     Args.notNull(header, "Header");
/* 112 */     Args.notNull(origin, "Cookie origin");
/* 113 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 114 */       throw new MalformedCookieException("Unrecognized cookie header: '" + header + "'");
/*     */     }
/*     */ 
/*     */     
/* 118 */     if (header instanceof FormattedHeader) {
/* 119 */       buffer = ((FormattedHeader)header).getBuffer();
/* 120 */       cursor = new Tokenizer.Cursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */     } else {
/* 122 */       String s = header.getValue();
/* 123 */       if (s == null) {
/* 124 */         throw new MalformedCookieException("Header value is null");
/*     */       }
/* 126 */       buffer = new CharArrayBuffer(s.length());
/* 127 */       buffer.append(s);
/* 128 */       cursor = new Tokenizer.Cursor(0, buffer.length());
/*     */     } 
/* 130 */     String name = this.tokenParser.parseToken((CharSequence)buffer, cursor, TOKEN_DELIMS);
/* 131 */     if (name.isEmpty()) {
/* 132 */       return Collections.emptyList();
/*     */     }
/* 134 */     if (cursor.atEnd()) {
/* 135 */       return Collections.emptyList();
/*     */     }
/* 137 */     int valueDelim = buffer.charAt(cursor.getPos());
/* 138 */     cursor.updatePos(cursor.getPos() + 1);
/* 139 */     if (valueDelim != 61) {
/* 140 */       throw new MalformedCookieException("Cookie value is invalid: '" + header + "'");
/*     */     }
/* 142 */     String value = this.tokenParser.parseValue((CharSequence)buffer, cursor, VALUE_DELIMS);
/* 143 */     if (!cursor.atEnd()) {
/* 144 */       cursor.updatePos(cursor.getPos() + 1);
/*     */     }
/* 146 */     BasicClientCookie cookie = new BasicClientCookie(name, value);
/* 147 */     cookie.setPath(getDefaultPath(origin));
/* 148 */     cookie.setDomain(getDefaultDomain(origin));
/* 149 */     cookie.setCreationDate(Instant.now());
/*     */     
/* 151 */     Map<String, String> attribMap = new LinkedHashMap<>();
/* 152 */     while (!cursor.atEnd()) {
/*     */       
/* 154 */       String paramName = this.tokenParser.parseToken((CharSequence)buffer, cursor, TOKEN_DELIMS).toLowerCase(Locale.ROOT);
/* 155 */       String paramValue = null;
/* 156 */       if (!cursor.atEnd()) {
/* 157 */         int paramDelim = buffer.charAt(cursor.getPos());
/* 158 */         cursor.updatePos(cursor.getPos() + 1);
/* 159 */         if (paramDelim == 61) {
/* 160 */           paramValue = this.tokenParser.parseToken((CharSequence)buffer, cursor, VALUE_DELIMS);
/* 161 */           if (!cursor.atEnd()) {
/* 162 */             cursor.updatePos(cursor.getPos() + 1);
/*     */           }
/*     */         } 
/*     */       } 
/* 166 */       cookie.setAttribute(paramName, paramValue);
/* 167 */       attribMap.put(paramName, paramValue);
/*     */     } 
/*     */     
/* 170 */     if (attribMap.containsKey("max-age")) {
/* 171 */       attribMap.remove("expires");
/*     */     }
/*     */     
/* 174 */     for (Map.Entry<String, String> entry : attribMap.entrySet()) {
/* 175 */       String paramName = entry.getKey();
/* 176 */       String paramValue = entry.getValue();
/* 177 */       CookieAttributeHandler handler = this.attribHandlerMap.get(paramName);
/* 178 */       if (handler != null) {
/* 179 */         handler.parse(cookie, paramValue);
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return (List)Collections.singletonList(cookie);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 189 */     Args.notNull(cookie, "Cookie");
/* 190 */     Args.notNull(origin, "Cookie origin");
/* 191 */     for (CookieAttributeHandler handler : this.attribHandlers) {
/* 192 */       handler.validate(cookie, origin);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean match(Cookie cookie, CookieOrigin origin) {
/* 198 */     Args.notNull(cookie, "Cookie");
/* 199 */     Args.notNull(origin, "Cookie origin");
/* 200 */     for (CookieAttributeHandler handler : this.attribHandlers) {
/* 201 */       if (!handler.match(cookie, origin)) {
/* 202 */         return false;
/*     */       }
/*     */     } 
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/*     */     List<? extends Cookie> sortedCookies;
/* 210 */     Args.notEmpty(cookies, "List of cookies");
/*     */     
/* 212 */     if (cookies.size() > 1) {
/*     */       
/* 214 */       sortedCookies = new ArrayList<>(cookies);
/* 215 */       sortedCookies.sort((Comparator<? super Cookie>)CookiePriorityComparator.INSTANCE);
/*     */     } else {
/* 217 */       sortedCookies = cookies;
/*     */     } 
/* 219 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * sortedCookies.size());
/* 220 */     buffer.append("Cookie");
/* 221 */     buffer.append(": ");
/* 222 */     for (int n = 0; n < sortedCookies.size(); n++) {
/* 223 */       Cookie cookie = sortedCookies.get(n);
/* 224 */       if (n > 0) {
/* 225 */         buffer.append(';');
/* 226 */         buffer.append(' ');
/*     */       } 
/* 228 */       buffer.append(cookie.getName());
/* 229 */       String s = cookie.getValue();
/* 230 */       if (s != null) {
/* 231 */         buffer.append('=');
/* 232 */         if (containsSpecialChar(s)) {
/* 233 */           buffer.append('"');
/* 234 */           for (int i = 0; i < s.length(); i++) {
/* 235 */             char ch = s.charAt(i);
/* 236 */             if (ch == '"' || ch == '\\') {
/* 237 */               buffer.append('\\');
/*     */             }
/* 239 */             buffer.append(ch);
/*     */           } 
/* 241 */           buffer.append('"');
/*     */         } else {
/* 243 */           buffer.append(s);
/*     */         } 
/*     */       } 
/*     */     } 
/* 247 */     List<Header> headers = new ArrayList<>(1);
/*     */     try {
/* 249 */       headers.add(new BufferedHeader(buffer));
/* 250 */     } catch (ParseException parseException) {}
/*     */ 
/*     */     
/* 253 */     return headers;
/*     */   }
/*     */   
/*     */   boolean containsSpecialChar(CharSequence s) {
/* 257 */     return containsChars(s, SPECIAL_CHARS);
/*     */   }
/*     */   
/*     */   boolean containsChars(CharSequence s, BitSet chars) {
/* 261 */     for (int i = 0; i < s.length(); i++) {
/* 262 */       char ch = s.charAt(i);
/* 263 */       if (chars.get(ch)) {
/* 264 */         return true;
/*     */       }
/*     */     } 
/* 267 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/RFC6265CookieSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */