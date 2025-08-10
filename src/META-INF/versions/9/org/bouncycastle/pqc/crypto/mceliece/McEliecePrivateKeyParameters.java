/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyParameters;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McEliecePrivateKeyParameters
/*     */   extends McElieceKeyParameters
/*     */ {
/*     */   private String oid;
/*     */   private int n;
/*     */   private int k;
/*     */   private GF2mField field;
/*     */   private PolynomialGF2mSmallM goppaPoly;
/*     */   private GF2Matrix sInv;
/*     */   private Permutation p1;
/*     */   private Permutation p2;
/*     */   private GF2Matrix h;
/*     */   private PolynomialGF2mSmallM[] qInv;
/*     */   
/*     */   public McEliecePrivateKeyParameters(int paramInt1, int paramInt2, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, Permutation paramPermutation1, Permutation paramPermutation2, GF2Matrix paramGF2Matrix) {
/*  62 */     super(true, null);
/*  63 */     this.k = paramInt2;
/*  64 */     this.n = paramInt1;
/*  65 */     this.field = paramGF2mField;
/*  66 */     this.goppaPoly = paramPolynomialGF2mSmallM;
/*  67 */     this.sInv = paramGF2Matrix;
/*  68 */     this.p1 = paramPermutation1;
/*  69 */     this.p2 = paramPermutation2;
/*  70 */     this.h = GoppaCode.createCanonicalCheckMatrix(paramGF2mField, paramPolynomialGF2mSmallM);
/*     */     
/*  72 */     PolynomialRingGF2m polynomialRingGF2m = new PolynomialRingGF2m(paramGF2mField, paramPolynomialGF2mSmallM);
/*     */ 
/*     */     
/*  75 */     this.qInv = polynomialRingGF2m.getSquareRootMatrix();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public McEliecePrivateKeyParameters(int paramInt1, int paramInt2, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte[] paramArrayOfbyte5, byte[] paramArrayOfbyte6, byte[][] paramArrayOfbyte) {
/*  99 */     super(true, null);
/* 100 */     this.n = paramInt1;
/* 101 */     this.k = paramInt2;
/* 102 */     this.field = new GF2mField(paramArrayOfbyte1);
/* 103 */     this.goppaPoly = new PolynomialGF2mSmallM(this.field, paramArrayOfbyte2);
/* 104 */     this.sInv = new GF2Matrix(paramArrayOfbyte3);
/* 105 */     this.p1 = new Permutation(paramArrayOfbyte4);
/* 106 */     this.p2 = new Permutation(paramArrayOfbyte5);
/* 107 */     this.h = new GF2Matrix(paramArrayOfbyte6);
/* 108 */     this.qInv = new PolynomialGF2mSmallM[paramArrayOfbyte.length];
/* 109 */     for (byte b = 0; b < paramArrayOfbyte.length; b++)
/*     */     {
/* 111 */       this.qInv[b] = new PolynomialGF2mSmallM(this.field, paramArrayOfbyte[b]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 120 */     return this.n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getK() {
/* 128 */     return this.k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/* 136 */     return this.field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/* 144 */     return this.goppaPoly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getSInv() {
/* 152 */     return this.sInv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP1() {
/* 160 */     return this.p1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permutation getP2() {
/* 168 */     return this.p2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GF2Matrix getH() {
/* 176 */     return this.h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM[] getQInv() {
/* 185 */     return this.qInv;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McEliecePrivateKeyParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */