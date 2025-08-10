package META-INF.versions.9.org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.pqc.crypto.lms.LMSContext;

public interface LMSContextBasedVerifier {
  LMSContext generateLMSContext(byte[] paramArrayOfbyte);
  
  boolean verify(LMSContext paramLMSContext);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/lms/LMSContextBasedVerifier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */