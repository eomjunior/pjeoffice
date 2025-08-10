/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Mac;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class MacHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   private final Mac prototype;
/*     */   private final Key key;
/*     */   private final String toString;
/*     */   private final int bits;
/*     */   private final boolean supportsClone;
/*     */   
/*     */   MacHashFunction(String algorithmName, Key key, String toString) {
/*  47 */     this.prototype = getMac(algorithmName, key);
/*  48 */     this.key = (Key)Preconditions.checkNotNull(key);
/*  49 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  50 */     this.bits = this.prototype.getMacLength() * 8;
/*  51 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  56 */     return this.bits;
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(Mac mac) {
/*     */     try {
/*  61 */       Object unused = mac.clone();
/*  62 */       return true;
/*  63 */     } catch (CloneNotSupportedException e) {
/*  64 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Mac getMac(String algorithmName, Key key) {
/*     */     try {
/*  70 */       Mac mac = Mac.getInstance(algorithmName);
/*  71 */       mac.init(key);
/*  72 */       return mac;
/*  73 */     } catch (NoSuchAlgorithmException e) {
/*  74 */       throw new IllegalStateException(e);
/*  75 */     } catch (InvalidKeyException e) {
/*  76 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  82 */     if (this.supportsClone) {
/*     */       try {
/*  84 */         return new MacHasher((Mac)this.prototype.clone());
/*  85 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/*  89 */     return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  94 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static final class MacHasher
/*     */     extends AbstractByteHasher {
/*     */     private final Mac mac;
/*     */     private boolean done;
/*     */     
/*     */     private MacHasher(Mac mac) {
/* 103 */       this.mac = mac;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 108 */       checkNotDone();
/* 109 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b) {
/* 114 */       checkNotDone();
/* 115 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 120 */       checkNotDone();
/* 121 */       this.mac.update(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(ByteBuffer bytes) {
/* 126 */       checkNotDone();
/* 127 */       Preconditions.checkNotNull(bytes);
/* 128 */       this.mac.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 132 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 137 */       checkNotDone();
/* 138 */       this.done = true;
/* 139 */       return HashCode.fromBytesNoCopy(this.mac.doFinal());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/MacHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */