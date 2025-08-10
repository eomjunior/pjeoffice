/*     */ package META-INF.versions.9.org.bouncycastle.jcajce.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import javax.crypto.spec.DHParameterSpec;
/*     */ import org.bouncycastle.crypto.params.DHParameters;
/*     */ import org.bouncycastle.crypto.params.DHValidationParameters;
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
/*     */ public class DHDomainParameterSpec
/*     */   extends DHParameterSpec
/*     */ {
/*     */   private final BigInteger q;
/*     */   private final BigInteger j;
/*     */   private final int m;
/*     */   private DHValidationParameters validationParameters;
/*     */   
/*     */   public DHDomainParameterSpec(DHParameters paramDHParameters) {
/*  29 */     this(paramDHParameters.getP(), paramDHParameters.getQ(), paramDHParameters.getG(), paramDHParameters.getJ(), paramDHParameters.getM(), paramDHParameters.getL());
/*  30 */     this.validationParameters = paramDHParameters.getValidationParameters();
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
/*     */   public DHDomainParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3) {
/*  42 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, null, 0);
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
/*     */   public DHDomainParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt) {
/*  55 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, null, paramInt);
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
/*     */   public DHDomainParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, int paramInt) {
/*  69 */     this(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4, 0, paramInt);
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
/*     */   public DHDomainParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, int paramInt1, int paramInt2) {
/*  84 */     super(paramBigInteger1, paramBigInteger3, paramInt2);
/*  85 */     this.q = paramBigInteger2;
/*  86 */     this.j = paramBigInteger4;
/*  87 */     this.m = paramInt1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getQ() {
/*  97 */     return this.q;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getJ() {
/* 107 */     return this.j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getM() {
/* 117 */     return this.m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DHParameters getDomainParameters() {
/* 127 */     return new DHParameters(getP(), getG(), this.q, this.m, getL(), this.j, this.validationParameters);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jcajce/spec/DHDomainParameterSpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */