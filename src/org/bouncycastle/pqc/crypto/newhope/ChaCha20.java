package org.bouncycastle.pqc.crypto.newhope;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

class ChaCha20 {
  static void process(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2) {
    ChaChaEngine chaChaEngine = new ChaChaEngine(20);
    chaChaEngine.init(true, (CipherParameters)new ParametersWithIV((CipherParameters)new KeyParameter(paramArrayOfbyte1), paramArrayOfbyte2));
    chaChaEngine.processBytes(paramArrayOfbyte3, paramInt1, paramInt2, paramArrayOfbyte3, paramInt1);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/newhope/ChaCha20.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */