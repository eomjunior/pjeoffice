/*    */ package META-INF.versions.9.org.bouncycastle.crypto.params;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.crypto.CipherParameters;
/*    */ import org.bouncycastle.crypto.params.DSAValidationParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DSAParameters
/*    */   implements CipherParameters
/*    */ {
/*    */   private BigInteger g;
/*    */   private BigInteger q;
/*    */   private BigInteger p;
/*    */   private DSAValidationParameters validation;
/*    */   
/*    */   public DSAParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3) {
/* 20 */     this.g = paramBigInteger3;
/* 21 */     this.p = paramBigInteger1;
/* 22 */     this.q = paramBigInteger2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DSAParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, DSAValidationParameters paramDSAValidationParameters) {
/* 31 */     this.g = paramBigInteger3;
/* 32 */     this.p = paramBigInteger1;
/* 33 */     this.q = paramBigInteger2;
/* 34 */     this.validation = paramDSAValidationParameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getP() {
/* 39 */     return this.p;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getQ() {
/* 44 */     return this.q;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getG() {
/* 49 */     return this.g;
/*    */   }
/*    */ 
/*    */   
/*    */   public DSAValidationParameters getValidationParameters() {
/* 54 */     return this.validation;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 60 */     if (!(paramObject instanceof org.bouncycastle.crypto.params.DSAParameters))
/*    */     {
/* 62 */       return false;
/*    */     }
/*    */     
/* 65 */     org.bouncycastle.crypto.params.DSAParameters dSAParameters = (org.bouncycastle.crypto.params.DSAParameters)paramObject;
/*    */     
/* 67 */     return (dSAParameters.getP().equals(this.p) && dSAParameters.getQ().equals(this.q) && dSAParameters.getG().equals(this.g));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     return getP().hashCode() ^ getQ().hashCode() ^ getG().hashCode();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/params/DSAParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */