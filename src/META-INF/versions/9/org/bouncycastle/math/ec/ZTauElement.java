/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ZTauElement
/*    */ {
/*    */   public final BigInteger u;
/*    */   public final BigInteger v;
/*    */   
/*    */   public ZTauElement(BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/* 34 */     this.u = paramBigInteger1;
/* 35 */     this.v = paramBigInteger2;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ZTauElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */