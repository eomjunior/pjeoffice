package com.github.signer4j;

import java.security.Signature;

public interface ISignatureAlgorithm extends IAlgorithm {
  IHashAlgorithm getHashAlgorithm();
  
  Signature toSignature() throws Exception;
  
  Signature toSignature(String paramString) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ISignatureAlgorithm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */