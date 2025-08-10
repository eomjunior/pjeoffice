package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.cert.CertificateEncodingException;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.jcajce.util.JcaJceHelper;

class X509CertificateInternal extends X509CertificateImpl {
  private final byte[] encoding;
  
  X509CertificateInternal(JcaJceHelper paramJcaJceHelper, Certificate paramCertificate, BasicConstraints paramBasicConstraints, boolean[] paramArrayOfboolean, String paramString, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    super(paramJcaJceHelper, paramCertificate, paramBasicConstraints, paramArrayOfboolean, paramString, paramArrayOfbyte1);
    this.encoding = paramArrayOfbyte2;
  }
  
  public byte[] getEncoded() throws CertificateEncodingException {
    if (null == this.encoding)
      throw new CertificateEncodingException(); 
    return this.encoding;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/x509/X509CertificateInternal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */