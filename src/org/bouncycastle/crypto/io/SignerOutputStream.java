package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Signer;

public class SignerOutputStream extends OutputStream {
  protected Signer signer;
  
  public SignerOutputStream(Signer paramSigner) {
    this.signer = paramSigner;
  }
  
  public void write(int paramInt) throws IOException {
    this.signer.update((byte)paramInt);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.signer.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public Signer getSigner() {
    return this.signer;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/io/SignerOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */