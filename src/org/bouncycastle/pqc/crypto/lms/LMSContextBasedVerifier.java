package org.bouncycastle.pqc.crypto.lms;

public interface LMSContextBasedVerifier {
  LMSContext generateLMSContext(byte[] paramArrayOfbyte);
  
  boolean verify(LMSContext paramLMSContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSContextBasedVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */