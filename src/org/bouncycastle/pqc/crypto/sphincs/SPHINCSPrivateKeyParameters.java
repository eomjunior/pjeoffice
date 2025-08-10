package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.util.Arrays;

public class SPHINCSPrivateKeyParameters extends SPHINCSKeyParameters {
  private final byte[] keyData;
  
  public SPHINCSPrivateKeyParameters(byte[] paramArrayOfbyte) {
    super(true, null);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public SPHINCSPrivateKeyParameters(byte[] paramArrayOfbyte, String paramString) {
    super(true, paramString);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getKeyData() {
    return Arrays.clone(this.keyData);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/sphincs/SPHINCSPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */