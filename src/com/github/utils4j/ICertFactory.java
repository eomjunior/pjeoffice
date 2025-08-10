package com.github.utils4j;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

public interface ICertFactory {
  X509Certificate generateCertificate(InputStream paramInputStream) throws CertificateException;
  
  Iterator<String> getCertPathEncodings();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ICertFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */