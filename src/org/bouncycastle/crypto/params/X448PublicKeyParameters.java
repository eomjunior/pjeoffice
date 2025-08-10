package org.bouncycastle.crypto.params;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

public final class X448PublicKeyParameters extends AsymmetricKeyParameter {
  public static final int KEY_SIZE = 56;
  
  private final byte[] data = new byte[56];
  
  public X448PublicKeyParameters(byte[] paramArrayOfbyte, int paramInt) {
    super(false);
    System.arraycopy(paramArrayOfbyte, paramInt, this.data, 0, 56);
  }
  
  public X448PublicKeyParameters(InputStream paramInputStream) throws IOException {
    super(false);
    if (56 != Streams.readFully(paramInputStream, this.data))
      throw new EOFException("EOF encountered in middle of X448 public key"); 
  }
  
  public void encode(byte[] paramArrayOfbyte, int paramInt) {
    System.arraycopy(this.data, 0, paramArrayOfbyte, paramInt, 56);
  }
  
  public byte[] getEncoded() {
    return Arrays.clone(this.data);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/X448PublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */