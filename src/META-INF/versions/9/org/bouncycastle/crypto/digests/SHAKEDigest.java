/*     */ package META-INF.versions.9.org.bouncycastle.crypto.digests;
/*     */ 
/*     */ import org.bouncycastle.crypto.Xof;
/*     */ import org.bouncycastle.crypto.digests.KeccakDigest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SHAKEDigest
/*     */   extends KeccakDigest
/*     */   implements Xof
/*     */ {
/*     */   private static int checkBitLength(int paramInt) {
/*  17 */     switch (paramInt) {
/*     */       
/*     */       case 128:
/*     */       case 256:
/*  21 */         return paramInt;
/*     */     } 
/*  23 */     throw new IllegalArgumentException("'bitLength' " + paramInt + " not supported for SHAKE");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SHAKEDigest() {
/*  29 */     this(128);
/*     */   }
/*     */ 
/*     */   
/*     */   public SHAKEDigest(int paramInt) {
/*  34 */     super(checkBitLength(paramInt));
/*     */   }
/*     */   
/*     */   public SHAKEDigest(org.bouncycastle.crypto.digests.SHAKEDigest paramSHAKEDigest) {
/*  38 */     super(paramSHAKEDigest);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithmName() {
/*  43 */     return "SHAKE" + this.fixedOutputLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt) {
/*  48 */     return doFinal(paramArrayOfbyte, paramInt, getDigestSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public int doFinal(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  53 */     int i = doOutput(paramArrayOfbyte, paramInt1, paramInt2);
/*     */     
/*  55 */     reset();
/*     */     
/*  57 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public int doOutput(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  62 */     if (!this.squeezing)
/*     */     {
/*  64 */       absorbBits(15, 4);
/*     */     }
/*     */     
/*  67 */     squeeze(paramArrayOfbyte, paramInt1, paramInt2 * 8L);
/*     */     
/*  69 */     return paramInt2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int doFinal(byte[] paramArrayOfbyte, int paramInt1, byte paramByte, int paramInt2) {
/*  77 */     return doFinal(paramArrayOfbyte, paramInt1, getDigestSize(), paramByte, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int doFinal(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, byte paramByte, int paramInt3) {
/*  85 */     if (paramInt3 < 0 || paramInt3 > 7)
/*     */     {
/*  87 */       throw new IllegalArgumentException("'partialBits' must be in the range [0,7]");
/*     */     }
/*     */     
/*  90 */     int i = paramByte & (1 << paramInt3) - 1 | 15 << paramInt3;
/*  91 */     int j = paramInt3 + 4;
/*     */     
/*  93 */     if (j >= 8) {
/*     */       
/*  95 */       absorb((byte)i);
/*  96 */       j -= 8;
/*  97 */       i >>>= 8;
/*     */     } 
/*     */     
/* 100 */     if (j > 0)
/*     */     {
/* 102 */       absorbBits(i, j);
/*     */     }
/*     */     
/* 105 */     squeeze(paramArrayOfbyte, paramInt1, paramInt2 * 8L);
/*     */     
/* 107 */     reset();
/*     */     
/* 109 */     return paramInt2;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/digests/SHAKEDigest.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */