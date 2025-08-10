package com.github.signer4j;

import java.nio.file.Path;

public interface IPkcs12Installer {
  IDeviceAccessor install(Path... paramVarArgs);
  
  IDeviceAccessor uninstall(Path... paramVarArgs);
  
  IDeviceAccessor uninstallPkcs12();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IPkcs12Installer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */