package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.Arrays;

public class TweakableBlockCipherParameters implements CipherParameters {
  private final byte[] tweak;
  
  private final KeyParameter key;
  
  public TweakableBlockCipherParameters(KeyParameter paramKeyParameter, byte[] paramArrayOfbyte) {
    this.key = paramKeyParameter;
    this.tweak = Arrays.clone(paramArrayOfbyte);
  }
  
  public KeyParameter getKey() {
    return this.key;
  }
  
  public byte[] getTweak() {
    return this.tweak;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/TweakableBlockCipherParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */