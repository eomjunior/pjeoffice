package org.bouncycastle.util;

public interface Memoable {
  Memoable copy();
  
  void reset(Memoable paramMemoable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/Memoable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */