package org.bouncycastle.math.ec;

public class ScaleYNegateXPointMap implements ECPointMap {
  protected final ECFieldElement scale;
  
  public ScaleYNegateXPointMap(ECFieldElement paramECFieldElement) {
    this.scale = paramECFieldElement;
  }
  
  public ECPoint map(ECPoint paramECPoint) {
    return paramECPoint.scaleYNegateX(this.scale);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/ScaleYNegateXPointMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */