/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class Murmur3_32HashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  56 */   static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0, false);
/*     */   
/*  58 */   static final HashFunction MURMUR3_32_FIXED = new Murmur3_32HashFunction(0, true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   static final HashFunction GOOD_FAST_HASH_32 = new Murmur3_32HashFunction(Hashing.GOOD_FAST_HASH_SEED, true);
/*     */   
/*     */   private static final int CHUNK_SIZE = 4;
/*     */   
/*     */   private static final int C1 = -862048943;
/*     */   
/*     */   private static final int C2 = 461845907;
/*     */   private final int seed;
/*     */   private final boolean supplementaryPlaneFix;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_32HashFunction(int seed, boolean supplementaryPlaneFix) {
/*  75 */     this.seed = seed;
/*  76 */     this.supplementaryPlaneFix = supplementaryPlaneFix;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  81 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  86 */     return new Murmur3_32Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     return "Hashing.murmur3_32(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/*  96 */     if (object instanceof Murmur3_32HashFunction) {
/*  97 */       Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
/*  98 */       return (this.seed == other.seed && this.supplementaryPlaneFix == other.supplementaryPlaneFix);
/*     */     } 
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 105 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/* 110 */     int k1 = mixK1(input);
/* 111 */     int h1 = mixH1(this.seed, k1);
/*     */     
/* 113 */     return fmix(h1, 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/* 118 */     int low = (int)input;
/* 119 */     int high = (int)(input >>> 32L);
/*     */     
/* 121 */     int k1 = mixK1(low);
/* 122 */     int h1 = mixH1(this.seed, k1);
/*     */     
/* 124 */     k1 = mixK1(high);
/* 125 */     h1 = mixH1(h1, k1);
/*     */     
/* 127 */     return fmix(h1, 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/* 132 */     int h1 = this.seed;
/*     */ 
/*     */     
/* 135 */     for (int i = 1; i < input.length(); i += 2) {
/* 136 */       int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
/* 137 */       k1 = mixK1(k1);
/* 138 */       h1 = mixH1(h1, k1);
/*     */     } 
/*     */ 
/*     */     
/* 142 */     if ((input.length() & 0x1) == 1) {
/* 143 */       int k1 = input.charAt(input.length() - 1);
/* 144 */       k1 = mixK1(k1);
/* 145 */       h1 ^= k1;
/*     */     } 
/*     */     
/* 148 */     return fmix(h1, 2 * input.length());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/* 154 */     if (Charsets.UTF_8.equals(charset)) {
/* 155 */       int utf16Length = input.length();
/* 156 */       int h1 = this.seed;
/* 157 */       int i = 0;
/* 158 */       int len = 0;
/*     */ 
/*     */       
/* 161 */       while (i + 4 <= utf16Length) {
/* 162 */         char c0 = input.charAt(i);
/* 163 */         char c1 = input.charAt(i + 1);
/* 164 */         char c2 = input.charAt(i + 2);
/* 165 */         char c3 = input.charAt(i + 3);
/* 166 */         if (c0 < '' && c1 < '' && c2 < '' && c3 < '') {
/* 167 */           int j = c0 | c1 << 8 | c2 << 16 | c3 << 24;
/* 168 */           j = mixK1(j);
/* 169 */           h1 = mixH1(h1, j);
/* 170 */           i += 4;
/* 171 */           len += 4;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 177 */       long buffer = 0L;
/* 178 */       int shift = 0;
/* 179 */       for (; i < utf16Length; i++) {
/* 180 */         char c = input.charAt(i);
/* 181 */         if (c < '') {
/* 182 */           buffer |= c << shift;
/* 183 */           shift += 8;
/* 184 */           len++;
/* 185 */         } else if (c < 'ࠀ') {
/* 186 */           buffer |= charToTwoUtf8Bytes(c) << shift;
/* 187 */           shift += 16;
/* 188 */           len += 2;
/* 189 */         } else if (c < '?' || c > '?') {
/* 190 */           buffer |= charToThreeUtf8Bytes(c) << shift;
/* 191 */           shift += 24;
/* 192 */           len += 3;
/*     */         } else {
/* 194 */           int codePoint = Character.codePointAt(input, i);
/* 195 */           if (codePoint == c)
/*     */           {
/* 197 */             return hashBytes(input.toString().getBytes(charset));
/*     */           }
/* 199 */           i++;
/* 200 */           buffer |= codePointToFourUtf8Bytes(codePoint) << shift;
/* 201 */           if (this.supplementaryPlaneFix) {
/* 202 */             shift += 32;
/*     */           }
/* 204 */           len += 4;
/*     */         } 
/*     */         
/* 207 */         if (shift >= 32) {
/* 208 */           int j = mixK1((int)buffer);
/* 209 */           h1 = mixH1(h1, j);
/* 210 */           buffer >>>= 32L;
/* 211 */           shift -= 32;
/*     */         } 
/*     */       } 
/*     */       
/* 215 */       int k1 = mixK1((int)buffer);
/* 216 */       h1 ^= k1;
/* 217 */       return fmix(h1, len);
/*     */     } 
/* 219 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/* 225 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/* 226 */     int h1 = this.seed;
/*     */     int i;
/* 228 */     for (i = 0; i + 4 <= len; i += 4) {
/* 229 */       int j = mixK1(getIntLittleEndian(input, off + i));
/* 230 */       h1 = mixH1(h1, j);
/*     */     } 
/*     */     
/* 233 */     int k1 = 0;
/* 234 */     for (int shift = 0; i < len; i++, shift += 8) {
/* 235 */       k1 ^= UnsignedBytes.toInt(input[off + i]) << shift;
/*     */     }
/* 237 */     h1 ^= mixK1(k1);
/* 238 */     return fmix(h1, len);
/*     */   }
/*     */   
/*     */   private static int getIntLittleEndian(byte[] input, int offset) {
/* 242 */     return Ints.fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
/*     */   }
/*     */   
/*     */   private static int mixK1(int k1) {
/* 246 */     k1 *= -862048943;
/* 247 */     k1 = Integer.rotateLeft(k1, 15);
/* 248 */     k1 *= 461845907;
/* 249 */     return k1;
/*     */   }
/*     */   
/*     */   private static int mixH1(int h1, int k1) {
/* 253 */     h1 ^= k1;
/* 254 */     h1 = Integer.rotateLeft(h1, 13);
/* 255 */     h1 = h1 * 5 + -430675100;
/* 256 */     return h1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashCode fmix(int h1, int length) {
/* 261 */     h1 ^= length;
/* 262 */     h1 ^= h1 >>> 16;
/* 263 */     h1 *= -2048144789;
/* 264 */     h1 ^= h1 >>> 13;
/* 265 */     h1 *= -1028477387;
/* 266 */     h1 ^= h1 >>> 16;
/* 267 */     return HashCode.fromInt(h1);
/*     */   }
/*     */   
/*     */   private static final class Murmur3_32Hasher extends AbstractHasher {
/*     */     private int h1;
/*     */     private long buffer;
/*     */     private int shift;
/*     */     private int length;
/*     */     private boolean isDone;
/*     */     
/*     */     Murmur3_32Hasher(int seed) {
/* 278 */       this.h1 = seed;
/* 279 */       this.length = 0;
/* 280 */       this.isDone = false;
/*     */     }
/*     */ 
/*     */     
/*     */     private void update(int nBytes, long update) {
/* 285 */       this.buffer |= (update & 0xFFFFFFFFL) << this.shift;
/* 286 */       this.shift += nBytes * 8;
/* 287 */       this.length += nBytes;
/*     */       
/* 289 */       if (this.shift >= 32) {
/* 290 */         this.h1 = Murmur3_32HashFunction.mixH1(this.h1, Murmur3_32HashFunction.mixK1((int)this.buffer));
/* 291 */         this.buffer >>>= 32L;
/* 292 */         this.shift -= 32;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putByte(byte b) {
/* 299 */       update(1, (b & 0xFF));
/* 300 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/* 306 */       Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*     */       int i;
/* 308 */       for (i = 0; i + 4 <= len; i += 4) {
/* 309 */         update(4, Murmur3_32HashFunction.getIntLittleEndian(bytes, off + i));
/*     */       }
/* 311 */       for (; i < len; i++) {
/* 312 */         putByte(bytes[off + i]);
/*     */       }
/* 314 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putBytes(ByteBuffer buffer) {
/* 320 */       ByteOrder bo = buffer.order();
/* 321 */       buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 322 */       while (buffer.remaining() >= 4) {
/* 323 */         putInt(buffer.getInt());
/*     */       }
/* 325 */       while (buffer.hasRemaining()) {
/* 326 */         putByte(buffer.get());
/*     */       }
/* 328 */       buffer.order(bo);
/* 329 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putInt(int i) {
/* 335 */       update(4, i);
/* 336 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putLong(long l) {
/* 342 */       update(4, (int)l);
/* 343 */       update(4, l >>> 32L);
/* 344 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putChar(char c) {
/* 350 */       update(2, c);
/* 351 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Hasher putString(CharSequence input, Charset charset) {
/* 358 */       if (Charsets.UTF_8.equals(charset)) {
/* 359 */         int utf16Length = input.length();
/* 360 */         int i = 0;
/*     */ 
/*     */         
/* 363 */         while (i + 4 <= utf16Length) {
/* 364 */           char c0 = input.charAt(i);
/* 365 */           char c1 = input.charAt(i + 1);
/* 366 */           char c2 = input.charAt(i + 2);
/* 367 */           char c3 = input.charAt(i + 3);
/* 368 */           if (c0 < '' && c1 < '' && c2 < '' && c3 < '') {
/* 369 */             update(4, (c0 | c1 << 8 | c2 << 16 | c3 << 24));
/* 370 */             i += 4;
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 376 */         for (; i < utf16Length; i++) {
/* 377 */           char c = input.charAt(i);
/* 378 */           if (c < '') {
/* 379 */             update(1, c);
/* 380 */           } else if (c < 'ࠀ') {
/* 381 */             update(2, Murmur3_32HashFunction.charToTwoUtf8Bytes(c));
/* 382 */           } else if (c < '?' || c > '?') {
/* 383 */             update(3, Murmur3_32HashFunction.charToThreeUtf8Bytes(c));
/*     */           } else {
/* 385 */             int codePoint = Character.codePointAt(input, i);
/* 386 */             if (codePoint == c) {
/*     */               
/* 388 */               putBytes(input.subSequence(i, utf16Length).toString().getBytes(charset));
/* 389 */               return this;
/*     */             } 
/* 391 */             i++;
/* 392 */             update(4, Murmur3_32HashFunction.codePointToFourUtf8Bytes(codePoint));
/*     */           } 
/*     */         } 
/* 395 */         return this;
/*     */       } 
/* 397 */       return super.putString(input, charset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 403 */       Preconditions.checkState(!this.isDone);
/* 404 */       this.isDone = true;
/* 405 */       this.h1 ^= Murmur3_32HashFunction.mixK1((int)this.buffer);
/* 406 */       return Murmur3_32HashFunction.fmix(this.h1, this.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static long codePointToFourUtf8Bytes(int codePoint) {
/* 412 */     return 0xF0L | (codePoint >>> 18) | (0x80L | (0x3F & codePoint >>> 12)) << 8L | (0x80L | (0x3F & codePoint >>> 6)) << 16L | (0x80L | (0x3F & codePoint)) << 24L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long charToThreeUtf8Bytes(char c) {
/* 419 */     return 0xE0L | (c >>> 12) | ((0x80 | 0x3F & c >>> 6) << 8) | ((0x80 | 0x3F & c) << 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long charToTwoUtf8Bytes(char c) {
/* 426 */     return 0xC0L | (c >>> 6) | ((0x80 | 0x3F & c) << 8);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Murmur3_32HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */