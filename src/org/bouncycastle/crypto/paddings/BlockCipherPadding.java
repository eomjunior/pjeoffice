package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public interface BlockCipherPadding {
  void init(SecureRandom paramSecureRandom) throws IllegalArgumentException;
  
  String getPaddingName();
  
  int addPadding(byte[] paramArrayOfbyte, int paramInt);
  
  int padCount(byte[] paramArrayOfbyte) throws InvalidCipherTextException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/paddings/BlockCipherPadding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */