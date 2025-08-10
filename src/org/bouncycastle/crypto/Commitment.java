package org.bouncycastle.crypto;

public class Commitment {
  private final byte[] secret;
  
  private final byte[] commitment;
  
  public Commitment(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    this.secret = paramArrayOfbyte1;
    this.commitment = paramArrayOfbyte2;
  }
  
  public byte[] getSecret() {
    return this.secret;
  }
  
  public byte[] getCommitment() {
    return this.commitment;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/Commitment.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */