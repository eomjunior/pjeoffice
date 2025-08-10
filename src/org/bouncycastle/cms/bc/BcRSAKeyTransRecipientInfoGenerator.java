package org.bouncycastle.cms.bc;

import java.io.IOException;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.operator.bc.BcAsymmetricKeyWrapper;
import org.bouncycastle.operator.bc.BcRSAAsymmetricKeyWrapper;

public class BcRSAKeyTransRecipientInfoGenerator extends BcKeyTransRecipientInfoGenerator {
  public BcRSAKeyTransRecipientInfoGenerator(byte[] paramArrayOfbyte, AlgorithmIdentifier paramAlgorithmIdentifier, AsymmetricKeyParameter paramAsymmetricKeyParameter) {
    super(paramArrayOfbyte, (BcAsymmetricKeyWrapper)new BcRSAAsymmetricKeyWrapper(paramAlgorithmIdentifier, paramAsymmetricKeyParameter));
  }
  
  public BcRSAKeyTransRecipientInfoGenerator(X509CertificateHolder paramX509CertificateHolder) throws IOException {
    super(paramX509CertificateHolder, (BcAsymmetricKeyWrapper)new BcRSAAsymmetricKeyWrapper(paramX509CertificateHolder.getSubjectPublicKeyInfo().getAlgorithm(), paramX509CertificateHolder.getSubjectPublicKeyInfo()));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/bc/BcRSAKeyTransRecipientInfoGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */