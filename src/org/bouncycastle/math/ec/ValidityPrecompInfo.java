package org.bouncycastle.math.ec;

class ValidityPrecompInfo implements PreCompInfo {
  static final String PRECOMP_NAME = "bc_validity";
  
  private boolean failed = false;
  
  private boolean curveEquationPassed = false;
  
  private boolean orderPassed = false;
  
  boolean hasFailed() {
    return this.failed;
  }
  
  void reportFailed() {
    this.failed = true;
  }
  
  boolean hasCurveEquationPassed() {
    return this.curveEquationPassed;
  }
  
  void reportCurveEquationPassed() {
    this.curveEquationPassed = true;
  }
  
  boolean hasOrderPassed() {
    return this.orderPassed;
  }
  
  void reportOrderPassed() {
    this.orderPassed = true;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/ValidityPrecompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */