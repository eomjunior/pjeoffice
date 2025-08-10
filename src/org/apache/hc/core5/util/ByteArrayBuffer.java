/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteArrayBuffer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4359112959524048036L;
/*     */   private byte[] array;
/*     */   private int len;
/*     */   
/*     */   public ByteArrayBuffer(int capacity) {
/*  53 */     Args.notNegative(capacity, "Buffer capacity");
/*  54 */     this.array = new byte[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  58 */     byte[] newArray = new byte[Math.max(this.array.length << 1, newlen)];
/*  59 */     System.arraycopy(this.array, 0, newArray, 0, this.len);
/*  60 */     this.array = newArray;
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
/*     */   public void append(byte[] b, int off, int len) {
/*  76 */     if (b == null) {
/*     */       return;
/*     */     }
/*  79 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  81 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  83 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  86 */     int newlen = this.len + len;
/*  87 */     if (newlen > this.array.length) {
/*  88 */       expand(newlen);
/*     */     }
/*  90 */     System.arraycopy(b, off, this.array, this.len, len);
/*  91 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(int b) {
/* 101 */     int newlen = this.len + 1;
/* 102 */     if (newlen > this.array.length) {
/* 103 */       expand(newlen);
/*     */     }
/* 105 */     this.array[this.len] = (byte)b;
/* 106 */     this.len = newlen;
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
/*     */   public void append(char[] b, int off, int len) {
/* 124 */     if (b == null) {
/*     */       return;
/*     */     }
/* 127 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 129 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 131 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 134 */     int oldlen = this.len;
/* 135 */     int newlen = oldlen + len;
/* 136 */     if (newlen > this.array.length) {
/* 137 */       expand(newlen);
/*     */     }
/*     */     
/* 140 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 141 */       int c = b[i1];
/* 142 */       if ((c >= 32 && c <= 126) || (c >= 160 && c <= 255) || c == 9) {
/*     */ 
/*     */         
/* 145 */         this.array[i2] = (byte)c;
/*     */       } else {
/* 147 */         this.array[i2] = 63;
/*     */       } 
/*     */     } 
/* 150 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 169 */     if (b == null) {
/*     */       return;
/*     */     }
/* 172 */     append(b.array(), off, len);
/*     */   }
/*     */   
/*     */   public void append(ByteBuffer buffer) {
/* 176 */     if (buffer == null) {
/*     */       return;
/*     */     }
/* 179 */     int bufferLength = buffer.remaining();
/* 180 */     if (bufferLength > 0) {
/* 181 */       int newLength = this.len + bufferLength;
/* 182 */       if (newLength > this.array.length) {
/* 183 */         expand(newLength);
/*     */       }
/* 185 */       buffer.get(this.array, this.len, bufferLength);
/* 186 */       this.len = newLength;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 194 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 203 */     byte[] b = new byte[this.len];
/* 204 */     if (this.len > 0) {
/* 205 */       System.arraycopy(this.array, 0, b, 0, this.len);
/*     */     }
/* 207 */     return b;
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
/*     */   public int byteAt(int i) {
/* 221 */     return this.array[i];
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
/* 232 */     return this.array.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 241 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 255 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 258 */     int available = this.array.length - this.len;
/* 259 */     if (required > available) {
/* 260 */       expand(this.len + required);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] array() {
/* 270 */     return this.array;
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
/* 284 */     if (len < 0 || len > this.array.length) {
/* 285 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.array.length);
/*     */     }
/* 287 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 297 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 307 */     return (this.len == this.array.length);
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
/*     */ 
/*     */   
/*     */   public int indexOf(byte b, int from, int to) {
/* 334 */     int beginIndex = from;
/* 335 */     if (beginIndex < 0) {
/* 336 */       beginIndex = 0;
/*     */     }
/* 338 */     int endIndex = to;
/* 339 */     if (endIndex > this.len) {
/* 340 */       endIndex = this.len;
/*     */     }
/* 342 */     if (beginIndex > endIndex) {
/* 343 */       return -1;
/*     */     }
/* 345 */     for (int i = beginIndex; i < endIndex; i++) {
/* 346 */       if (this.array[i] == b) {
/* 347 */         return i;
/*     */       }
/*     */     } 
/* 350 */     return -1;
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
/*     */   public int indexOf(byte b) {
/* 366 */     return indexOf(b, 0, this.len);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/ByteArrayBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */