package org.bouncycastle.cert.ocsp.jcajce;

import java.security.PublicKey;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.ocsp.BasicOCSPRespBuilder;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.operator.DigestCalculator;

public class JcaBasicOCSPRespBuilder extends BasicOCSPRespBuilder {
  public JcaBasicOCSPRespBuilder(X500Principal paramX500Principal) {
    super(new JcaRespID(paramX500Principal));
  }
  
  public JcaBasicOCSPRespBuilder(PublicKey paramPublicKey, DigestCalculator paramDigestCalculator) throws OCSPException {
    super(SubjectPublicKeyInfo.getInstance(paramPublicKey.getEncoded()), paramDigestCalculator);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/ocsp/jcajce/JcaBasicOCSPRespBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */