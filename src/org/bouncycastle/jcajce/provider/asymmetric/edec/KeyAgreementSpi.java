package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.RawAgreement;
import org.bouncycastle.crypto.agreement.X25519Agreement;
import org.bouncycastle.crypto.agreement.X448Agreement;
import org.bouncycastle.crypto.agreement.XDHUnifiedAgreement;
import org.bouncycastle.crypto.agreement.kdf.ConcatenationKDFGenerator;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.XDHUPrivateParameters;
import org.bouncycastle.crypto.params.XDHUPublicParameters;
import org.bouncycastle.crypto.util.DigestFactory;
import org.bouncycastle.jcajce.provider.asymmetric.util.BaseAgreementSpi;
import org.bouncycastle.jcajce.spec.DHUParameterSpec;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;

public class KeyAgreementSpi extends BaseAgreementSpi {
  private RawAgreement agreement;
  
  private DHUParameterSpec dhuSpec;
  
  private byte[] result;
  
  KeyAgreementSpi(String paramString) {
    super(paramString, null);
  }
  
  KeyAgreementSpi(String paramString, DerivationFunction paramDerivationFunction) {
    super(paramString, paramDerivationFunction);
  }
  
  protected byte[] calcSecret() {
    return this.result;
  }
  
  protected void engineInit(Key paramKey, SecureRandom paramSecureRandom) throws InvalidKeyException {
    if (paramKey instanceof BCXDHPrivateKey) {
      AsymmetricKeyParameter asymmetricKeyParameter = ((BCXDHPrivateKey)paramKey).engineGetKeyParameters();
      if (asymmetricKeyParameter instanceof org.bouncycastle.crypto.params.X448PrivateKeyParameters) {
        this.agreement = getAgreement("X448");
      } else {
        this.agreement = getAgreement("X25519");
      } 
      this.agreement.init((CipherParameters)asymmetricKeyParameter);
    } else {
      throw new InvalidKeyException("cannot identify XDH private key");
    } 
    if (this.kdf != null) {
      this.ukmParameters = new byte[0];
    } else {
      this.ukmParameters = null;
    } 
  }
  
  protected void engineInit(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
    AsymmetricKeyParameter asymmetricKeyParameter;
    if (paramKey instanceof BCXDHPrivateKey) {
      asymmetricKeyParameter = ((BCXDHPrivateKey)paramKey).engineGetKeyParameters();
      if (asymmetricKeyParameter instanceof org.bouncycastle.crypto.params.X448PrivateKeyParameters) {
        this.agreement = getAgreement("X448");
      } else {
        this.agreement = getAgreement("X25519");
      } 
    } else {
      throw new InvalidKeyException("cannot identify XDH private key");
    } 
    this.ukmParameters = null;
    if (paramAlgorithmParameterSpec instanceof DHUParameterSpec) {
      if (this.kaAlgorithm.indexOf('U') < 0)
        throw new InvalidAlgorithmParameterException("agreement algorithm not DHU based"); 
      this.dhuSpec = (DHUParameterSpec)paramAlgorithmParameterSpec;
      this.ukmParameters = this.dhuSpec.getUserKeyingMaterial();
      this.agreement.init((CipherParameters)new XDHUPrivateParameters(asymmetricKeyParameter, ((BCXDHPrivateKey)this.dhuSpec.getEphemeralPrivateKey()).engineGetKeyParameters(), ((BCXDHPublicKey)this.dhuSpec.getEphemeralPublicKey()).engineGetKeyParameters()));
    } else {
      this.agreement.init((CipherParameters)asymmetricKeyParameter);
      if (paramAlgorithmParameterSpec instanceof UserKeyingMaterialSpec) {
        if (this.kdf == null)
          throw new InvalidAlgorithmParameterException("no KDF specified for UserKeyingMaterialSpec"); 
        this.ukmParameters = ((UserKeyingMaterialSpec)paramAlgorithmParameterSpec).getUserKeyingMaterial();
      } else {
        throw new InvalidAlgorithmParameterException("unknown ParameterSpec");
      } 
    } 
    if (this.kdf != null && this.ukmParameters == null)
      this.ukmParameters = new byte[0]; 
  }
  
  protected Key engineDoPhase(Key paramKey, boolean paramBoolean) throws InvalidKeyException, IllegalStateException {
    if (this.agreement == null)
      throw new IllegalStateException(this.kaAlgorithm + " not initialised."); 
    if (!paramBoolean)
      throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties."); 
    if (!(paramKey instanceof BCXDHPublicKey))
      throw new InvalidKeyException("cannot identify XDH private key"); 
    AsymmetricKeyParameter asymmetricKeyParameter = ((BCXDHPublicKey)paramKey).engineGetKeyParameters();
    this.result = new byte[this.agreement.getAgreementSize()];
    if (this.dhuSpec != null) {
      this.agreement.calculateAgreement((CipherParameters)new XDHUPublicParameters(asymmetricKeyParameter, ((BCXDHPublicKey)this.dhuSpec.getOtherPartyEphemeralKey()).engineGetKeyParameters()), this.result, 0);
    } else {
      this.agreement.calculateAgreement((CipherParameters)asymmetricKeyParameter, this.result, 0);
    } 
    return null;
  }
  
  private RawAgreement getAgreement(String paramString) throws InvalidKeyException {
    if (!this.kaAlgorithm.equals("XDH") && !this.kaAlgorithm.startsWith(paramString))
      throw new InvalidKeyException("inappropriate key for " + this.kaAlgorithm); 
    return (RawAgreement)((this.kaAlgorithm.indexOf('U') > 0) ? (paramString.startsWith("X448") ? new XDHUnifiedAgreement((RawAgreement)new X448Agreement()) : new XDHUnifiedAgreement((RawAgreement)new X25519Agreement())) : (paramString.startsWith("X448") ? new X448Agreement() : new X25519Agreement()));
  }
  
  public static final class X25519 extends KeyAgreementSpi {
    public X25519() {
      super("X25519");
    }
  }
  
  public static class X25519UwithSHA256CKDF extends KeyAgreementSpi {
    public X25519UwithSHA256CKDF() {
      super("X25519UwithSHA256CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA256()));
    }
  }
  
  public static class X25519UwithSHA256KDF extends KeyAgreementSpi {
    public X25519UwithSHA256KDF() {
      super("X25519UwithSHA256KDF", (DerivationFunction)new KDF2BytesGenerator(DigestFactory.createSHA256()));
    }
  }
  
  public static final class X25519withSHA256CKDF extends KeyAgreementSpi {
    public X25519withSHA256CKDF() {
      super("X25519withSHA256CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA256()));
    }
  }
  
  public static final class X25519withSHA256KDF extends KeyAgreementSpi {
    public X25519withSHA256KDF() {
      super("X25519withSHA256KDF", (DerivationFunction)new KDF2BytesGenerator(DigestFactory.createSHA256()));
    }
  }
  
  public static class X25519withSHA384CKDF extends KeyAgreementSpi {
    public X25519withSHA384CKDF() {
      super("X25519withSHA384CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA384()));
    }
  }
  
  public static class X25519withSHA512CKDF extends KeyAgreementSpi {
    public X25519withSHA512CKDF() {
      super("X25519withSHA512CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA512()));
    }
  }
  
  public static final class X448 extends KeyAgreementSpi {
    public X448() {
      super("X448");
    }
  }
  
  public static class X448UwithSHA512CKDF extends KeyAgreementSpi {
    public X448UwithSHA512CKDF() {
      super("X448UwithSHA512CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA512()));
    }
  }
  
  public static class X448UwithSHA512KDF extends KeyAgreementSpi {
    public X448UwithSHA512KDF() {
      super("X448UwithSHA512KDF", (DerivationFunction)new KDF2BytesGenerator(DigestFactory.createSHA512()));
    }
  }
  
  public static final class X448withSHA256CKDF extends KeyAgreementSpi {
    public X448withSHA256CKDF() {
      super("X448withSHA256CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA256()));
    }
  }
  
  public static class X448withSHA384CKDF extends KeyAgreementSpi {
    public X448withSHA384CKDF() {
      super("X448withSHA384CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA384()));
    }
  }
  
  public static final class X448withSHA512CKDF extends KeyAgreementSpi {
    public X448withSHA512CKDF() {
      super("X448withSHA512CKDF", (DerivationFunction)new ConcatenationKDFGenerator(DigestFactory.createSHA512()));
    }
  }
  
  public static final class X448withSHA512KDF extends KeyAgreementSpi {
    public X448withSHA512KDF() {
      super("X448withSHA512KDF", (DerivationFunction)new KDF2BytesGenerator(DigestFactory.createSHA512()));
    }
  }
  
  public static final class XDH extends KeyAgreementSpi {
    public XDH() {
      super("XDH");
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/edec/KeyAgreementSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */