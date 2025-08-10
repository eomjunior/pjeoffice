/*    */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*    */ 
/*    */ import org.bouncycastle.crypto.digests.KeccakDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SHA3Digest
/*    */   extends KeccakDigest
/*    */ {
/*    */   private static int checkBitLength(int paramInt) {
/* 14 */     switch (paramInt) {
/*    */       
/*    */       case 224:
/*    */       case 256:
/*    */       case 384:
/*    */       case 512:
/* 20 */         return paramInt;
/*    */     } 
/* 22 */     throw new IllegalArgumentException("'bitLength' " + paramInt + " not supported for SHA-3");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SHA3Digest() {
/* 28 */     this(256);
/*    */   }
/*    */ 
/*    */   
/*    */   public SHA3Digest(int paramInt) {
/* 33 */     super(checkBitLength(paramInt));
/*    */   }
/*    */   
/*    */   public SHA3Digest(org.bouncycastle.crypto.digests.SHA3Digest paramSHA3Digest) {
/* 37 */     super(paramSHA3Digest);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getAlgorithmName() {
/* 42 */     return "SHA3-" + this.fixedOutputLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/* 47 */     absorbBits(2, 2);
/*    */     
/* 49 */     return super.doFinal(paramArrayOfbyte, paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int doFinal(byte[] paramArrayOfbyte, int paramInt1, byte paramByte, int paramInt2) {
/* 57 */     if (paramInt2 < 0 || paramInt2 > 7)
/*    */     {
/* 59 */       throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
/*    */     }
/*    */     
/* 62 */     int i = paramByte & (1 << paramInt2) - 1 | 2 << paramInt2;
/* 63 */     int j = paramInt2 + 2;
/*    */     
/* 65 */     if (j >= 8) {
/*    */       
/* 67 */       absorb((byte)i);
/* 68 */       j -= 8;
/* 69 */       i >>>= 8;
/*    */     } 
/*    */     
/* 72 */     return super.doFinal(paramArrayOfbyte, paramInt1, (byte)i, j);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHA3Digest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */