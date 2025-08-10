package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.crypto.DerivationParameters;

public class GSKKDFParameters implements DerivationParameters {
  private final byte[] z;
  
  private final int startCounter;
  
  private final byte[] nonce;
  
  public GSKKDFParameters(byte[] paramArrayOfbyte, int paramInt) {
    this(paramArrayOfbyte, paramInt, null);
  }
  
  public GSKKDFParameters(byte[] paramArrayOfbyte1, int paramInt, byte[] paramArrayOfbyte2) {
    this.z = paramArrayOfbyte1;
    this.startCounter = paramInt;
    this.nonce = paramArrayOfbyte2;
  }
  
  public byte[] getZ() {
    return this.z;
  }
  
  public int getStartCounter() {
    return this.startCounter;
  }
  
  public byte[] getNonce() {
    return this.nonce;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/agreement/kdf/GSKKDFParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */