package org.bouncycastle.its.asn1;

import org.bouncycastle.util.Arrays;

class Utils {
  static byte[] octetStringFixed(byte[] paramArrayOfbyte, int paramInt) {
    if (paramArrayOfbyte.length != paramInt)
      throw new IllegalArgumentException("octet string out of range"); 
    return paramArrayOfbyte;
  }
  
  static byte[] octetStringFixed(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte.length < 1 || paramArrayOfbyte.length > 32)
      throw new IllegalArgumentException("octet string out of range"); 
    return Arrays.clone(paramArrayOfbyte);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */