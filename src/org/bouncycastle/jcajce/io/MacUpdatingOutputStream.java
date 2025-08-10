package org.bouncycastle.jcajce.io;

import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Mac;

class MacUpdatingOutputStream extends OutputStream {
  private Mac mac;
  
  MacUpdatingOutputStream(Mac paramMac) {
    this.mac = paramMac;
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    this.mac.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    this.mac.update(paramArrayOfbyte);
  }
  
  public void write(int paramInt) throws IOException {
    this.mac.update((byte)paramInt);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/io/MacUpdatingOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */