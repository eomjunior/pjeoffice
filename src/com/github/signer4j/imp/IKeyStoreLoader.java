package com.github.signer4j.imp;

import com.github.signer4j.IKeyStoreAccess;
import com.github.signer4j.imp.exception.Signer4JException;
import com.github.utils4j.IParams;

interface IKeyStoreLoader {
  IKeyStoreAccess getKeyStore(IParams paramIParams) throws Signer4JException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/IKeyStoreLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */