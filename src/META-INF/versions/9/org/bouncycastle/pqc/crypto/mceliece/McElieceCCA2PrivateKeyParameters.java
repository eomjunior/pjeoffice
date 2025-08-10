/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyParameters;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialRingGF2m;
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
/*     */ 
/*     */ public class McElieceCCA2PrivateKeyParameters
/*     */   extends McElieceCCA2KeyParameters
/*     */ {
/*     */   private int n;
/*     */   private int k;
/*     */   private GF2mField field;
/*     */   private PolynomialGF2mSmallM goppaPoly;
/*     */   private Permutation p;
/*     */   private GF2Matrix h;
/*     */   private PolynomialGF2mSmallM[] qInv;
/*     */   
/*     */   public McElieceCCA2PrivateKeyParameters(int paramInt1, int paramInt2, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, Permutation paramPermutation, String paramString) {
/*  53 */     this(paramInt1, paramInt2, paramGF2mField, paramPolynomialGF2mSmallM, GoppaCode.createCanonicalCheckMatrix(paramGF2mField, paramPolynomialGF2mSmallM), paramPermutation, paramString);
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
/*     */   
/*     */   public McElieceCCA2PrivateKeyParameters(int paramInt1, int paramInt2, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, GF2Matrix paramGF2Matrix, Permutation paramPermutation, String paramString) {
/*  70 */     super(true, paramString);
/*     */     
/*  72 */     this.n = paramInt1;
/*  73 */     this.k = paramInt2;
/*  74 */     this.field = paramGF2mField;
/*  75 */     this.goppaPoly = paramPolynomialGF2mSmallM;
/*  76 */     this.h = paramGF2Matrix;
/*  77 */     this.p = paramPermutation;
/*     */     
/*  79 */     PolynomialRingGF2m polynomialRingGF2m = new PolynomialRingGF2m(paramGF2mField, paramPolynomialGF2mSmallM);
/*     */ 
/*     */     
/*  82 */     this.qInv = polynomialRingGF2m.getSquareRootMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/*  90 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/*  98 */     return this.k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getT() {
/* 106 */     return this.goppaPoly.getDegree();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/* 114 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/* 122 */     return this.goppaPoly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP() {
/* 130 */     return this.p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getH() {
/* 138 */     return this.h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getQInv() {
/* 146 */     return this.qInv;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2PrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */