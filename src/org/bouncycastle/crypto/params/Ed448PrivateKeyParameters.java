package org.bouncycastle.crypto.params;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import org.bouncycastle.math.ec.rfc8032.Ed448;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public final class Ed448PrivateKeyParameters extends AsymmetricKeyParameter {
  public static final int KEY_SIZE = 57;
  
  public static final int SIGNATURE_SIZE = 114;
  
  private final byte[] data = new byte[57];
  
  private Ed448PublicKeyParameters cachedPublicKey;
  
  public Ed448PrivateKeyParameters(SecureRandom paramSecureRandom) {
    super(true);
    Ed448.generatePrivateKey(paramSecureRandom, this.data);
  }
  
  public Ed448PrivateKeyParameters(byte[] paramArrayOfbyte, int paramInt) {
    super(true);
    System.arraycopy(paramArrayOfbyte, paramInt, this.data, 0, 57);
  }
  
  public Ed448PrivateKeyParameters(InputStream paramInputStream) throws IOException {
    super(true);
    if (57 != Streams.readFully(paramInputStream, this.data))
      throw new EOFException("EOF encountered in middle of Ed448 private key"); 
  }
  
  public void encode(byte[] paramArrayOfbyte, int paramInt) {
    System.arraycopy(this.data, 0, paramArrayOfbyte, paramInt, 57);
  }
  
  public byte[] getEncoded() {
    return Arrays.clone(this.data);
  }
  
  public Ed448PublicKeyParameters generatePublicKey() {
    synchronized (this.data) {
      if (null == this.cachedPublicKey) {
        byte[] arrayOfByte = new byte[57];
        Ed448.generatePublicKey(this.data, 0, arrayOfByte, 0);
        this.cachedPublicKey = new Ed448PublicKeyParameters(arrayOfByte, 0);
      } 
      return this.cachedPublicKey;
    } 
  }
  
  public void sign(int paramInt1, Ed448PublicKeyParameters paramEd448PublicKeyParameters, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3, byte[] paramArrayOfbyte3, int paramInt4) {
    sign(paramInt1, paramArrayOfbyte1, paramArrayOfbyte2, paramInt2, paramInt3, paramArrayOfbyte3, paramInt4);
  }
  
  public void sign(int paramInt1, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt2, int paramInt3, byte[] paramArrayOfbyte3, int paramInt4) {
    Ed448PublicKeyParameters ed448PublicKeyParameters = generatePublicKey();
    byte[] arrayOfByte = new byte[57];
    ed448PublicKeyParameters.encode(arrayOfByte, 0);
    switch (paramInt1) {
      case 0:
        Ed448.sign(this.data, 0, arrayOfByte, 0, paramArrayOfbyte1, paramArrayOfbyte2, paramInt2, paramInt3, paramArrayOfbyte3, paramInt4);
        return;
      case 1:
        if (64 != paramInt3)
          throw new IllegalArgumentException("msgLen"); 
        Ed448.signPrehash(this.data, 0, arrayOfByte, 0, paramArrayOfbyte1, paramArrayOfbyte2, paramInt2, paramArrayOfbyte3, paramInt4);
        return;
    } 
    throw new IllegalArgumentException("algorithm");
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/Ed448PrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */