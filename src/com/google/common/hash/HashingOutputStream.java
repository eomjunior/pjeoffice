/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @Beta
/*    */ public final class HashingOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private final Hasher hasher;
/*    */   
/*    */   public HashingOutputStream(HashFunction hashFunction, OutputStream out) {
/* 46 */     super((OutputStream)Preconditions.checkNotNull(out));
/* 47 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 52 */     this.hasher.putByte((byte)b);
/* 53 */     this.out.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] bytes, int off, int len) throws IOException {
/* 58 */     this.hasher.putBytes(bytes, off, len);
/* 59 */     this.out.write(bytes, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HashCode hash() {
/* 67 */     return this.hasher.hash();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 75 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/HashingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */