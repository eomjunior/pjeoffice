package com.github.signer4j;

import java.io.File;
import java.io.IOException;

public interface IConfig {
  File getConfigFile() throws IOException;
  
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */