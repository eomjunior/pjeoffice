package org.bouncycastle.pqc.jcajce.provider.mceliece;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.mceliece.McElieceKeyPairGenerator;
import org.bouncycastle.pqc.crypto.mceliece.McElieceParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import org.bouncycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import org.bouncycastle.pqc.jcajce.spec.McElieceKeyGenParameterSpec;

public class McElieceKeyPairGeneratorSpi extends KeyPairGenerator {
  McElieceKeyPairGenerator kpg;
  
  public McElieceKeyPairGeneratorSpi() {
    super("McEliece");
  }
  
  public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidAlgorithmParameterException {
    this.kpg = new McElieceKeyPairGenerator();
    McElieceKeyGenParameterSpec mcElieceKeyGenParameterSpec = (McElieceKeyGenParameterSpec)paramAlgorithmParameterSpec;
    McElieceKeyGenerationParameters mcElieceKeyGenerationParameters = new McElieceKeyGenerationParameters(paramSecureRandom, new McElieceParameters(mcElieceKeyGenParameterSpec.getM(), mcElieceKeyGenParameterSpec.getT()));
    this.kpg.init((KeyGenerationParameters)mcElieceKeyGenerationParameters);
  }
  
  public void initialize(int paramInt, SecureRandom paramSecureRandom) {
    McElieceKeyGenParameterSpec mcElieceKeyGenParameterSpec = new McElieceKeyGenParameterSpec();
    try {
      initialize((AlgorithmParameterSpec)mcElieceKeyGenParameterSpec, paramSecureRandom);
    } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {}
  }
  
  public KeyPair generateKeyPair() {
    AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.kpg.generateKeyPair();
    McEliecePrivateKeyParameters mcEliecePrivateKeyParameters = (McEliecePrivateKeyParameters)asymmetricCipherKeyPair.getPrivate();
    McEliecePublicKeyParameters mcEliecePublicKeyParameters = (McEliecePublicKeyParameters)asymmetricCipherKeyPair.getPublic();
    return new KeyPair(new BCMcEliecePublicKey(mcEliecePublicKeyParameters), new BCMcEliecePrivateKey(mcEliecePrivateKeyParameters));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/jcajce/provider/mceliece/McElieceKeyPairGeneratorSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */