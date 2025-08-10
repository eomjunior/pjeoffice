package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Memoable;

public final class Zuc256Engine extends Zuc256CoreEngine {
  public Zuc256Engine() {}
  
  public Zuc256Engine(int paramInt) {
    super(paramInt);
  }
  
  private Zuc256Engine(Zuc256Engine paramZuc256Engine) {
    super(paramZuc256Engine);
  }
  
  public Memoable copy() {
    return new Zuc256Engine(this);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/Zuc256Engine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */