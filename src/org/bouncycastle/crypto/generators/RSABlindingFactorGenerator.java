package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class RSABlindingFactorGenerator {
  private static BigInteger ZERO = BigInteger.valueOf(0L);
  
  private static BigInteger ONE = BigInteger.valueOf(1L);
  
  private RSAKeyParameters key;
  
  private SecureRandom random;
  
  public void init(CipherParameters paramCipherParameters) {
    if (paramCipherParameters instanceof ParametersWithRandom) {
      ParametersWithRandom parametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.key = (RSAKeyParameters)parametersWithRandom.getParameters();
      this.random = parametersWithRandom.getRandom();
    } else {
      this.key = (RSAKeyParameters)paramCipherParameters;
      this.random = CryptoServicesRegistrar.getSecureRandom();
    } 
    if (this.key instanceof org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters)
      throw new IllegalArgumentException("generator requires RSA public key"); 
  }
  
  public BigInteger generateBlindingFactor() {
    if (this.key == null)
      throw new IllegalStateException("generator not initialised"); 
    BigInteger bigInteger = this.key.getModulus();
    int i = bigInteger.bitLength() - 1;
    while (true) {
      BigInteger bigInteger1 = BigIntegers.createRandomBigInteger(i, this.random);
      BigInteger bigInteger2 = bigInteger1.gcd(bigInteger);
      if (!bigInteger1.equals(ZERO) && !bigInteger1.equals(ONE) && bigInteger2.equals(ONE))
        return bigInteger1; 
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/generators/RSABlindingFactorGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */