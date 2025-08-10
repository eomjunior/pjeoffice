/*    */ package META-INF.versions.9.org.bouncycastle.math.ec.endo;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class ScalarSplitParameters {
/*    */   protected final BigInteger v1A;
/*    */   
/*    */   private static void checkVector(BigInteger[] paramArrayOfBigInteger, String paramString) {
/*  9 */     if (paramArrayOfBigInteger == null || paramArrayOfBigInteger.length != 2 || paramArrayOfBigInteger[0] == null || paramArrayOfBigInteger[1] == null)
/*    */     {
/* 11 */       throw new IllegalArgumentException("'" + paramString + "' must consist of exactly 2 (non-null) values"); } 
/*    */   }
/*    */   
/*    */   protected final BigInteger v1B;
/*    */   protected final BigInteger v2A;
/*    */   protected final BigInteger v2B;
/*    */   protected final BigInteger g1;
/*    */   protected final BigInteger g2;
/*    */   protected final int bits;
/*    */   
/*    */   public ScalarSplitParameters(BigInteger[] paramArrayOfBigInteger1, BigInteger[] paramArrayOfBigInteger2, BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt) {
/* 22 */     checkVector(paramArrayOfBigInteger1, "v1");
/* 23 */     checkVector(paramArrayOfBigInteger2, "v2");
/*    */     
/* 25 */     this.v1A = paramArrayOfBigInteger1[0];
/* 26 */     this.v1B = paramArrayOfBigInteger1[1];
/* 27 */     this.v2A = paramArrayOfBigInteger2[0];
/* 28 */     this.v2B = paramArrayOfBigInteger2[1];
/* 29 */     this.g1 = paramBigInteger1;
/* 30 */     this.g2 = paramBigInteger2;
/* 31 */     this.bits = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getV1A() {
/* 36 */     return this.v1A;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getV1B() {
/* 41 */     return this.v1B;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getV2A() {
/* 46 */     return this.v2A;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getV2B() {
/* 51 */     return this.v2B;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getG1() {
/* 56 */     return this.g1;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getG2() {
/* 61 */     return this.g2;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBits() {
/* 66 */     return this.bits;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/ScalarSplitParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */