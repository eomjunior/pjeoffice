/*    */ package META-INF.versions.9.org.bouncycastle.math.ec.endo;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.math.ec.endo.ScalarSplitParameters;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GLVTypeBParameters
/*    */ {
/*    */   protected final BigInteger beta;
/*    */   protected final BigInteger lambda;
/*    */   protected final ScalarSplitParameters splitParams;
/*    */   
/*    */   public GLVTypeBParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger[] paramArrayOfBigInteger1, BigInteger[] paramArrayOfBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, int paramInt) {
/* 16 */     this.beta = paramBigInteger1;
/* 17 */     this.lambda = paramBigInteger2;
/* 18 */     this.splitParams = new ScalarSplitParameters(paramArrayOfBigInteger1, paramArrayOfBigInteger2, paramBigInteger3, paramBigInteger4, paramInt);
/*    */   }
/*    */ 
/*    */   
/*    */   public GLVTypeBParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, ScalarSplitParameters paramScalarSplitParameters) {
/* 23 */     this.beta = paramBigInteger1;
/* 24 */     this.lambda = paramBigInteger2;
/* 25 */     this.splitParams = paramScalarSplitParameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getBeta() {
/* 30 */     return this.beta;
/*    */   }
/*    */ 
/*    */   
/*    */   public BigInteger getLambda() {
/* 35 */     return this.lambda;
/*    */   }
/*    */ 
/*    */   
/*    */   public ScalarSplitParameters getSplitParams() {
/* 40 */     return this.splitParams;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getV1A() {
/* 48 */     return getSplitParams().getV1A();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getV1B() {
/* 56 */     return getSplitParams().getV1B();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getV2A() {
/* 64 */     return getSplitParams().getV2A();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getV2B() {
/* 72 */     return getSplitParams().getV2B();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getG1() {
/* 80 */     return getSplitParams().getG1();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BigInteger getG2() {
/* 88 */     return getSplitParams().getG2();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBits() {
/* 96 */     return getSplitParams().getBits();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/endo/GLVTypeBParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */