package org.bouncycastle.pqc.math.linearalgebra;

import java.security.SecureRandom;

public class RandUtils {
  static int nextInt(SecureRandom paramSecureRandom, int paramInt) {
    if ((paramInt & -paramInt) == paramInt)
      return (int)(paramInt * (paramSecureRandom.nextInt() >>> 1) >> 31L); 
    while (true) {
      int i = paramSecureRandom.nextInt() >>> 1;
      int j = i % paramInt;
      if (i - j + paramInt - 1 >= 0)
        return j; 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/math/linearalgebra/RandUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */