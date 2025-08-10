package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;

interface DRBGProvider {
  String getAlgorithm();
  
  SP80090DRBG get(EntropySource paramEntropySource);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/prng/DRBGProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */