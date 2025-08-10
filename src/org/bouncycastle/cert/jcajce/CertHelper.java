package org.bouncycastle.cert.jcajce;

import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

abstract class CertHelper {
  public CertificateFactory getCertificateFactory(String paramString) throws NoSuchProviderException, CertificateException {
    return createCertificateFactory(paramString);
  }
  
  protected abstract CertificateFactory createCertificateFactory(String paramString) throws CertificateException, NoSuchProviderException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/jcajce/CertHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */