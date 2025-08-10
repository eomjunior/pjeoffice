/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
/*     */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyGenerationParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Matrix;
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
/*     */ public class McElieceKeyPairGenerator
/*     */   implements AsymmetricCipherKeyPairGenerator
/*     */ {
/*     */   private static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.1";
/*     */   private McElieceKeyGenerationParameters mcElieceParams;
/*     */   private int m;
/*     */   private int n;
/*     */   private int t;
/*     */   private int fieldPoly;
/*     */   private SecureRandom random;
/*     */   private boolean initialized = false;
/*     */   
/*     */   private void initializeDefault() {
/*  63 */     McElieceKeyGenerationParameters mcElieceKeyGenerationParameters = new McElieceKeyGenerationParameters(null, new McElieceParameters());
/*  64 */     initialize((KeyGenerationParameters)mcElieceKeyGenerationParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(KeyGenerationParameters paramKeyGenerationParameters) {
/*  70 */     this.mcElieceParams = (McElieceKeyGenerationParameters)paramKeyGenerationParameters;
/*  71 */     this.random = paramKeyGenerationParameters.getRandom();
/*     */     
/*  73 */     this.m = this.mcElieceParams.getParameters().getM();
/*  74 */     this.n = this.mcElieceParams.getParameters().getN();
/*  75 */     this.t = this.mcElieceParams.getParameters().getT();
/*  76 */     this.fieldPoly = this.mcElieceParams.getParameters().getFieldPoly();
/*  77 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AsymmetricCipherKeyPair genKeyPair() {
/*  84 */     if (!this.initialized)
/*     */     {
/*  86 */       initializeDefault();
/*     */     }
/*     */ 
/*     */     
/*  90 */     GF2mField gF2mField = new GF2mField(this.m, this.fieldPoly);
/*     */ 
/*     */     
/*  93 */     PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(gF2mField, this.t, 'I', this.random);
/*     */     
/*  95 */     PolynomialRingGF2m polynomialRingGF2m = new PolynomialRingGF2m(gF2mField, polynomialGF2mSmallM);
/*     */ 
/*     */     
/*  98 */     PolynomialGF2mSmallM[] arrayOfPolynomialGF2mSmallM = polynomialRingGF2m.getSquareRootMatrix();
/*     */ 
/*     */     
/* 101 */     GF2Matrix gF2Matrix1 = GoppaCode.createCanonicalCheckMatrix(gF2mField, polynomialGF2mSmallM);
/*     */ 
/*     */     
/* 104 */     GoppaCode.MaMaPe maMaPe = GoppaCode.computeSystematicForm(gF2Matrix1, this.random);
/* 105 */     GF2Matrix gF2Matrix2 = maMaPe.getSecondMatrix();
/* 106 */     Permutation permutation1 = maMaPe.getPermutation();
/*     */ 
/*     */     
/* 109 */     GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix2.computeTranspose();
/*     */ 
/*     */     
/* 112 */     GF2Matrix gF2Matrix4 = gF2Matrix3.extendLeftCompactForm();
/*     */ 
/*     */     
/* 115 */     int i = gF2Matrix3.getNumRows();
/*     */ 
/*     */ 
/*     */     
/* 119 */     GF2Matrix[] arrayOfGF2Matrix = GF2Matrix.createRandomRegularMatrixAndItsInverse(i, this.random);
/*     */ 
/*     */     
/* 122 */     Permutation permutation2 = new Permutation(this.n, this.random);
/*     */ 
/*     */     
/* 125 */     GF2Matrix gF2Matrix5 = (GF2Matrix)arrayOfGF2Matrix[0].rightMultiply((Matrix)gF2Matrix4);
/* 126 */     gF2Matrix5 = (GF2Matrix)gF2Matrix5.rightMultiply(permutation2);
/*     */ 
/*     */ 
/*     */     
/* 130 */     McEliecePublicKeyParameters mcEliecePublicKeyParameters = new McEliecePublicKeyParameters(this.n, this.t, gF2Matrix5);
/* 131 */     McEliecePrivateKeyParameters mcEliecePrivateKeyParameters = new McEliecePrivateKeyParameters(this.n, i, gF2mField, polynomialGF2mSmallM, permutation1, permutation2, arrayOfGF2Matrix[1]);
/*     */ 
/*     */     
/* 134 */     return new AsymmetricCipherKeyPair((AsymmetricKeyParameter)mcEliecePublicKeyParameters, (AsymmetricKeyParameter)mcEliecePrivateKeyParameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(KeyGenerationParameters paramKeyGenerationParameters) {
/* 139 */     initialize(paramKeyGenerationParameters);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsymmetricCipherKeyPair generateKeyPair() {
/* 144 */     return genKeyPair();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceKeyPairGenerator.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */