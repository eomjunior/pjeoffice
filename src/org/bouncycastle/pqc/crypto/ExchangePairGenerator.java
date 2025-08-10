package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface ExchangePairGenerator {
  ExchangePair GenerateExchange(AsymmetricKeyParameter paramAsymmetricKeyParameter);
  
  ExchangePair generateExchange(AsymmetricKeyParameter paramAsymmetricKeyParameter);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/ExchangePairGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */