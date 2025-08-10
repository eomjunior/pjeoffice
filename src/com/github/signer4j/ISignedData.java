package com.github.signer4j;

import java.io.IOException;
import java.io.OutputStream;

public interface ISignedData extends IPersonalData {
  byte[] getSignature();
  
  void writeTo(OutputStream paramOutputStream) throws IOException;
  
  String getSignature64();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ISignedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */