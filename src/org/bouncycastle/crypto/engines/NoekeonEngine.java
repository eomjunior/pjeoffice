package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Integers;
import org.bouncycastle.util.Pack;

public class NoekeonEngine implements BlockCipher {
  private static final int SIZE = 16;
  
  private static final byte[] roundConstants = new byte[] { 
      Byte.MIN_VALUE, 27, 54, 108, -40, -85, 77, -102, 47, 94, 
      -68, 99, -58, -105, 53, 106, -44 };
  
  private final int[] k = new int[4];
  
  private boolean _initialised = false;
  
  private boolean _forEncryption;
  
  public String getAlgorithmName() {
    return "Noekeon";
  }
  
  public int getBlockSize() {
    return 16;
  }
  
  public void init(boolean paramBoolean, CipherParameters paramCipherParameters) {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to Noekeon init - " + paramCipherParameters.getClass().getName()); 
    this._forEncryption = paramBoolean;
    this._initialised = true;
    KeyParameter keyParameter = (KeyParameter)paramCipherParameters;
    Pack.bigEndianToInt(keyParameter.getKey(), 0, this.k, 0, 4);
    if (!paramBoolean) {
      int i = this.k[0];
      int j = this.k[1];
      int k = this.k[2];
      int m = this.k[3];
      int n = i ^ k;
      n ^= Integers.rotateLeft(n, 8) ^ Integers.rotateLeft(n, 24);
      j ^= n;
      m ^= n;
      n = j ^ m;
      n ^= Integers.rotateLeft(n, 8) ^ Integers.rotateLeft(n, 24);
      i ^= n;
      k ^= n;
      this.k[0] = i;
      this.k[1] = j;
      this.k[2] = k;
      this.k[3] = m;
    } 
  }
  
  public int processBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2) {
    if (!this._initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised"); 
    if (paramInt1 > paramArrayOfbyte1.length - 16)
      throw new DataLengthException("input buffer too short"); 
    if (paramInt2 > paramArrayOfbyte2.length - 16)
      throw new OutputLengthException("output buffer too short"); 
    return this._forEncryption ? encryptBlock(paramArrayOfbyte1, paramInt1, paramArrayOfbyte2, paramInt2) : decryptBlock(paramArrayOfbyte1, paramInt1, paramArrayOfbyte2, paramInt2);
  }
  
  public void reset() {}
  
  private int encryptBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2) {
    int i = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1);
    int j = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 4);
    int k = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 8);
    int m = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 12);
    int n = this.k[0];
    int i1 = this.k[1];
    int i2 = this.k[2];
    int i3 = this.k[3];
    byte b = 0;
    while (true) {
      i ^= roundConstants[b] & 0xFF;
      int i4 = i ^ k;
      i4 ^= Integers.rotateLeft(i4, 8) ^ Integers.rotateLeft(i4, 24);
      j ^= i4;
      m ^= i4;
      i ^= n;
      j ^= i1;
      k ^= i2;
      m ^= i3;
      i4 = j ^ m;
      i4 ^= Integers.rotateLeft(i4, 8) ^ Integers.rotateLeft(i4, 24);
      i ^= i4;
      k ^= i4;
      if (++b > 16) {
        Pack.intToBigEndian(i, paramArrayOfbyte2, paramInt2);
        Pack.intToBigEndian(j, paramArrayOfbyte2, paramInt2 + 4);
        Pack.intToBigEndian(k, paramArrayOfbyte2, paramInt2 + 8);
        Pack.intToBigEndian(m, paramArrayOfbyte2, paramInt2 + 12);
        return 16;
      } 
      j = Integers.rotateLeft(j, 1);
      k = Integers.rotateLeft(k, 5);
      m = Integers.rotateLeft(m, 2);
      j ^= (m ^ 0xFFFFFFFF) & (k ^ 0xFFFFFFFF);
      i ^= k & j;
      i4 = m;
      m = i;
      i = i4;
      k ^= i ^ j ^ m;
      j ^= (m ^ 0xFFFFFFFF) & (k ^ 0xFFFFFFFF);
      i ^= k & j;
      j = Integers.rotateLeft(j, 31);
      k = Integers.rotateLeft(k, 27);
      m = Integers.rotateLeft(m, 30);
    } 
  }
  
  private int decryptBlock(byte[] paramArrayOfbyte1, int paramInt1, byte[] paramArrayOfbyte2, int paramInt2) {
    int i = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1);
    int j = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 4);
    int k = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 8);
    int m = Pack.bigEndianToInt(paramArrayOfbyte1, paramInt1 + 12);
    int n = this.k[0];
    int i1 = this.k[1];
    int i2 = this.k[2];
    int i3 = this.k[3];
    byte b = 16;
    while (true) {
      int i4 = i ^ k;
      i4 ^= Integers.rotateLeft(i4, 8) ^ Integers.rotateLeft(i4, 24);
      j ^= i4;
      m ^= i4;
      i ^= n;
      j ^= i1;
      k ^= i2;
      m ^= i3;
      i4 = j ^ m;
      i4 ^= Integers.rotateLeft(i4, 8) ^ Integers.rotateLeft(i4, 24);
      i ^= i4;
      k ^= i4;
      i ^= roundConstants[b] & 0xFF;
      if (--b < 0) {
        Pack.intToBigEndian(i, paramArrayOfbyte2, paramInt2);
        Pack.intToBigEndian(j, paramArrayOfbyte2, paramInt2 + 4);
        Pack.intToBigEndian(k, paramArrayOfbyte2, paramInt2 + 8);
        Pack.intToBigEndian(m, paramArrayOfbyte2, paramInt2 + 12);
        return 16;
      } 
      j = Integers.rotateLeft(j, 1);
      k = Integers.rotateLeft(k, 5);
      m = Integers.rotateLeft(m, 2);
      j ^= (m ^ 0xFFFFFFFF) & (k ^ 0xFFFFFFFF);
      i ^= k & j;
      i4 = m;
      m = i;
      i = i4;
      k ^= i ^ j ^ m;
      j ^= (m ^ 0xFFFFFFFF) & (k ^ 0xFFFFFFFF);
      i ^= k & j;
      j = Integers.rotateLeft(j, 31);
      k = Integers.rotateLeft(k, 27);
      m = Integers.rotateLeft(m, 30);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/engines/NoekeonEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */