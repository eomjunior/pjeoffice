package org.bouncycastle.jcajce.provider.asymmetric.dh;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAlgorithmParameterGeneratorSpi;
import org.bouncycastle.jcajce.provider.asymmetric.util.PrimeCertaintyCalculator;

public class AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGeneratorSpi {
  protected SecureRandom random;
  
  protected int strength = 2048;
  
  private int l = 0;
  
  protected void engineInit(int paramInt, SecureRandom paramSecureRandom) {
    this.strength = paramInt;
    this.random = paramSecureRandom;
  }
  
  protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidAlgorithmParameterException {
    if (!(paramAlgorithmParameterSpec instanceof DHGenParameterSpec))
      throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation"); 
    DHGenParameterSpec dHGenParameterSpec = (DHGenParameterSpec)paramAlgorithmParameterSpec;
    this.strength = dHGenParameterSpec.getPrimeSize();
    this.l = dHGenParameterSpec.getExponentSize();
    this.random = paramSecureRandom;
  }
  
  protected AlgorithmParameters engineGenerateParameters() {
    AlgorithmParameters algorithmParameters;
    DHParametersGenerator dHParametersGenerator = new DHParametersGenerator();
    int i = PrimeCertaintyCalculator.getDefaultCertainty(this.strength);
    dHParametersGenerator.init(this.strength, i, CryptoServicesRegistrar.getSecureRandom(this.random));
    DHParameters dHParameters = dHParametersGenerator.generateParameters();
    try {
      algorithmParameters = createParametersInstance("DH");
      algorithmParameters.init(new DHParameterSpec(dHParameters.getP(), dHParameters.getG(), this.l));
    } catch (Exception exception) {
      throw new RuntimeException(exception.getMessage());
    } 
    return algorithmParameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/dh/AlgorithmParameterGeneratorSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */