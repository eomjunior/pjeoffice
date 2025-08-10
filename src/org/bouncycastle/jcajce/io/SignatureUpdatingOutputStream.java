package org.bouncycastle.jcajce.io;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Signature;
import java.security.SignatureException;

class SignatureUpdatingOutputStream extends OutputStream {
  private Signature sig;
  
  SignatureUpdatingOutputStream(Signature paramSignature) {
    this.sig = paramSignature;
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    try {
      this.sig.update(paramArrayOfbyte, paramInt1, paramInt2);
    } catch (SignatureException signatureException) {
      throw new IOException(signatureException.getMessage());
    } 
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    try {
      this.sig.update(paramArrayOfbyte);
    } catch (SignatureException signatureException) {
      throw new IOException(signatureException.getMessage());
    } 
  }
  
  public void write(int paramInt) throws IOException {
    try {
      this.sig.update((byte)paramInt);
    } catch (SignatureException signatureException) {
      throw new IOException(signatureException.getMessage());
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/io/SignatureUpdatingOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */