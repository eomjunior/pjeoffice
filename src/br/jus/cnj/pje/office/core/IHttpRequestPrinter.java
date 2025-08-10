package br.jus.cnj.pje.office.core;

import java.io.IOException;
import java.io.InputStream;

public interface IHttpRequestPrinter {
  void uri(String paramString);
  
  void method(String paramString);
  
  void beginHeaders();
  
  void endHeaders();
  
  void keyValue(String paramString1, String paramString2);
  
  void beginQueryParams();
  
  void endQueryParams();
  
  void body(InputStream paramInputStream) throws IOException;
  
  void warning(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IHttpRequestPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */