package com.github.signer4j.cert;

import java.util.Optional;

public interface ISubjectAlternativeNames {
  boolean hasCertificatePF();
  
  Optional<ICertificatePF> getCertificatePF();
  
  boolean hasCertificatePJ();
  
  Optional<ICertificatePJ> getCertificatePJ();
  
  Optional<String> getEmail();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/ISubjectAlternativeNames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */