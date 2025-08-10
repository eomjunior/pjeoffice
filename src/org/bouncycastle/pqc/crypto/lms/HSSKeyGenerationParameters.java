package org.bouncycastle.pqc.crypto.lms;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class HSSKeyGenerationParameters extends KeyGenerationParameters {
  private final LMSParameters[] lmsParameters;
  
  public HSSKeyGenerationParameters(LMSParameters[] paramArrayOfLMSParameters, SecureRandom paramSecureRandom) {
    super(paramSecureRandom, LmsUtils.calculateStrength(paramArrayOfLMSParameters[0]));
    if (paramArrayOfLMSParameters.length == 0 || paramArrayOfLMSParameters.length > 8)
      throw new IllegalArgumentException("lmsParameters length should be between 1 and 8 inclusive"); 
    this.lmsParameters = paramArrayOfLMSParameters;
  }
  
  public int getDepth() {
    return this.lmsParameters.length;
  }
  
  public LMSParameters[] getLmsParameters() {
    return this.lmsParameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/HSSKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */