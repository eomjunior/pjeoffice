package org.bouncycastle.cert.jcajce;

import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

class ProviderCertHelper extends CertHelper {
  private final Provider provider;
  
  ProviderCertHelper(Provider paramProvider) {
    this.provider = paramProvider;
  }
  
  protected CertificateFactory createCertificateFactory(String paramString) throws CertificateException {
    return CertificateFactory.getInstance(paramString, this.provider);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/jcajce/ProviderCertHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */