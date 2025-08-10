package org.bouncycastle.crypto.engines;

public class AESWrapPadEngine extends RFC5649WrapEngine {
  public AESWrapPadEngine() {
    super(new AESEngine());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/AESWrapPadEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */