/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.ProtocolException;
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
/*     */ public class HeaderGroup
/*     */   implements MessageHeaders, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2608834160639271617L;
/*  53 */   private static final Header[] EMPTY = new Header[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final List<Header> headers = new ArrayList<>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  70 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  80 */     if (header == null) {
/*     */       return;
/*     */     }
/*  83 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeHeader(Header header) {
/*  93 */     if (header == null) {
/*  94 */       return false;
/*     */     }
/*  96 */     for (int i = 0; i < this.headers.size(); i++) {
/*  97 */       Header current = this.headers.get(i);
/*  98 */       if (headerEquals(header, current)) {
/*  99 */         this.headers.remove(current);
/* 100 */         return true;
/*     */       } 
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   private boolean headerEquals(Header header1, Header header2) {
/* 107 */     return (header2 == header1 || (header2.getName().equalsIgnoreCase(header1.getName()) && 
/* 108 */       Objects.equals(header1.getValue(), header2.getValue())));
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
/*     */   public boolean removeHeaders(Header header) {
/* 120 */     if (header == null) {
/* 121 */       return false;
/*     */     }
/* 123 */     boolean removed = false;
/* 124 */     for (Iterator<Header> iterator = headerIterator(); iterator.hasNext(); ) {
/* 125 */       Header current = iterator.next();
/* 126 */       if (headerEquals(header, current)) {
/* 127 */         iterator.remove();
/* 128 */         removed = true;
/*     */       } 
/*     */     } 
/* 131 */     return removed;
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
/*     */   public void setHeader(Header header) {
/* 144 */     if (header == null) {
/*     */       return;
/*     */     }
/* 147 */     for (int i = 0; i < this.headers.size(); i++) {
/* 148 */       Header current = this.headers.get(i);
/* 149 */       if (current.getName().equalsIgnoreCase(header.getName())) {
/* 150 */         this.headers.set(i, header);
/*     */         return;
/*     */       } 
/*     */     } 
/* 154 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Header... headers) {
/* 165 */     clear();
/* 166 */     if (headers == null) {
/*     */       return;
/*     */     }
/* 169 */     Collections.addAll(this.headers, headers);
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
/*     */   public Header getCondensedHeader(String name) {
/* 184 */     Header[] hdrs = getHeaders(name);
/*     */     
/* 186 */     if (hdrs.length == 0)
/* 187 */       return null; 
/* 188 */     if (hdrs.length == 1) {
/* 189 */       return hdrs[0];
/*     */     }
/* 191 */     CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
/* 192 */     valueBuffer.append(hdrs[0].getValue());
/* 193 */     for (int i = 1; i < hdrs.length; i++) {
/* 194 */       valueBuffer.append(", ");
/* 195 */       valueBuffer.append(hdrs[i].getValue());
/*     */     } 
/*     */     
/* 198 */     return new BasicHeader(TextUtils.toLowerCase(name), valueBuffer.toString());
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
/*     */   public Header[] getHeaders(String name) {
/* 214 */     List<Header> headersFound = null;
/* 215 */     for (int i = 0; i < this.headers.size(); i++) {
/* 216 */       Header header = this.headers.get(i);
/* 217 */       if (header.getName().equalsIgnoreCase(name)) {
/* 218 */         if (headersFound == null) {
/* 219 */           headersFound = new ArrayList<>();
/*     */         }
/* 221 */         headersFound.add(header);
/*     */       } 
/*     */     } 
/* 224 */     return (headersFound != null) ? headersFound.<Header>toArray(EMPTY) : EMPTY;
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
/*     */   public Header getFirstHeader(String name) {
/* 237 */     for (int i = 0; i < this.headers.size(); i++) {
/* 238 */       Header header = this.headers.get(i);
/* 239 */       if (header.getName().equalsIgnoreCase(name)) {
/* 240 */         return header;
/*     */       }
/*     */     } 
/* 243 */     return null;
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
/*     */   public Header getHeader(String name) throws ProtocolException {
/* 257 */     int count = 0;
/* 258 */     Header singleHeader = null;
/* 259 */     for (int i = 0; i < this.headers.size(); i++) {
/* 260 */       Header header = this.headers.get(i);
/* 261 */       if (header.getName().equalsIgnoreCase(name)) {
/* 262 */         singleHeader = header;
/* 263 */         count++;
/*     */       } 
/*     */     } 
/* 266 */     if (count > 1) {
/* 267 */       throw new ProtocolException("multiple '%s' headers found", new Object[] { name });
/*     */     }
/* 269 */     return singleHeader;
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
/*     */   public Header getLastHeader(String name) {
/* 283 */     for (int i = this.headers.size() - 1; i >= 0; i--) {
/* 284 */       Header header = this.headers.get(i);
/* 285 */       if (header.getName().equalsIgnoreCase(name)) {
/* 286 */         return header;
/*     */       }
/*     */     } 
/*     */     
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getHeaders() {
/* 300 */     return this.headers.<Header>toArray(EMPTY);
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
/*     */   public boolean containsHeader(String name) {
/* 314 */     for (int i = 0; i < this.headers.size(); i++) {
/* 315 */       Header header = this.headers.get(i);
/* 316 */       if (header.getName().equalsIgnoreCase(name)) {
/* 317 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 321 */     return false;
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
/*     */   public int countHeaders(String name) {
/* 333 */     int count = 0;
/* 334 */     for (int i = 0; i < this.headers.size(); i++) {
/* 335 */       Header header = this.headers.get(i);
/* 336 */       if (header.getName().equalsIgnoreCase(name)) {
/* 337 */         count++;
/*     */       }
/*     */     } 
/* 340 */     return count;
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
/*     */   public Iterator<Header> headerIterator() {
/* 352 */     return new BasicListHeaderIterator(this.headers, null);
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
/*     */   public Iterator<Header> headerIterator(String name) {
/* 367 */     return new BasicListHeaderIterator(this.headers, name);
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
/*     */   public boolean removeHeaders(String name) {
/* 379 */     if (name == null) {
/* 380 */       return false;
/*     */     }
/* 382 */     boolean removed = false;
/* 383 */     for (Iterator<Header> iterator = headerIterator(); iterator.hasNext(); ) {
/* 384 */       Header header = iterator.next();
/* 385 */       if (header.getName().equalsIgnoreCase(name)) {
/* 386 */         iterator.remove();
/* 387 */         removed = true;
/*     */       } 
/*     */     } 
/* 390 */     return removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 395 */     return this.headers.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/HeaderGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */