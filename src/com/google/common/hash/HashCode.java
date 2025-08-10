/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.UnsignedInts;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public abstract class HashCode
/*     */ {
/*     */   public abstract int bits();
/*     */   
/*     */   public abstract int asInt();
/*     */   
/*     */   public abstract long asLong();
/*     */   
/*     */   public abstract long padToLong();
/*     */   
/*     */   public abstract byte[] asBytes();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int writeBytesTo(byte[] dest, int offset, int maxLength) {
/*  86 */     maxLength = Ints.min(new int[] { maxLength, bits() / 8 });
/*  87 */     Preconditions.checkPositionIndexes(offset, offset + maxLength, dest.length);
/*  88 */     writeBytesToImpl(dest, offset, maxLength);
/*  89 */     return maxLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void writeBytesToImpl(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] getBytesInternal() {
/* 100 */     return asBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean equalsSameBits(HashCode paramHashCode);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromInt(int hash) {
/* 116 */     return new IntHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class IntHashCode extends HashCode implements Serializable {
/*     */     final int hash;
/*     */     
/*     */     IntHashCode(int hash) {
/* 123 */       this.hash = hash;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public int bits() {
/* 128 */       return 32;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 133 */       return new byte[] { (byte)this.hash, (byte)(this.hash >> 8), (byte)(this.hash >> 16), (byte)(this.hash >> 24) };
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 138 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 143 */       throw new IllegalStateException("this HashCode only has 32 bits; cannot create a long");
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 148 */       return UnsignedInts.toLong(this.hash);
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 153 */       for (int i = 0; i < maxLength; i++) {
/* 154 */         dest[offset + i] = (byte)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 160 */       return (this.hash == that.asInt());
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
/*     */   public static HashCode fromLong(long hash) {
/* 173 */     return new LongHashCode(hash);
/*     */   }
/*     */   
/*     */   private static final class LongHashCode extends HashCode implements Serializable { final long hash;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongHashCode(long hash) {
/* 180 */       this.hash = hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 185 */       return 64;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 190 */       return new byte[] { (byte)(int)this.hash, (byte)(int)(this.hash >> 8L), (byte)(int)(this.hash >> 16L), (byte)(int)(this.hash >> 24L), (byte)(int)(this.hash >> 32L), (byte)(int)(this.hash >> 40L), (byte)(int)(this.hash >> 48L), (byte)(int)(this.hash >> 56L) };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 204 */       return (int)this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 209 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 214 */       return this.hash;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 219 */       for (int i = 0; i < maxLength; i++) {
/* 220 */         dest[offset + i] = (byte)(int)(this.hash >> i * 8);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/* 226 */       return (this.hash == that.asLong());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromBytes(byte[] bytes) {
/* 239 */     Preconditions.checkArgument((bytes.length >= 1), "A HashCode must contain at least 1 byte.");
/* 240 */     return fromBytesNoCopy((byte[])bytes.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HashCode fromBytesNoCopy(byte[] bytes) {
/* 248 */     return new BytesHashCode(bytes);
/*     */   }
/*     */   
/*     */   private static final class BytesHashCode extends HashCode implements Serializable { final byte[] bytes;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BytesHashCode(byte[] bytes) {
/* 255 */       this.bytes = (byte[])Preconditions.checkNotNull(bytes);
/*     */     }
/*     */ 
/*     */     
/*     */     public int bits() {
/* 260 */       return this.bytes.length * 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] asBytes() {
/* 265 */       return (byte[])this.bytes.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public int asInt() {
/* 270 */       Preconditions.checkState((this.bytes.length >= 4), "HashCode#asInt() requires >= 4 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 274 */       return this.bytes[0] & 0xFF | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long asLong() {
/* 282 */       Preconditions.checkState((this.bytes.length >= 8), "HashCode#asLong() requires >= 8 bytes (it only has %s bytes).", this.bytes.length);
/*     */ 
/*     */ 
/*     */       
/* 286 */       return padToLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public long padToLong() {
/* 291 */       long retVal = (this.bytes[0] & 0xFF);
/* 292 */       for (int i = 1; i < Math.min(this.bytes.length, 8); i++) {
/* 293 */         retVal |= (this.bytes[i] & 0xFFL) << i * 8;
/*     */       }
/* 295 */       return retVal;
/*     */     }
/*     */ 
/*     */     
/*     */     void writeBytesToImpl(byte[] dest, int offset, int maxLength) {
/* 300 */       System.arraycopy(this.bytes, 0, dest, offset, maxLength);
/*     */     }
/*     */ 
/*     */     
/*     */     byte[] getBytesInternal() {
/* 305 */       return this.bytes;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsSameBits(HashCode that) {
/*     */       int j;
/* 312 */       if (this.bytes.length != (that.getBytesInternal()).length) {
/* 313 */         return false;
/*     */       }
/*     */       
/* 316 */       boolean areEqual = true;
/* 317 */       for (int i = 0; i < this.bytes.length; i++) {
/* 318 */         j = areEqual & ((this.bytes[i] == that.getBytesInternal()[i]) ? 1 : 0);
/*     */       }
/* 320 */       return j;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashCode fromString(String string) {
/* 337 */     Preconditions.checkArgument(
/* 338 */         (string.length() >= 2), "input string (%s) must have at least 2 characters", string);
/* 339 */     Preconditions.checkArgument(
/* 340 */         (string.length() % 2 == 0), "input string (%s) must have an even number of characters", string);
/*     */ 
/*     */ 
/*     */     
/* 344 */     byte[] bytes = new byte[string.length() / 2];
/* 345 */     for (int i = 0; i < string.length(); i += 2) {
/* 346 */       int ch1 = decode(string.charAt(i)) << 4;
/* 347 */       int ch2 = decode(string.charAt(i + 1));
/* 348 */       bytes[i / 2] = (byte)(ch1 + ch2);
/*     */     } 
/* 350 */     return fromBytesNoCopy(bytes);
/*     */   }
/*     */   
/*     */   private static int decode(char ch) {
/* 354 */     if (ch >= '0' && ch <= '9') {
/* 355 */       return ch - 48;
/*     */     }
/* 357 */     if (ch >= 'a' && ch <= 'f') {
/* 358 */       return ch - 97 + 10;
/*     */     }
/* 360 */     throw new IllegalArgumentException("Illegal hexadecimal character: " + ch);
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
/*     */   public final boolean equals(@CheckForNull Object object) {
/* 372 */     if (object instanceof HashCode) {
/* 373 */       HashCode that = (HashCode)object;
/* 374 */       return (bits() == that.bits() && equalsSameBits(that));
/*     */     } 
/* 376 */     return false;
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
/*     */   public final int hashCode() {
/* 388 */     if (bits() >= 32) {
/* 389 */       return asInt();
/*     */     }
/*     */     
/* 392 */     byte[] bytes = getBytesInternal();
/* 393 */     int val = bytes[0] & 0xFF;
/* 394 */     for (int i = 1; i < bytes.length; i++) {
/* 395 */       val |= (bytes[i] & 0xFF) << i * 8;
/*     */     }
/* 397 */     return val;
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
/*     */   public final String toString() {
/* 413 */     byte[] bytes = getBytesInternal();
/* 414 */     StringBuilder sb = new StringBuilder(2 * bytes.length);
/* 415 */     for (byte b : bytes) {
/* 416 */       sb.append(hexDigits[b >> 4 & 0xF]).append(hexDigits[b & 0xF]);
/*     */     }
/* 418 */     return sb.toString();
/*     */   }
/*     */   
/* 421 */   private static final char[] hexDigits = "0123456789abcdef".toCharArray();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/HashCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */