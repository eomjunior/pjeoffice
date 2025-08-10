package org.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class McElieceCCA2KeyGenerationParameters extends KeyGenerationParameters {
  private McElieceCCA2Parameters params;
  
  public McElieceCCA2KeyGenerationParameters(SecureRandom paramSecureRandom, McElieceCCA2Parameters paramMcElieceCCA2Parameters) {
    super(paramSecureRandom, 128);
    this.params = paramMcElieceCCA2Parameters;
  }
  
  public McElieceCCA2Parameters getParameters() {
    return this.params;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/mceliece/McElieceCCA2KeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */