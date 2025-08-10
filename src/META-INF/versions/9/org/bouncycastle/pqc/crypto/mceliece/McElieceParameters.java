/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import org.bouncycastle.crypto.CipherParameters;
/*     */ import org.bouncycastle.crypto.Digest;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McElieceParameters
/*     */   implements CipherParameters
/*     */ {
/*     */   public static final int DEFAULT_M = 11;
/*     */   public static final int DEFAULT_T = 50;
/*     */   private int m;
/*     */   private int t;
/*     */   private int n;
/*     */   private int fieldPoly;
/*     */   private Digest digest;
/*     */   
/*     */   public McElieceParameters() {
/*  48 */     this(11, 50);
/*     */   }
/*     */ 
/*     */   
/*     */   public McElieceParameters(Digest paramDigest) {
/*  53 */     this(11, 50, paramDigest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceParameters(int paramInt) {
/*  64 */     this(paramInt, null);
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
/*     */   public McElieceParameters(int paramInt, Digest paramDigest) {
/*  76 */     if (paramInt < 1)
/*     */     {
/*  78 */       throw new IllegalArgumentException("key size must be positive");
/*     */     }
/*  80 */     this.m = 0;
/*  81 */     this.n = 1;
/*  82 */     while (this.n < paramInt) {
/*     */       
/*  84 */       this.n <<= 1;
/*  85 */       this.m++;
/*     */     } 
/*  87 */     this.t = this.n >>> 1;
/*  88 */     this.t /= this.m;
/*  89 */     this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(this.m);
/*  90 */     this.digest = paramDigest;
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
/*     */   
/*     */   public McElieceParameters(int paramInt1, int paramInt2) {
/* 103 */     this(paramInt1, paramInt2, null);
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
/*     */   
/*     */   public McElieceParameters(int paramInt1, int paramInt2, Digest paramDigest) {
/* 116 */     if (paramInt1 < 1)
/*     */     {
/* 118 */       throw new IllegalArgumentException("m must be positive");
/*     */     }
/* 120 */     if (paramInt1 > 32)
/*     */     {
/* 122 */       throw new IllegalArgumentException("m is too large");
/*     */     }
/* 124 */     this.m = paramInt1;
/* 125 */     this.n = 1 << paramInt1;
/* 126 */     if (paramInt2 < 0)
/*     */     {
/* 128 */       throw new IllegalArgumentException("t must be positive");
/*     */     }
/* 130 */     if (paramInt2 > this.n)
/*     */     {
/* 132 */       throw new IllegalArgumentException("t must be less than n = 2^m");
/*     */     }
/* 134 */     this.t = paramInt2;
/* 135 */     this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(paramInt1);
/* 136 */     this.digest = paramDigest;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceParameters(int paramInt1, int paramInt2, int paramInt3) {
/* 151 */     this(paramInt1, paramInt2, paramInt3, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McElieceParameters(int paramInt1, int paramInt2, int paramInt3, Digest paramDigest) {
/* 167 */     this.m = paramInt1;
/* 168 */     if (paramInt1 < 1)
/*     */     {
/* 170 */       throw new IllegalArgumentException("m must be positive");
/*     */     }
/* 172 */     if (paramInt1 > 32)
/*     */     {
/* 174 */       throw new IllegalArgumentException(" m is too large");
/*     */     }
/* 176 */     this.n = 1 << paramInt1;
/* 177 */     this.t = paramInt2;
/* 178 */     if (paramInt2 < 0)
/*     */     {
/* 180 */       throw new IllegalArgumentException("t must be positive");
/*     */     }
/* 182 */     if (paramInt2 > this.n)
/*     */     {
/* 184 */       throw new IllegalArgumentException("t must be less than n = 2^m");
/*     */     }
/* 186 */     if (PolynomialRingGF2.degree(paramInt3) == paramInt1 && 
/* 187 */       PolynomialRingGF2.isIrreducible(paramInt3)) {
/*     */       
/* 189 */       this.fieldPoly = paramInt3;
/*     */     }
/*     */     else {
/*     */       
/* 193 */       throw new IllegalArgumentException("polynomial is not a field polynomial for GF(2^m)");
/*     */     } 
/*     */     
/* 196 */     this.digest = paramDigest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getM() {
/* 204 */     return this.m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 212 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/* 220 */     return this.t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldPoly() {
/* 228 */     return this.fieldPoly;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */