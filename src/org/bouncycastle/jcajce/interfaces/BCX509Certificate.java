package org.bouncycastle.jcajce.interfaces;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.TBSCertificate;

public interface BCX509Certificate {
  X500Name getIssuerX500Name();
  
  TBSCertificate getTBSCertificateNative();
  
  X500Name getSubjectX500Name();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/interfaces/BCX509Certificate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */