package org.bouncycastle.math.ec;

public class ScaleXNegateYPointMap implements ECPointMap {
  protected final ECFieldElement scale;
  
  public ScaleXNegateYPointMap(ECFieldElement paramECFieldElement) {
    this.scale = paramECFieldElement;
  }
  
  public ECPoint map(ECPoint paramECPoint) {
    return paramECPoint.scaleXNegateY(this.scale);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/ScaleXNegateYPointMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */