package org.bouncycastle.crypto.engines;

public class ARIAWrapEngine extends RFC3394WrapEngine {
  public ARIAWrapEngine() {
    super(new ARIAEngine());
  }
  
  public ARIAWrapEngine(boolean paramBoolean) {
    super(new ARIAEngine(), paramBoolean);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/ARIAWrapEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */