package br.jus.cnj.pje.office.core;

import java.io.File;
import java.io.IOException;

public interface IPjeHttpExchangeResponse extends IPjeResponse {
  void write(File paramFile) throws IOException;
  
  void notFound() throws IOException;
  
  void success() throws IOException;
  
  void fail(int paramInt) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeHttpExchangeResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */