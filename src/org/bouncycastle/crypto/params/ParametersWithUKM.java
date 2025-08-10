package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithUKM implements CipherParameters {
  private byte[] ukm;
  
  private CipherParameters parameters;
  
  public ParametersWithUKM(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte) {
    this(paramCipherParameters, paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public ParametersWithUKM(CipherParameters paramCipherParameters, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.ukm = new byte[paramInt2];
    this.parameters = paramCipherParameters;
    System.arraycopy(paramArrayOfbyte, paramInt1, this.ukm, 0, paramInt2);
  }
  
  public byte[] getUKM() {
    return this.ukm;
  }
  
  public CipherParameters getParameters() {
    return this.parameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/ParametersWithUKM.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */