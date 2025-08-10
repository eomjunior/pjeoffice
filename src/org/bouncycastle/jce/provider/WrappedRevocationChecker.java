package org.bouncycastle.jce.provider;

import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import org.bouncycastle.jcajce.PKIXCertRevocationChecker;
import org.bouncycastle.jcajce.PKIXCertRevocationCheckerParameters;

class WrappedRevocationChecker implements PKIXCertRevocationChecker {
  private final PKIXCertPathChecker checker;
  
  public WrappedRevocationChecker(PKIXCertPathChecker paramPKIXCertPathChecker) {
    this.checker = paramPKIXCertPathChecker;
  }
  
  public void setParameter(String paramString, Object paramObject) {}
  
  public void initialize(PKIXCertRevocationCheckerParameters paramPKIXCertRevocationCheckerParameters) throws CertPathValidatorException {
    this.checker.init(false);
  }
  
  public void check(Certificate paramCertificate) throws CertPathValidatorException {
    this.checker.check(paramCertificate);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jce/provider/WrappedRevocationChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */