package org.bouncycastle.math.ec;

import java.math.BigInteger;

public abstract class AbstractECMultiplier implements ECMultiplier {
  public ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger) {
    int i = paramBigInteger.signum();
    if (i == 0 || paramECPoint.isInfinity())
      return paramECPoint.getCurve().getInfinity(); 
    ECPoint eCPoint1 = multiplyPositive(paramECPoint, paramBigInteger.abs());
    ECPoint eCPoint2 = (i > 0) ? eCPoint1 : eCPoint1.negate();
    return checkResult(eCPoint2);
  }
  
  protected abstract ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger);
  
  protected ECPoint checkResult(ECPoint paramECPoint) {
    return ECAlgorithms.implCheckResult(paramECPoint);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/AbstractECMultiplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */