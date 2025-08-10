package org.bouncycastle.util.encoders;

public interface Translator {
  int getEncodedBlockSize();
  
  int encode(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3);
  
  int getDecodedBlockSize();
  
  int decode(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/encoders/Translator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */