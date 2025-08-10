package org.bouncycastle.pqc.crypto.lms;

public interface LMSContextBasedSigner {
  LMSContext generateLMSContext();
  
  byte[] generateSignature(LMSContext paramLMSContext);
  
  long getUsagesRemaining();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSContextBasedSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */