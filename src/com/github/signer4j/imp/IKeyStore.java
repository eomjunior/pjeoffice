package com.github.signer4j.imp;

import com.github.signer4j.IKeyStoreAccess;

interface IKeyStore extends AutoCloseable, IKeyStoreAccess {
  boolean isClosed();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/IKeyStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */