package org.bouncycastle.mail.smime.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CRLFOutputStream extends FilterOutputStream {
  protected int lastb = -1;
  
  protected static byte[] newline = new byte[2];
  
  public CRLFOutputStream(OutputStream paramOutputStream) {
    super(paramOutputStream);
  }
  
  public void write(int paramInt) throws IOException {
    if (paramInt == 13) {
      this.out.write(newline);
    } else if (paramInt == 10) {
      if (this.lastb != 13)
        this.out.write(newline); 
    } else {
      this.out.write(paramInt);
    } 
    this.lastb = paramInt;
  }
  
  public void write(byte[] paramArrayOfbyte) throws IOException {
    write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    for (int i = paramInt1; i != paramInt1 + paramInt2; i++)
      write(paramArrayOfbyte[i]); 
  }
  
  public void writeln() throws IOException {
    this.out.write(newline);
  }
  
  static {
    newline[0] = 13;
    newline[1] = 10;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/util/CRLFOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */