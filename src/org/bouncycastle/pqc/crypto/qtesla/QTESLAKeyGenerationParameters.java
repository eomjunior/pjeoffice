package org.bouncycastle.pqc.crypto.qtesla;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class QTESLAKeyGenerationParameters extends KeyGenerationParameters {
  private final int securityCategory;
  
  public QTESLAKeyGenerationParameters(int paramInt, SecureRandom paramSecureRandom) {
    super(paramSecureRandom, -1);
    QTESLASecurityCategory.getPrivateSize(paramInt);
    this.securityCategory = paramInt;
  }
  
  public int getSecurityCategory() {
    return this.securityCategory;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/qtesla/QTESLAKeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */