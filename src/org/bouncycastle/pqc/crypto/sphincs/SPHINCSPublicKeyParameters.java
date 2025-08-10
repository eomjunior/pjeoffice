package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.util.Arrays;

public class SPHINCSPublicKeyParameters extends SPHINCSKeyParameters {
  private final byte[] keyData;
  
  public SPHINCSPublicKeyParameters(byte[] paramArrayOfbyte) {
    super(false, null);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public SPHINCSPublicKeyParameters(byte[] paramArrayOfbyte, String paramString) {
    super(false, paramString);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getKeyData() {
    return Arrays.clone(this.keyData);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/sphincs/SPHINCSPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */