/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
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
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class SipHashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  39 */   static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
/*     */ 
/*     */   
/*     */   private final int c;
/*     */ 
/*     */   
/*     */   private final int d;
/*     */ 
/*     */   
/*     */   private final long k0;
/*     */ 
/*     */   
/*     */   private final long k1;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   SipHashFunction(int c, int d, long k0, long k1) {
/*  57 */     Preconditions.checkArgument((c > 0), "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
/*     */     
/*  59 */     Preconditions.checkArgument((d > 0), "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
/*     */     
/*  61 */     this.c = c;
/*  62 */     this.d = d;
/*  63 */     this.k0 = k0;
/*  64 */     this.k1 = k1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  69 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  74 */     return new SipHasher(this.c, this.d, this.k0, this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  81 */     return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/*  86 */     if (object instanceof SipHashFunction) {
/*  87 */       SipHashFunction other = (SipHashFunction)object;
/*  88 */       return (this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1);
/*     */     } 
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return (int)((getClass().hashCode() ^ this.c ^ this.d) ^ this.k0 ^ this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SipHasher
/*     */     extends AbstractStreamingHasher
/*     */   {
/*     */     private static final int CHUNK_SIZE = 8;
/*     */ 
/*     */     
/*     */     private final int c;
/*     */     
/*     */     private final int d;
/*     */     
/* 110 */     private long v0 = 8317987319222330741L;
/* 111 */     private long v1 = 7237128888997146477L;
/* 112 */     private long v2 = 7816392313619706465L;
/* 113 */     private long v3 = 8387220255154660723L;
/*     */ 
/*     */     
/* 116 */     private long b = 0L;
/*     */ 
/*     */ 
/*     */     
/* 120 */     private long finalM = 0L;
/*     */     
/*     */     SipHasher(int c, int d, long k0, long k1) {
/* 123 */       super(8);
/* 124 */       this.c = c;
/* 125 */       this.d = d;
/* 126 */       this.v0 ^= k0;
/* 127 */       this.v1 ^= k1;
/* 128 */       this.v2 ^= k0;
/* 129 */       this.v3 ^= k1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer buffer) {
/* 134 */       this.b += 8L;
/* 135 */       processM(buffer.getLong());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer buffer) {
/* 140 */       this.b += buffer.remaining();
/* 141 */       for (int i = 0; buffer.hasRemaining(); i += 8) {
/* 142 */         this.finalM ^= (buffer.get() & 0xFFL) << i;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected HashCode makeHash() {
/* 149 */       this.finalM ^= this.b << 56L;
/* 150 */       processM(this.finalM);
/*     */ 
/*     */       
/* 153 */       this.v2 ^= 0xFFL;
/* 154 */       sipRound(this.d);
/* 155 */       return HashCode.fromLong(this.v0 ^ this.v1 ^ this.v2 ^ this.v3);
/*     */     }
/*     */     
/*     */     private void processM(long m) {
/* 159 */       this.v3 ^= m;
/* 160 */       sipRound(this.c);
/* 161 */       this.v0 ^= m;
/*     */     }
/*     */     
/*     */     private void sipRound(int iterations) {
/* 165 */       for (int i = 0; i < iterations; i++) {
/* 166 */         this.v0 += this.v1;
/* 167 */         this.v2 += this.v3;
/* 168 */         this.v1 = Long.rotateLeft(this.v1, 13);
/* 169 */         this.v3 = Long.rotateLeft(this.v3, 16);
/* 170 */         this.v1 ^= this.v0;
/* 171 */         this.v3 ^= this.v2;
/* 172 */         this.v0 = Long.rotateLeft(this.v0, 32);
/* 173 */         this.v2 += this.v1;
/* 174 */         this.v0 += this.v3;
/* 175 */         this.v1 = Long.rotateLeft(this.v1, 17);
/* 176 */         this.v3 = Long.rotateLeft(this.v3, 21);
/* 177 */         this.v1 ^= this.v2;
/* 178 */         this.v3 ^= this.v0;
/* 179 */         this.v2 = Long.rotateLeft(this.v2, 32);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/SipHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */