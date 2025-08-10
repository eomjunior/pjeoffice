package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;

public interface Tokenizer {
  String getToken(Reader paramReader) throws IOException;
  
  String getPostToken();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/Tokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */