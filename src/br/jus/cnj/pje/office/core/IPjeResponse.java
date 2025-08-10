package br.jus.cnj.pje.office.core;

import java.io.IOException;

public interface IPjeResponse {
  void write(byte[] paramArrayOfbyte) throws IOException;
  
  void write(byte[] paramArrayOfbyte, String paramString) throws IOException;
  
  void flush() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */