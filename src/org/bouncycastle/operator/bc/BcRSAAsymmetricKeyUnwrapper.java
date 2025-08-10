package org.bouncycastle.operator.bc;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class BcRSAAsymmetricKeyUnwrapper extends BcAsymmetricKeyUnwrapper {
  public BcRSAAsymmetricKeyUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier, AsymmetricKeyParameter paramAsymmetricKeyParameter) {
    super(paramAlgorithmIdentifier, paramAsymmetricKeyParameter);
  }
  
  protected AsymmetricBlockCipher createAsymmetricUnwrapper(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    return (AsymmetricBlockCipher)new PKCS1Encoding((AsymmetricBlockCipher)new RSABlindedEngine());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/bc/BcRSAAsymmetricKeyUnwrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */