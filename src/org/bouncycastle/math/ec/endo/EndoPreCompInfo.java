package org.bouncycastle.math.ec.endo;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.PreCompInfo;

public class EndoPreCompInfo implements PreCompInfo {
  protected ECEndomorphism endomorphism;
  
  protected ECPoint mappedPoint;
  
  public ECEndomorphism getEndomorphism() {
    return this.endomorphism;
  }
  
  public void setEndomorphism(ECEndomorphism paramECEndomorphism) {
    this.endomorphism = paramECEndomorphism;
  }
  
  public ECPoint getMappedPoint() {
    return this.mappedPoint;
  }
  
  public void setMappedPoint(ECPoint paramECPoint) {
    this.mappedPoint = paramECPoint;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/endo/EndoPreCompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */