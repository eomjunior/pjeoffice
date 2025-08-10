package org.bouncycastle.math.ec;

public class WNafPreCompInfo implements PreCompInfo {
  volatile int promotionCountdown = 4;
  
  protected int confWidth = -1;
  
  protected ECPoint[] preComp = null;
  
  protected ECPoint[] preCompNeg = null;
  
  protected ECPoint twice = null;
  
  protected int width = -1;
  
  int decrementPromotionCountdown() {
    int i = this.promotionCountdown;
    if (i > 0)
      this.promotionCountdown = --i; 
    return i;
  }
  
  int getPromotionCountdown() {
    return this.promotionCountdown;
  }
  
  void setPromotionCountdown(int paramInt) {
    this.promotionCountdown = paramInt;
  }
  
  public boolean isPromoted() {
    return (this.promotionCountdown <= 0);
  }
  
  public int getConfWidth() {
    return this.confWidth;
  }
  
  public void setConfWidth(int paramInt) {
    this.confWidth = paramInt;
  }
  
  public ECPoint[] getPreComp() {
    return this.preComp;
  }
  
  public void setPreComp(ECPoint[] paramArrayOfECPoint) {
    this.preComp = paramArrayOfECPoint;
  }
  
  public ECPoint[] getPreCompNeg() {
    return this.preCompNeg;
  }
  
  public void setPreCompNeg(ECPoint[] paramArrayOfECPoint) {
    this.preCompNeg = paramArrayOfECPoint;
  }
  
  public ECPoint getTwice() {
    return this.twice;
  }
  
  public void setTwice(ECPoint paramECPoint) {
    this.twice = paramECPoint;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public void setWidth(int paramInt) {
    this.width = paramInt;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/WNafPreCompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */