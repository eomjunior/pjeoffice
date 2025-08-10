package com.github.signer4j;

import com.github.signer4j.imp.Repository;

public interface IDeviceManager extends IDeviceAccessor, IPkcs12Installer {
  Repository getRepository();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IDeviceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */