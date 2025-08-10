/*    */ package META-INF.versions.9.org.bouncycastle.math.ec;
/*    */ 
/*    */ import org.bouncycastle.math.ec.PreCompInfo;
/*    */ 
/*    */ class ValidityPrecompInfo
/*    */   implements PreCompInfo {
/*    */   static final String PRECOMP_NAME = "bc_validity";
/*    */   private boolean failed = false;
/*    */   private boolean curveEquationPassed = false;
/*    */   private boolean orderPassed = false;
/*    */   
/*    */   boolean hasFailed() {
/* 13 */     return this.failed;
/*    */   }
/*    */ 
/*    */   
/*    */   void reportFailed() {
/* 18 */     this.failed = true;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean hasCurveEquationPassed() {
/* 23 */     return this.curveEquationPassed;
/*    */   }
/*    */ 
/*    */   
/*    */   void reportCurveEquationPassed() {
/* 28 */     this.curveEquationPassed = true;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean hasOrderPassed() {
/* 33 */     return this.orderPassed;
/*    */   }
/*    */ 
/*    */   
/*    */   void reportOrderPassed() {
/* 38 */     this.orderPassed = true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/ValidityPrecompInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */