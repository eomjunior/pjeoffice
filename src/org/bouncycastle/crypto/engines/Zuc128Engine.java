package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Memoable;

public final class Zuc128Engine extends Zuc128CoreEngine {
  public Zuc128Engine() {}
  
  private Zuc128Engine(Zuc128Engine paramZuc128Engine) {
    super(paramZuc128Engine);
  }
  
  public Memoable copy() {
    return new Zuc128Engine(this);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/Zuc128Engine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */