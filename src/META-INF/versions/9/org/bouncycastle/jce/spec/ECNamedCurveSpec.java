/*     */ package META-INF.versions.9.org.bouncycastle.jce.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.spec.ECField;
/*     */ import java.security.spec.ECFieldF2m;
/*     */ import java.security.spec.ECFieldFp;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.security.spec.ECPoint;
/*     */ import java.security.spec.EllipticCurve;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.field.FiniteField;
/*     */ import org.bouncycastle.math.field.Polynomial;
/*     */ import org.bouncycastle.math.field.PolynomialExtensionField;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ECNamedCurveSpec
/*     */   extends ECParameterSpec
/*     */ {
/*     */   private String name;
/*     */   
/*     */   private static EllipticCurve convertCurve(ECCurve paramECCurve, byte[] paramArrayOfbyte) {
/*  31 */     ECField eCField = convertField(paramECCurve.getField());
/*  32 */     BigInteger bigInteger1 = paramECCurve.getA().toBigInteger(), bigInteger2 = paramECCurve.getB().toBigInteger();
/*  33 */     return new EllipticCurve(eCField, bigInteger1, bigInteger2, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECField convertField(FiniteField paramFiniteField) {
/*  38 */     if (ECAlgorithms.isFpField(paramFiniteField))
/*     */     {
/*  40 */       return new ECFieldFp(paramFiniteField.getCharacteristic());
/*     */     }
/*     */ 
/*     */     
/*  44 */     Polynomial polynomial = ((PolynomialExtensionField)paramFiniteField).getMinimalPolynomial();
/*  45 */     int[] arrayOfInt1 = polynomial.getExponentsPresent();
/*  46 */     int[] arrayOfInt2 = Arrays.reverse(Arrays.copyOfRange(arrayOfInt1, 1, arrayOfInt1.length - 1));
/*  47 */     return new ECFieldF2m(polynomial.getDegree(), arrayOfInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  57 */     super(convertCurve(paramECCurve, null), EC5Util.convertPoint(paramECPoint), paramBigInteger, 1);
/*     */     
/*  59 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECNamedCurveSpec(String paramString, EllipticCurve paramEllipticCurve, ECPoint paramECPoint, BigInteger paramBigInteger) {
/*  68 */     super(paramEllipticCurve, paramECPoint, paramBigInteger, 1);
/*     */     
/*  70 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  80 */     super(convertCurve(paramECCurve, null), EC5Util.convertPoint(paramECPoint), paramBigInteger1, paramBigInteger2.intValue());
/*     */     
/*  82 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECNamedCurveSpec(String paramString, EllipticCurve paramEllipticCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  92 */     super(paramEllipticCurve, paramECPoint, paramBigInteger1, paramBigInteger2.intValue());
/*     */     
/*  94 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
/* 105 */     super(convertCurve(paramECCurve, paramArrayOfbyte), EC5Util.convertPoint(paramECPoint), paramBigInteger1, paramBigInteger2.intValue());
/*     */     
/* 107 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 115 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jce/spec/ECNamedCurveSpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */