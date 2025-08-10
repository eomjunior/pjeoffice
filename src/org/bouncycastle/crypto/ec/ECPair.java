package org.bouncycastle.crypto.ec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPair {
  private final ECPoint x;
  
  private final ECPoint y;
  
  public ECPair(ECPoint paramECPoint1, ECPoint paramECPoint2) {
    this.x = paramECPoint1;
    this.y = paramECPoint2;
  }
  
  public ECPoint getX() {
    return this.x;
  }
  
  public ECPoint getY() {
    return this.y;
  }
  
  public boolean equals(ECPair paramECPair) {
    return (paramECPair.getX().equals(getX()) && paramECPair.getY().equals(getY()));
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof ECPair) ? equals((ECPair)paramObject) : false;
  }
  
  public int hashCode() {
    return this.x.hashCode() + 37 * this.y.hashCode();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/ec/ECPair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */