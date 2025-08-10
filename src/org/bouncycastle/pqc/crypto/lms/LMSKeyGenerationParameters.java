package org.bouncycastle.pqc.crypto.lms;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class LMSKeyGenerationParameters extends KeyGenerationParameters {
  private final LMSParameters lmsParameters;
  
  public LMSKeyGenerationParameters(LMSParameters paramLMSParameters, SecureRandom paramSecureRandom) {
    super(paramSecureRandom, LmsUtils.calculateStrength(paramLMSParameters));
    this.lmsParameters = paramLMSParameters;
  }
  
  public LMSParameters getParameters() {
    return this.lmsParameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */