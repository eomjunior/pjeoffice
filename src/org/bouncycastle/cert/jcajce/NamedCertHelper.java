package org.bouncycastle.cert.jcajce;

import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

class NamedCertHelper extends CertHelper {
  private final String providerName;
  
  NamedCertHelper(String paramString) {
    this.providerName = paramString;
  }
  
  protected CertificateFactory createCertificateFactory(String paramString) throws CertificateException, NoSuchProviderException {
    return CertificateFactory.getInstance(paramString, this.providerName);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/jcajce/NamedCertHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */