package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSalt implements CipherParameters {
  private byte[] salt;
  
  private CipherParameters parameters;
  
  public ParametersWithSalt(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte) {
    this(paramCipherParameters, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public ParametersWithSalt(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.salt = new byte[paramInt2];
    this.parameters = paramCipherParameters;
    System.arraycopy(paramArrayOfbyte, paramInt1, this.salt, 0, paramInt2);
  }
  
  public byte[] getSalt() {
    return this.salt;
  }
  
  public CipherParameters getParameters() {
    return this.parameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/ParametersWithSalt.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */