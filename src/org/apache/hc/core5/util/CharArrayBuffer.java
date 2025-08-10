/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharArrayBuffer
/*     */   implements CharSequence, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6208952725094867135L;
/*     */   private char[] array;
/*     */   private int len;
/*     */   
/*     */   public CharArrayBuffer(int capacity) {
/*  55 */     Args.notNegative(capacity, "Buffer capacity");
/*  56 */     this.array = new char[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  60 */     char[] newArray = new char[Math.max(this.array.length << 1, newlen)];
/*  61 */     System.arraycopy(this.array, 0, newArray, 0, this.len);
/*  62 */     this.array = newArray;
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
/*     */   public void append(char[] b, int off, int len) {
/*  78 */     if (b == null) {
/*     */       return;
/*     */     }
/*  81 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  83 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  85 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  88 */     int newlen = this.len + len;
/*  89 */     if (newlen > this.array.length) {
/*  90 */       expand(newlen);
/*     */     }
/*  92 */     System.arraycopy(b, off, this.array, this.len, len);
/*  93 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(String str) {
/* 103 */     String s = (str != null) ? str : "null";
/* 104 */     int strlen = s.length();
/* 105 */     int newlen = this.len + strlen;
/* 106 */     if (newlen > this.array.length) {
/* 107 */       expand(newlen);
/*     */     }
/* 109 */     s.getChars(0, strlen, this.array, this.len);
/* 110 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 127 */     if (b == null) {
/*     */       return;
/*     */     }
/* 130 */     append(b.array, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(CharArrayBuffer b) {
/* 141 */     if (b == null) {
/*     */       return;
/*     */     }
/* 144 */     append(b.array, 0, b.len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(char ch) {
/* 154 */     int newlen = this.len + 1;
/* 155 */     if (newlen > this.array.length) {
/* 156 */       expand(newlen);
/*     */     }
/* 158 */     this.array[this.len] = ch;
/* 159 */     this.len = newlen;
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
/*     */   public void append(byte[] b, int off, int len) {
/* 177 */     if (b == null) {
/*     */       return;
/*     */     }
/* 180 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 182 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 184 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 187 */     int oldlen = this.len;
/* 188 */     int newlen = oldlen + len;
/* 189 */     if (newlen > this.array.length) {
/* 190 */       expand(newlen);
/*     */     }
/* 192 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 193 */       this.array[i2] = (char)(b[i1] & 0xFF);
/*     */     }
/* 195 */     this.len = newlen;
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
/*     */   public void append(ByteArrayBuffer b, int off, int len) {
/* 213 */     if (b == null) {
/*     */       return;
/*     */     }
/* 216 */     append(b.array(), off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(Object obj) {
/* 227 */     append(String.valueOf(obj));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 234 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] toCharArray() {
/* 243 */     char[] b = new char[this.len];
/* 244 */     if (this.len > 0) {
/* 245 */       System.arraycopy(this.array, 0, b, 0, this.len);
/*     */     }
/* 247 */     return b;
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
/*     */   public char charAt(int i) {
/* 262 */     return this.array[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] array() {
/* 271 */     return this.array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 282 */     return this.array.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 292 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 304 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 307 */     int available = this.array.length - this.len;
/* 308 */     if (required > available) {
/* 309 */       expand(this.len + required);
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
/*     */   public void setLength(int len) {
/* 324 */     if (len < 0 || len > this.array.length) {
/* 325 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.array.length);
/*     */     }
/* 327 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 337 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 347 */     return (this.len == this.array.length);
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
/*     */ 
/*     */   
/*     */   public int indexOf(int ch, int from, int to) {
/* 372 */     int beginIndex = from;
/* 373 */     if (beginIndex < 0) {
/* 374 */       beginIndex = 0;
/*     */     }
/* 376 */     int endIndex = to;
/* 377 */     if (endIndex > this.len) {
/* 378 */       endIndex = this.len;
/*     */     }
/* 380 */     if (beginIndex > endIndex) {
/* 381 */       return -1;
/*     */     }
/* 383 */     for (int i = beginIndex; i < endIndex; i++) {
/* 384 */       if (this.array[i] == ch) {
/* 385 */         return i;
/*     */       }
/*     */     } 
/* 388 */     return -1;
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
/*     */   public int indexOf(int ch) {
/* 402 */     return indexOf(ch, 0, this.len);
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
/*     */   public String substring(int beginIndex, int endIndex) {
/* 420 */     if (beginIndex < 0) {
/* 421 */       throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
/*     */     }
/* 423 */     if (endIndex > this.len) {
/* 424 */       throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
/*     */     }
/* 426 */     if (beginIndex > endIndex) {
/* 427 */       throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
/*     */     }
/* 429 */     return new String(this.array, beginIndex, endIndex - beginIndex);
/*     */   }
/*     */   
/*     */   private static boolean isWhitespace(char ch) {
/* 433 */     return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
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
/*     */   public String substringTrimmed(int beginIndex, int endIndex) {
/* 453 */     if (beginIndex < 0) {
/* 454 */       throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
/*     */     }
/* 456 */     if (endIndex > this.len) {
/* 457 */       throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
/*     */     }
/* 459 */     if (beginIndex > endIndex) {
/* 460 */       throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
/*     */     }
/* 462 */     int beginIndex0 = beginIndex;
/* 463 */     int endIndex0 = endIndex;
/* 464 */     while (beginIndex0 < endIndex && isWhitespace(this.array[beginIndex0])) {
/* 465 */       beginIndex0++;
/*     */     }
/* 467 */     while (endIndex0 > beginIndex0 && isWhitespace(this.array[endIndex0 - 1])) {
/* 468 */       endIndex0--;
/*     */     }
/* 470 */     return new String(this.array, beginIndex0, endIndex0 - beginIndex0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence subSequence(int beginIndex, int endIndex) {
/* 479 */     if (beginIndex < 0) {
/* 480 */       throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
/*     */     }
/* 482 */     if (endIndex > this.len) {
/* 483 */       throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
/*     */     }
/* 485 */     if (beginIndex > endIndex) {
/* 486 */       throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
/*     */     }
/* 488 */     return CharBuffer.wrap(this.array, beginIndex, endIndex - beginIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 493 */     return new String(this.array, 0, this.len);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/CharArrayBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */