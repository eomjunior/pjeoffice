package org.bouncycastle.jce.interfaces;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface IESKey extends Key {
  PublicKey getPublic();
  
  PrivateKey getPrivate();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jce/interfaces/IESKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */