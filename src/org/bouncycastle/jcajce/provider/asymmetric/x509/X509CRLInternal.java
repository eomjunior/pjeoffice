package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.cert.CRLException;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.jcajce.util.JcaJceHelper;

class X509CRLInternal extends X509CRLImpl {
  private final byte[] encoding;
  
  X509CRLInternal(JcaJceHelper paramJcaJceHelper, CertificateList paramCertificateList, String paramString, byte[] paramArrayOfbyte1, boolean paramBoolean, byte[] paramArrayOfbyte2) {
    super(paramJcaJceHelper, paramCertificateList, paramString, paramArrayOfbyte1, paramBoolean);
    this.encoding = paramArrayOfbyte2;
  }
  
  public byte[] getEncoded() throws CRLException {
    if (null == this.encoding)
      throw new CRLException(); 
    return this.encoding;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/x509/X509CRLInternal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */