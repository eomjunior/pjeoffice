/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class MessageDigestHashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final MessageDigest prototype;
/*     */   private final int bytes;
/*     */   private final boolean supportsClone;
/*     */   private final String toString;
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, String toString) {
/*  48 */     this.prototype = getMessageDigest(algorithmName);
/*  49 */     this.bytes = this.prototype.getDigestLength();
/*  50 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  51 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
/*  55 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  56 */     this.prototype = getMessageDigest(algorithmName);
/*  57 */     int maxLength = this.prototype.getDigestLength();
/*  58 */     Preconditions.checkArgument((bytes >= 4 && bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
/*     */     
/*  60 */     this.bytes = bytes;
/*  61 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(MessageDigest digest) {
/*     */     try {
/*  66 */       Object unused = digest.clone();
/*  67 */       return true;
/*  68 */     } catch (CloneNotSupportedException e) {
/*  69 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  75 */     return this.bytes * 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static MessageDigest getMessageDigest(String algorithmName) {
/*     */     try {
/*  85 */       return MessageDigest.getInstance(algorithmName);
/*  86 */     } catch (NoSuchAlgorithmException e) {
/*  87 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  93 */     if (this.supportsClone) {
/*     */       try {
/*  95 */         return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
/*  96 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/* 100 */     return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
/*     */   }
/*     */   
/*     */   private static final class SerializedForm
/*     */     implements Serializable {
/*     */     private final String algorithmName;
/*     */     private final int bytes;
/*     */     
/*     */     private SerializedForm(String algorithmName, int bytes, String toString) {
/* 109 */       this.algorithmName = algorithmName;
/* 110 */       this.bytes = bytes;
/* 111 */       this.toString = toString;
/*     */     }
/*     */     private final String toString; private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 115 */       return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 122 */     return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 126 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   private static final class MessageDigestHasher
/*     */     extends AbstractByteHasher {
/*     */     private final MessageDigest digest;
/*     */     private final int bytes;
/*     */     private boolean done;
/*     */     
/*     */     private MessageDigestHasher(MessageDigest digest, int bytes) {
/* 136 */       this.digest = digest;
/* 137 */       this.bytes = bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 142 */       checkNotDone();
/* 143 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 148 */       checkNotDone();
/* 149 */       this.digest.update(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(ByteBuffer bytes) {
/* 154 */       checkNotDone();
/* 155 */       this.digest.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 159 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 164 */       checkNotDone();
/* 165 */       this.done = true;
/* 166 */       return (this.bytes == this.digest.getDigestLength()) ? 
/* 167 */         HashCode.fromBytesNoCopy(this.digest.digest()) : 
/* 168 */         HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/MessageDigestHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */