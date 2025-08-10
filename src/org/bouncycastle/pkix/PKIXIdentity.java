package org.bouncycastle.pkix;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;

public class PKIXIdentity {
  private final PrivateKeyInfo privateKeyInfo;
  
  private final X509CertificateHolder[] certificateHolders;
  
  public PKIXIdentity(PrivateKeyInfo paramPrivateKeyInfo, X509CertificateHolder[] paramArrayOfX509CertificateHolder) {
    this.privateKeyInfo = paramPrivateKeyInfo;
    this.certificateHolders = new X509CertificateHolder[paramArrayOfX509CertificateHolder.length];
    System.arraycopy(paramArrayOfX509CertificateHolder, 0, this.certificateHolders, 0, paramArrayOfX509CertificateHolder.length);
  }
  
  public PKIXIdentity(PrivateKeyInfo paramPrivateKeyInfo, X509CertificateHolder paramX509CertificateHolder) {
    this(paramPrivateKeyInfo, new X509CertificateHolder[] { paramX509CertificateHolder });
  }
  
  public PrivateKeyInfo getPrivateKeyInfo() {
    return this.privateKeyInfo;
  }
  
  public X509CertificateHolder getCertificate() {
    return this.certificateHolders[0];
  }
  
  public X509CertificateHolder[] getCertificateChain() {
    X509CertificateHolder[] arrayOfX509CertificateHolder = new X509CertificateHolder[this.certificateHolders.length];
    System.arraycopy(this.certificateHolders, 0, arrayOfX509CertificateHolder, 0, arrayOfX509CertificateHolder.length);
    return arrayOfX509CertificateHolder;
  }
  
  public RecipientId getRecipientId() {
    return (RecipientId)new KeyTransRecipientId(this.certificateHolders[0].getIssuer(), this.certificateHolders[0].getSerialNumber(), getSubjectKeyIdentifier());
  }
  
  private byte[] getSubjectKeyIdentifier() {
    SubjectKeyIdentifier subjectKeyIdentifier = SubjectKeyIdentifier.fromExtensions(this.certificateHolders[0].getExtensions());
    return (subjectKeyIdentifier == null) ? null : subjectKeyIdentifier.getKeyIdentifier();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pkix/PKIXIdentity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */