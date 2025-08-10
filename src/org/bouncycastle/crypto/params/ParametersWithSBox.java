package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSBox implements CipherParameters {
  private CipherParameters parameters;
  
  private byte[] sBox;
  
  public ParametersWithSBox(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte) {
    this.parameters = paramCipherParameters;
    this.sBox = paramArrayOfbyte;
  }
  
  public byte[] getSBox() {
    return this.sBox;
  }
  
  public CipherParameters getParameters() {
    return this.parameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/ParametersWithSBox.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */