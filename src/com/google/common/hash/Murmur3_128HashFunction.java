/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class Murmur3_128HashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  46 */   static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
/*     */   
/*  48 */   static final HashFunction GOOD_FAST_HASH_128 = new Murmur3_128HashFunction(Hashing.GOOD_FAST_HASH_SEED);
/*     */   
/*     */   private final int seed;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Murmur3_128HashFunction(int seed) {
/*  55 */     this.seed = seed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  60 */     return 128;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  65 */     return new Murmur3_128Hasher(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     return "Hashing.murmur3_128(" + this.seed + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/*  75 */     if (object instanceof Murmur3_128HashFunction) {
/*  76 */       Murmur3_128HashFunction other = (Murmur3_128HashFunction)object;
/*  77 */       return (this.seed == other.seed);
/*     */     } 
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  84 */     return getClass().hashCode() ^ this.seed;
/*     */   }
/*     */   
/*     */   private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
/*     */     private static final int CHUNK_SIZE = 16;
/*     */     private static final long C1 = -8663945395140668459L;
/*     */     private static final long C2 = 5545529020109919103L;
/*     */     private long h1;
/*     */     private long h2;
/*     */     private int length;
/*     */     
/*     */     Murmur3_128Hasher(int seed) {
/*  96 */       super(16);
/*  97 */       this.h1 = seed;
/*  98 */       this.h2 = seed;
/*  99 */       this.length = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer bb) {
/* 104 */       long k1 = bb.getLong();
/* 105 */       long k2 = bb.getLong();
/* 106 */       bmix64(k1, k2);
/* 107 */       this.length += 16;
/*     */     }
/*     */     
/*     */     private void bmix64(long k1, long k2) {
/* 111 */       this.h1 ^= mixK1(k1);
/*     */       
/* 113 */       this.h1 = Long.rotateLeft(this.h1, 27);
/* 114 */       this.h1 += this.h2;
/* 115 */       this.h1 = this.h1 * 5L + 1390208809L;
/*     */       
/* 117 */       this.h2 ^= mixK2(k2);
/*     */       
/* 119 */       this.h2 = Long.rotateLeft(this.h2, 31);
/* 120 */       this.h2 += this.h1;
/* 121 */       this.h2 = this.h2 * 5L + 944331445L;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer bb) {
/* 126 */       long k1 = 0L;
/* 127 */       long k2 = 0L;
/* 128 */       this.length += bb.remaining();
/* 129 */       switch (bb.remaining()) {
/*     */         case 15:
/* 131 */           k2 ^= UnsignedBytes.toInt(bb.get(14)) << 48L;
/*     */         case 14:
/* 133 */           k2 ^= UnsignedBytes.toInt(bb.get(13)) << 40L;
/*     */         case 13:
/* 135 */           k2 ^= UnsignedBytes.toInt(bb.get(12)) << 32L;
/*     */         case 12:
/* 137 */           k2 ^= UnsignedBytes.toInt(bb.get(11)) << 24L;
/*     */         case 11:
/* 139 */           k2 ^= UnsignedBytes.toInt(bb.get(10)) << 16L;
/*     */         case 10:
/* 141 */           k2 ^= UnsignedBytes.toInt(bb.get(9)) << 8L;
/*     */         case 9:
/* 143 */           k2 ^= UnsignedBytes.toInt(bb.get(8));
/*     */         case 8:
/* 145 */           k1 ^= bb.getLong();
/*     */           break;
/*     */         case 7:
/* 148 */           k1 ^= UnsignedBytes.toInt(bb.get(6)) << 48L;
/*     */         case 6:
/* 150 */           k1 ^= UnsignedBytes.toInt(bb.get(5)) << 40L;
/*     */         case 5:
/* 152 */           k1 ^= UnsignedBytes.toInt(bb.get(4)) << 32L;
/*     */         case 4:
/* 154 */           k1 ^= UnsignedBytes.toInt(bb.get(3)) << 24L;
/*     */         case 3:
/* 156 */           k1 ^= UnsignedBytes.toInt(bb.get(2)) << 16L;
/*     */         case 2:
/* 158 */           k1 ^= UnsignedBytes.toInt(bb.get(1)) << 8L;
/*     */         case 1:
/* 160 */           k1 ^= UnsignedBytes.toInt(bb.get(0));
/*     */           break;
/*     */         default:
/* 163 */           throw new AssertionError("Should never get here.");
/*     */       } 
/* 165 */       this.h1 ^= mixK1(k1);
/* 166 */       this.h2 ^= mixK2(k2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected HashCode makeHash() {
/* 171 */       this.h1 ^= this.length;
/* 172 */       this.h2 ^= this.length;
/*     */       
/* 174 */       this.h1 += this.h2;
/* 175 */       this.h2 += this.h1;
/*     */       
/* 177 */       this.h1 = fmix64(this.h1);
/* 178 */       this.h2 = fmix64(this.h2);
/*     */       
/* 180 */       this.h1 += this.h2;
/* 181 */       this.h2 += this.h1;
/*     */       
/* 183 */       return HashCode.fromBytesNoCopy(
/* 184 */           ByteBuffer.wrap(new byte[16])
/* 185 */           .order(ByteOrder.LITTLE_ENDIAN)
/* 186 */           .putLong(this.h1)
/* 187 */           .putLong(this.h2)
/* 188 */           .array());
/*     */     }
/*     */     
/*     */     private static long fmix64(long k) {
/* 192 */       k ^= k >>> 33L;
/* 193 */       k *= -49064778989728563L;
/* 194 */       k ^= k >>> 33L;
/* 195 */       k *= -4265267296055464877L;
/* 196 */       k ^= k >>> 33L;
/* 197 */       return k;
/*     */     }
/*     */     
/*     */     private static long mixK1(long k1) {
/* 201 */       k1 *= -8663945395140668459L;
/* 202 */       k1 = Long.rotateLeft(k1, 31);
/* 203 */       k1 *= 5545529020109919103L;
/* 204 */       return k1;
/*     */     }
/*     */     
/*     */     private static long mixK2(long k2) {
/* 208 */       k2 *= 5545529020109919103L;
/* 209 */       k2 = Long.rotateLeft(k2, 33);
/* 210 */       k2 *= -8663945395140668459L;
/* 211 */       return k2;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Murmur3_128HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */