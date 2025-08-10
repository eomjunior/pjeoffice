package org.bouncycastle.pqc.crypto.lms;

import org.bouncycastle.crypto.Digest;

class LmsUtils {
  static void u32str(int paramInt, Digest paramDigest) {
    paramDigest.update((byte)(paramInt >>> 24));
    paramDigest.update((byte)(paramInt >>> 16));
    paramDigest.update((byte)(paramInt >>> 8));
    paramDigest.update((byte)paramInt);
  }
  
  static void u16str(short paramShort, Digest paramDigest) {
    paramDigest.update((byte)(paramShort >>> 8));
    paramDigest.update((byte)paramShort);
  }
  
  static void byteArray(byte[] paramArrayOfbyte, Digest paramDigest) {
    paramDigest.update(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  static void byteArray(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, Digest paramDigest) {
    paramDigest.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  static int calculateStrength(LMSParameters paramLMSParameters) {
    if (paramLMSParameters == null)
      throw new NullPointerException("lmsParameters cannot be null"); 
    LMSigParameters lMSigParameters = paramLMSParameters.getLMSigParam();
    return (1 << lMSigParameters.getH()) * lMSigParameters.getM();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LmsUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */