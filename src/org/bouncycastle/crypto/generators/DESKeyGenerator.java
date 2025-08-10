package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DESParameters;

public class DESKeyGenerator extends CipherKeyGenerator {
  public void init(KeyGenerationParameters paramKeyGenerationParameters) {
    super.init(paramKeyGenerationParameters);
    if (this.strength == 0 || this.strength == 7) {
      this.strength = 8;
    } else if (this.strength != 8) {
      throw new IllegalArgumentException("DES key must be 64 bits long.");
    } 
  }
  
  public byte[] generateKey() {
    byte[] arrayOfByte = new byte[8];
    while (true) {
      this.random.nextBytes(arrayOfByte);
      DESParameters.setOddParity(arrayOfByte);
      if (!DESParameters.isWeakKey(arrayOfByte, 0))
        return arrayOfByte; 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/generators/DESKeyGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */