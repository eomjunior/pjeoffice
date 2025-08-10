/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*    */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*    */ import org.bouncycastle.math.ec.ECConstants;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ 
/*    */ 
/*    */ public class ECNamedDomainParameters
/*    */   extends ECDomainParameters
/*    */ {
/*    */   private ASN1ObjectIdentifier name;
/*    */   
/*    */   public ECNamedDomainParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger) {
/* 18 */     this(paramASN1ObjectIdentifier, paramECCurve, paramECPoint, paramBigInteger, ECConstants.ONE, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public ECNamedDomainParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 23 */     this(paramASN1ObjectIdentifier, paramECCurve, paramECPoint, paramBigInteger1, paramBigInteger2, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public ECNamedDomainParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
/* 28 */     super(paramECCurve, paramECPoint, paramBigInteger1, paramBigInteger2, paramArrayOfbyte);
/*    */     
/* 30 */     this.name = paramASN1ObjectIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECNamedDomainParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ECDomainParameters paramECDomainParameters) {
/* 35 */     super(paramECDomainParameters.getCurve(), paramECDomainParameters.getG(), paramECDomainParameters.getN(), paramECDomainParameters.getH(), paramECDomainParameters.getSeed());
/* 36 */     this.name = paramASN1ObjectIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public ECNamedDomainParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier, X9ECParameters paramX9ECParameters) {
/* 41 */     super(paramX9ECParameters);
/* 42 */     this.name = paramASN1ObjectIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getName() {
/* 47 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/ECNamedDomainParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */