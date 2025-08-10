package org.bouncycastle.crypto.engines;

public class SEEDWrapEngine extends RFC3394WrapEngine {
  public SEEDWrapEngine() {
    super(new SEEDEngine());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/SEEDWrapEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */