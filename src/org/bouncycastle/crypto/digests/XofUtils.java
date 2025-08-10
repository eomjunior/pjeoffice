package org.bouncycastle.crypto.digests;

public class XofUtils {
  public static byte[] leftEncode(long paramLong) {
    byte b = 1;
    long l = paramLong;
    while ((l >>= 8L) != 0L)
      b = (byte)(b + 1); 
    byte[] arrayOfByte = new byte[b + 1];
    arrayOfByte[0] = b;
    for (byte b1 = 1; b1 <= b; b1++)
      arrayOfByte[b1] = (byte)(int)(paramLong >> 8 * (b - b1)); 
    return arrayOfByte;
  }
  
  public static byte[] rightEncode(long paramLong) {
    byte b = 1;
    long l = paramLong;
    while ((l >>= 8L) != 0L)
      b = (byte)(b + 1); 
    byte[] arrayOfByte = new byte[b + 1];
    arrayOfByte[b] = b;
    for (byte b1 = 0; b1 < b; b1++)
      arrayOfByte[b1] = (byte)(int)(paramLong >> 8 * (b - b1 - 1)); 
    return arrayOfByte;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/digests/XofUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */