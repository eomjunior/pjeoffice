/*    */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;
/*    */ 
/*    */ import org.bouncycastle.crypto.Digest;
/*    */ 
/*    */ 
/*    */ 
/*    */ class SeedDerive
/*    */ {
/*    */   private final byte[] I;
/*    */   private final byte[] masterSeed;
/*    */   private final Digest digest;
/*    */   private int q;
/*    */   private int j;
/*    */   
/*    */   public SeedDerive(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, Digest paramDigest) {
/* 16 */     this.I = paramArrayOfbyte1;
/* 17 */     this.masterSeed = paramArrayOfbyte2;
/* 18 */     this.digest = paramDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getQ() {
/* 23 */     return this.q;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setQ(int paramInt) {
/* 28 */     this.q = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getJ() {
/* 33 */     return this.j;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setJ(int paramInt) {
/* 38 */     this.j = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getI() {
/* 43 */     return this.I;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getMasterSeed() {
/* 48 */     return this.masterSeed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] deriveSeed(byte[] paramArrayOfbyte, int paramInt) {
/* 54 */     if (paramArrayOfbyte.length < this.digest.getDigestSize())
/*    */     {
/* 56 */       throw new IllegalArgumentException("target length is less than digest size.");
/*    */     }
/*    */     
/* 59 */     this.digest.update(this.I, 0, this.I.length);
/* 60 */     this.digest.update((byte)(this.q >>> 24));
/* 61 */     this.digest.update((byte)(this.q >>> 16));
/* 62 */     this.digest.update((byte)(this.q >>> 8));
/* 63 */     this.digest.update((byte)this.q);
/*    */     
/* 65 */     this.digest.update((byte)(this.j >>> 8));
/* 66 */     this.digest.update((byte)this.j);
/* 67 */     this.digest.update((byte)-1);
/* 68 */     this.digest.update(this.masterSeed, 0, this.masterSeed.length);
/*    */     
/* 70 */     this.digest.doFinal(paramArrayOfbyte, paramInt);
/*    */     
/* 72 */     return paramArrayOfbyte;
/*    */   }
/*    */ 
/*    */   
/*    */   public void deriveSeed(byte[] paramArrayOfbyte, boolean paramBoolean) {
/* 77 */     deriveSeed(paramArrayOfbyte, paramBoolean, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void deriveSeed(byte[] paramArrayOfbyte, boolean paramBoolean, int paramInt) {
/* 84 */     deriveSeed(paramArrayOfbyte, paramInt);
/*    */     
/* 86 */     if (paramBoolean)
/*    */     {
/* 88 */       this.j++;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/SeedDerive.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */