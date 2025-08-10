package com.github.signer4j;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

public interface IPersonalData {
  PrivateKey getPrivateKey();
  
  Certificate getCertificate();
  
  List<Certificate> getCertificateChain();
  
  String getCertificate64() throws CertificateException;
  
  String getCertificateChain64() throws CertificateException;
  
  int chainSize();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IPersonalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */