/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.Immutable;
/*    */ import java.io.Serializable;
/*    */ import java.util.zip.Checksum;
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
/*    */ @Immutable
/*    */ @ElementTypesAreNonnullByDefault
/*    */ final class ChecksumHashFunction
/*    */   extends AbstractHashFunction
/*    */   implements Serializable
/*    */ {
/*    */   private final ImmutableSupplier<? extends Checksum> checksumSupplier;
/*    */   private final int bits;
/*    */   private final String toString;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ChecksumHashFunction(ImmutableSupplier<? extends Checksum> checksumSupplier, int bits, String toString) {
/* 38 */     this.checksumSupplier = (ImmutableSupplier<? extends Checksum>)Preconditions.checkNotNull(checksumSupplier);
/* 39 */     Preconditions.checkArgument((bits == 32 || bits == 64), "bits (%s) must be either 32 or 64", bits);
/* 40 */     this.bits = bits;
/* 41 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*    */   }
/*    */ 
/*    */   
/*    */   public int bits() {
/* 46 */     return this.bits;
/*    */   }
/*    */ 
/*    */   
/*    */   public Hasher newHasher() {
/* 51 */     return new ChecksumHasher((Checksum)this.checksumSupplier.get());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return this.toString;
/*    */   }
/*    */   
/*    */   private final class ChecksumHasher
/*    */     extends AbstractByteHasher {
/*    */     private final Checksum checksum;
/*    */     
/*    */     private ChecksumHasher(Checksum checksum) {
/* 64 */       this.checksum = (Checksum)Preconditions.checkNotNull(checksum);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte b) {
/* 69 */       this.checksum.update(b);
/*    */     }
/*    */ 
/*    */     
/*    */     protected void update(byte[] bytes, int off, int len) {
/* 74 */       this.checksum.update(bytes, off, len);
/*    */     }
/*    */ 
/*    */     
/*    */     public HashCode hash() {
/* 79 */       long value = this.checksum.getValue();
/* 80 */       if (ChecksumHashFunction.this.bits == 32)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 86 */         return HashCode.fromInt((int)value);
/*    */       }
/* 88 */       return HashCode.fromLong(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/ChecksumHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */