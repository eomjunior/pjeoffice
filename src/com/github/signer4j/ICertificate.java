package com.github.signer4j;

import com.github.signer4j.cert.ICertificatePF;
import com.github.signer4j.cert.ICertificatePJ;
import com.github.signer4j.cert.IDistinguishedName;
import com.github.signer4j.cert.ISubjectAlternativeNames;
import com.github.signer4j.cert.imp.KeyUsage;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICertificate extends ISerialItem {
  Date getAfterDate();
  
  Date getBeforeDate();
  
  boolean isExpired();
  
  IDistinguishedName getCertificateIssuerDN();
  
  IDistinguishedName getCertificateSubjectDN();
  
  List<String> getCRLDistributionPoint() throws IOException;
  
  Optional<String> getEmail();
  
  Optional<ICertificatePF> getCertificatePF();
  
  Optional<ICertificatePJ> getCertificatePJ();
  
  KeyUsage getKeyUsage();
  
  ISubjectAlternativeNames getSubjectAlternativeNames();
  
  String getName();
  
  X509Certificate toX509();
  
  boolean hasCertificatePF();
  
  boolean hasCertificatePJ();
  
  Optional<String> getAlias();
  
  void setAlias(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */