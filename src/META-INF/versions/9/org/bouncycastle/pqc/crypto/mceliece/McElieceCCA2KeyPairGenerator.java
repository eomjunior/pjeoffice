/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.mceliece;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
/*     */ import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
/*     */ import org.bouncycastle.crypto.KeyGenerationParameters;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2KeyGenerationParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GoppaCode;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
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
/*     */ public class McElieceCCA2KeyPairGenerator
/*     */   implements AsymmetricCipherKeyPairGenerator
/*     */ {
/*     */   public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";
/*     */   private McElieceCCA2KeyGenerationParameters mcElieceCCA2Params;
/*     */   private int m;
/*     */   private int n;
/*     */   private int t;
/*     */   private int fieldPoly;
/*     */   private SecureRandom random;
/*     */   private boolean initialized = false;
/*     */   
/*     */   private void initializeDefault() {
/*  56 */     McElieceCCA2KeyGenerationParameters mcElieceCCA2KeyGenerationParameters = new McElieceCCA2KeyGenerationParameters(null, new McElieceCCA2Parameters());
/*  57 */     init((KeyGenerationParameters)mcElieceCCA2KeyGenerationParameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init(KeyGenerationParameters paramKeyGenerationParameters) {
/*  64 */     this.mcElieceCCA2Params = (McElieceCCA2KeyGenerationParameters)paramKeyGenerationParameters;
/*     */ 
/*     */     
/*  67 */     this.random = paramKeyGenerationParameters.getRandom();
/*     */     
/*  69 */     this.m = this.mcElieceCCA2Params.getParameters().getM();
/*  70 */     this.n = this.mcElieceCCA2Params.getParameters().getN();
/*  71 */     this.t = this.mcElieceCCA2Params.getParameters().getT();
/*  72 */     this.fieldPoly = this.mcElieceCCA2Params.getParameters().getFieldPoly();
/*  73 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsymmetricCipherKeyPair generateKeyPair() {
/*  80 */     if (!this.initialized)
/*     */     {
/*  82 */       initializeDefault();
/*     */     }
/*     */ 
/*     */     
/*  86 */     GF2mField gF2mField = new GF2mField(this.m, this.fieldPoly);
/*     */ 
/*     */     
/*  89 */     PolynomialGF2mSmallM polynomialGF2mSmallM = new PolynomialGF2mSmallM(gF2mField, this.t, 'I', this.random);
/*     */ 
/*     */ 
/*     */     
/*  93 */     GF2Matrix gF2Matrix1 = GoppaCode.createCanonicalCheckMatrix(gF2mField, polynomialGF2mSmallM);
/*     */ 
/*     */     
/*  96 */     GoppaCode.MaMaPe maMaPe = GoppaCode.computeSystematicForm(gF2Matrix1, this.random);
/*  97 */     GF2Matrix gF2Matrix2 = maMaPe.getSecondMatrix();
/*  98 */     Permutation permutation = maMaPe.getPermutation();
/*     */ 
/*     */     
/* 101 */     GF2Matrix gF2Matrix3 = (GF2Matrix)gF2Matrix2.computeTranspose();
/*     */ 
/*     */     
/* 104 */     int i = gF2Matrix3.getNumRows();
/*     */ 
/*     */     
/* 107 */     McElieceCCA2PublicKeyParameters mcElieceCCA2PublicKeyParameters = new McElieceCCA2PublicKeyParameters(this.n, this.t, gF2Matrix3, this.mcElieceCCA2Params.getParameters().getDigest());
/* 108 */     McElieceCCA2PrivateKeyParameters mcElieceCCA2PrivateKeyParameters = new McElieceCCA2PrivateKeyParameters(this.n, i, gF2mField, polynomialGF2mSmallM, permutation, this.mcElieceCCA2Params.getParameters().getDigest());
/*     */ 
/*     */     
/* 111 */     return new AsymmetricCipherKeyPair((AsymmetricKeyParameter)mcElieceCCA2PublicKeyParameters, (AsymmetricKeyParameter)mcElieceCCA2PrivateKeyParameters);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2KeyPairGenerator.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */