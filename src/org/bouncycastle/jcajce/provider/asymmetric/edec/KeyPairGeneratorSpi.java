package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.generators.Ed448KeyPairGenerator;
import org.bouncycastle.crypto.generators.X25519KeyPairGenerator;
import org.bouncycastle.crypto.generators.X448KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed448KeyGenerationParameters;
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.X448KeyGenerationParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.spec.EdDSAParameterSpec;
import org.bouncycastle.jcajce.spec.XDHParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;

public class KeyPairGeneratorSpi extends KeyPairGeneratorSpi {
  private static final int EdDSA = -1;
  
  private static final int XDH = -2;
  
  private static final int Ed448 = 0;
  
  private static final int Ed25519 = 1;
  
  private static final int X448 = 2;
  
  private static final int X25519 = 3;
  
  private int algorithm;
  
  private AsymmetricCipherKeyPairGenerator generator;
  
  private boolean initialised;
  
  private SecureRandom secureRandom;
  
  KeyPairGeneratorSpi(int paramInt, AsymmetricCipherKeyPairGenerator paramAsymmetricCipherKeyPairGenerator) {
    this.algorithm = paramInt;
    this.generator = paramAsymmetricCipherKeyPairGenerator;
  }
  
  public void initialize(int paramInt, SecureRandom paramSecureRandom) {
    this.secureRandom = paramSecureRandom;
    try {
      switch (paramInt) {
        case 255:
        case 256:
          switch (this.algorithm) {
            case -1:
            case 1:
              algorithmCheck(1);
              this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
              setupGenerator(1);
              return;
            case -2:
            case 3:
              algorithmCheck(3);
              this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
              setupGenerator(3);
              return;
          } 
          throw new InvalidParameterException("key size not configurable");
        case 448:
          switch (this.algorithm) {
            case -1:
            case 0:
              algorithmCheck(0);
              this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
              setupGenerator(0);
              return;
            case -2:
            case 2:
              algorithmCheck(2);
              this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
              setupGenerator(2);
              return;
          } 
          throw new InvalidParameterException("key size not configurable");
      } 
      throw new InvalidParameterException("unknown key size");
    } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
      throw new InvalidParameterException(invalidAlgorithmParameterException.getMessage());
    } 
  }
  
  public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidAlgorithmParameterException {
    this.secureRandom = paramSecureRandom;
    if (paramAlgorithmParameterSpec instanceof ECGenParameterSpec) {
      initializeGenerator(((ECGenParameterSpec)paramAlgorithmParameterSpec).getName());
    } else if (paramAlgorithmParameterSpec instanceof ECNamedCurveGenParameterSpec) {
      initializeGenerator(((ECNamedCurveGenParameterSpec)paramAlgorithmParameterSpec).getName());
    } else if (paramAlgorithmParameterSpec instanceof EdDSAParameterSpec) {
      initializeGenerator(((EdDSAParameterSpec)paramAlgorithmParameterSpec).getCurveName());
    } else if (paramAlgorithmParameterSpec instanceof XDHParameterSpec) {
      initializeGenerator(((XDHParameterSpec)paramAlgorithmParameterSpec).getCurveName());
    } else {
      String str = ECUtil.getNameFrom(paramAlgorithmParameterSpec);
      if (str != null) {
        initializeGenerator(str);
      } else {
        throw new InvalidAlgorithmParameterException("invalid parameterSpec: " + paramAlgorithmParameterSpec);
      } 
    } 
  }
  
  private void algorithmCheck(int paramInt) throws InvalidAlgorithmParameterException {
    if (this.algorithm != paramInt) {
      if (this.algorithm == 1 || this.algorithm == 0)
        throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type"); 
      if (this.algorithm == -1 && paramInt != 1 && paramInt != 0)
        throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type"); 
      if (this.algorithm == 3 || this.algorithm == 2)
        throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type"); 
      if (this.algorithm == -2 && paramInt != 3 && paramInt != 2)
        throw new InvalidAlgorithmParameterException("parameterSpec for wrong curve type"); 
    } 
  }
  
  private void initializeGenerator(String paramString) throws InvalidAlgorithmParameterException {
    if (paramString.equalsIgnoreCase("Ed448") || paramString.equals(EdECObjectIdentifiers.id_Ed448.getId())) {
      algorithmCheck(0);
      this.generator = (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator();
      setupGenerator(0);
    } else if (paramString.equalsIgnoreCase("Ed25519") || paramString.equals(EdECObjectIdentifiers.id_Ed25519.getId())) {
      algorithmCheck(1);
      this.generator = (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator();
      setupGenerator(1);
    } else if (paramString.equalsIgnoreCase("X448") || paramString.equals(EdECObjectIdentifiers.id_X448.getId())) {
      algorithmCheck(2);
      this.generator = (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator();
      setupGenerator(2);
    } else if (paramString.equalsIgnoreCase("X25519") || paramString.equals(EdECObjectIdentifiers.id_X25519.getId())) {
      algorithmCheck(3);
      this.generator = (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator();
      setupGenerator(3);
    } 
  }
  
  public KeyPair generateKeyPair() {
    if (this.generator == null)
      throw new IllegalStateException("generator not correctly initialized"); 
    if (!this.initialised)
      setupGenerator(this.algorithm); 
    AsymmetricCipherKeyPair asymmetricCipherKeyPair = this.generator.generateKeyPair();
    switch (this.algorithm) {
      case -2:
      case 2:
      case 3:
        return new KeyPair((PublicKey)new BCXDHPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BCXDHPrivateKey(asymmetricCipherKeyPair.getPrivate()));
    } 
    return new KeyPair((PublicKey)new BCEdDSAPublicKey(asymmetricCipherKeyPair.getPublic()), (PrivateKey)new BCEdDSAPrivateKey(asymmetricCipherKeyPair.getPrivate()));
  }
  
  private void setupGenerator(int paramInt) {
    this.initialised = true;
    switch (paramInt) {
      case 0:
        this.generator.init((KeyGenerationParameters)new Ed448KeyGenerationParameters(this.secureRandom));
        break;
      case -1:
      case 1:
        this.generator.init((KeyGenerationParameters)new Ed25519KeyGenerationParameters(this.secureRandom));
        break;
      case 2:
        this.generator.init((KeyGenerationParameters)new X448KeyGenerationParameters(this.secureRandom));
        break;
      case -2:
      case 3:
        this.generator.init((KeyGenerationParameters)new X25519KeyGenerationParameters(this.secureRandom));
        break;
    } 
  }
  
  public static final class Ed25519 extends KeyPairGeneratorSpi {
    public Ed25519() {
      super(1, (AsymmetricCipherKeyPairGenerator)new Ed25519KeyPairGenerator());
    }
  }
  
  public static final class Ed448 extends KeyPairGeneratorSpi {
    public Ed448() {
      super(0, (AsymmetricCipherKeyPairGenerator)new Ed448KeyPairGenerator());
    }
  }
  
  public static final class EdDSA extends KeyPairGeneratorSpi {
    public EdDSA() {
      super(-1, null);
    }
  }
  
  public static final class X25519 extends KeyPairGeneratorSpi {
    public X25519() {
      super(3, (AsymmetricCipherKeyPairGenerator)new X25519KeyPairGenerator());
    }
  }
  
  public static final class X448 extends KeyPairGeneratorSpi {
    public X448() {
      super(2, (AsymmetricCipherKeyPairGenerator)new X448KeyPairGenerator());
    }
  }
  
  public static final class XDH extends KeyPairGeneratorSpi {
    public XDH() {
      super(-2, null);
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyPairGeneratorSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */