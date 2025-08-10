/*     */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.params.DHValidationParameters;
/*     */ import org.bouncycastle.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DHParameters
/*     */   implements CipherParameters
/*     */ {
/*     */   private static final int DEFAULT_MINIMUM_LENGTH = 160;
/*     */   private BigInteger g;
/*     */   private BigInteger p;
/*     */   private BigInteger q;
/*     */   private BigInteger j;
/*     */   private int m;
/*     */   private int l;
/*     */   private DHValidationParameters validation;
/*     */   
/*     */   private static int getDefaultMParam(int paramInt) {
/*  25 */     if (paramInt == 0)
/*     */     {
/*  27 */       return 160;
/*     */     }
/*     */     
/*  30 */     return (paramInt < 160) ? paramInt : 160;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  37 */     this(paramBigInteger1, paramBigInteger2, null, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3) {
/*  45 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt) {
/*  54 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, getDefaultMParam(paramInt), paramInt, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt1, int paramInt2) {
/*  64 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramInt1, paramInt2, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, DHValidationParameters paramDHValidationParameters) {
/*  74 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, 160, 0, paramBigInteger4, paramDHValidationParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt1, int paramInt2, BigInteger paramBigInteger4, DHValidationParameters paramDHValidationParameters) {
/*  86 */     if (paramInt2 != 0) {
/*     */       
/*  88 */       if (paramInt2 > paramBigInteger1.bitLength())
/*     */       {
/*  90 */         throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
/*     */       }
/*  92 */       if (paramInt2 < paramInt1)
/*     */       {
/*  94 */         throw new IllegalArgumentException("when l value specified, it may not be less than m value");
/*     */       }
/*     */     } 
/*     */     
/*  98 */     if (paramInt1 > paramBigInteger1.bitLength() && !Properties.isOverrideSet("org.bouncycastle.dh.allow_unsafe_p_value"))
/*     */     {
/* 100 */       throw new IllegalArgumentException("unsafe p value so small specific l required");
/*     */     }
/*     */     
/* 103 */     this.g = paramBigInteger2;
/* 104 */     this.p = paramBigInteger1;
/* 105 */     this.q = paramBigInteger3;
/* 106 */     this.m = paramInt1;
/* 107 */     this.l = paramInt2;
/* 108 */     this.j = paramBigInteger4;
/* 109 */     this.validation = paramDHValidationParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getP() {
/* 114 */     return this.p;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getG() {
/* 119 */     return this.g;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/* 124 */     return this.q;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getJ() {
/* 134 */     return this.j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getM() {
/* 144 */     return this.m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getL() {
/* 154 */     return this.l;
/*     */   }
/*     */ 
/*     */   
/*     */   public DHValidationParameters getValidationParameters() {
/* 159 */     return this.validation;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 165 */     if (!(paramObject instanceof org.bouncycastle.crypto.params.DHParameters))
/*     */     {
/* 167 */       return false;
/*     */     }
/*     */     
/* 170 */     org.bouncycastle.crypto.params.DHParameters dHParameters = (org.bouncycastle.crypto.params.DHParameters)paramObject;
/*     */     
/* 172 */     if (getQ() != null) {
/*     */       
/* 174 */       if (!getQ().equals(dHParameters.getQ()))
/*     */       {
/* 176 */         return false;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 181 */     else if (dHParameters.getQ() != null) {
/*     */       
/* 183 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 187 */     return (dHParameters.getP().equals(this.p) && dHParameters.getG().equals(this.g));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 192 */     return getP().hashCode() ^ getG().hashCode() ^ ((getQ() != null) ? getQ().hashCode() : 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/DHParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */