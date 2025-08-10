package org.bouncycastle.cert.selector.jcajce;

import java.io.IOException;
import java.security.cert.X509CertSelector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.selector.X509CertificateHolderSelector;

public class JcaSelectorConverter {
  public X509CertificateHolderSelector getCertificateHolderSelector(X509CertSelector paramX509CertSelector) {
    try {
      return (paramX509CertSelector.getSubjectKeyIdentifier() != null) ? new X509CertificateHolderSelector(X500Name.getInstance(paramX509CertSelector.getIssuerAsBytes()), paramX509CertSelector.getSerialNumber(), ASN1OctetString.getInstance(paramX509CertSelector.getSubjectKeyIdentifier()).getOctets()) : new X509CertificateHolderSelector(X500Name.getInstance(paramX509CertSelector.getIssuerAsBytes()), paramX509CertSelector.getSerialNumber());
    } catch (IOException iOException) {
      throw new IllegalArgumentException("unable to convert issuer: " + iOException.getMessage());
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/selector/jcajce/JcaSelectorConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */