package org.bouncycastle.jcajce;

import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;

public interface PKIXCertRevocationChecker {
  void setParameter(String paramString, Object paramObject);
  
  void initialize(PKIXCertRevocationCheckerParameters paramPKIXCertRevocationCheckerParameters) throws CertPathValidatorException;
  
  void check(Certificate paramCertificate) throws CertPathValidatorException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/PKIXCertRevocationChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */