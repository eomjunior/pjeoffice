/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Beta
/*     */ public final class HashingInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Hasher hasher;
/*     */   
/*     */   public HashingInputStream(HashFunction hashFunction, InputStream in) {
/*  43 */     super((InputStream)Preconditions.checkNotNull(in));
/*  44 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int read() throws IOException {
/*  54 */     int b = this.in.read();
/*  55 */     if (b != -1) {
/*  56 */       this.hasher.putByte((byte)b);
/*     */     }
/*  58 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int read(byte[] bytes, int off, int len) throws IOException {
/*  68 */     int numOfBytesRead = this.in.read(bytes, off, len);
/*  69 */     if (numOfBytesRead != -1) {
/*  70 */       this.hasher.putBytes(bytes, off, numOfBytesRead);
/*     */     }
/*  72 */     return numOfBytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/*  96 */     throw new IOException("reset not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash() {
/* 104 */     return this.hasher.hash();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/HashingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */