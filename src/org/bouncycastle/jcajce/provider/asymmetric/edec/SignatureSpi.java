package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.crypto.signers.Ed448Signer;

public class SignatureSpi extends SignatureSpi {
  private static final byte[] EMPTY_CONTEXT = new byte[0];
  
  private final String algorithm;
  
  private Signer signer;
  
  SignatureSpi(String paramString) {
    this.algorithm = paramString;
  }
  
  protected void engineInitVerify(PublicKey paramPublicKey) throws InvalidKeyException {
    if (paramPublicKey instanceof BCEdDSAPublicKey) {
      AsymmetricKeyParameter asymmetricKeyParameter = ((BCEdDSAPublicKey)paramPublicKey).engineGetKeyParameters();
      if (asymmetricKeyParameter instanceof org.bouncycastle.crypto.params.Ed448PublicKeyParameters) {
        this.signer = getSigner("Ed448");
      } else {
        this.signer = getSigner("Ed25519");
      } 
      this.signer.init(false, (CipherParameters)asymmetricKeyParameter);
    } else {
      throw new InvalidKeyException("cannot identify EdDSA public key");
    } 
  }
  
  protected void engineInitSign(PrivateKey paramPrivateKey) throws InvalidKeyException {
    if (paramPrivateKey instanceof BCEdDSAPrivateKey) {
      AsymmetricKeyParameter asymmetricKeyParameter = ((BCEdDSAPrivateKey)paramPrivateKey).engineGetKeyParameters();
      if (asymmetricKeyParameter instanceof org.bouncycastle.crypto.params.Ed448PrivateKeyParameters) {
        this.signer = getSigner("Ed448");
      } else {
        this.signer = getSigner("Ed25519");
      } 
      this.signer.init(true, (CipherParameters)asymmetricKeyParameter);
    } else {
      throw new InvalidKeyException("cannot identify EdDSA private key");
    } 
  }
  
  private Signer getSigner(String paramString) throws InvalidKeyException {
    if (this.algorithm != null && !paramString.equals(this.algorithm))
      throw new InvalidKeyException("inappropriate key for " + this.algorithm); 
    return (Signer)(paramString.equals("Ed448") ? new Ed448Signer(EMPTY_CONTEXT) : new Ed25519Signer());
  }
  
  protected void engineUpdate(byte paramByte) throws SignatureException {
    this.signer.update(paramByte);
  }
  
  protected void engineUpdate(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws SignatureException {
    this.signer.update(paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  protected byte[] engineSign() throws SignatureException {
    try {
      return this.signer.generateSignature();
    } catch (CryptoException cryptoException) {
      throw new SignatureException(cryptoException.getMessage());
    } 
  }
  
  protected boolean engineVerify(byte[] paramArrayOfbyte) throws SignatureException {
    return this.signer.verifySignature(paramArrayOfbyte);
  }
  
  protected void engineSetParameter(String paramString, Object paramObject) throws InvalidParameterException {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }
  
  protected Object engineGetParameter(String paramString) throws InvalidParameterException {
    throw new UnsupportedOperationException("engineGetParameter unsupported");
  }
  
  protected AlgorithmParameters engineGetParameters() {
    return null;
  }
  
  public static final class Ed25519 extends SignatureSpi {
    public Ed25519() {
      super("Ed25519");
    }
  }
  
  public static final class Ed448 extends SignatureSpi {
    public Ed448() {
      super("Ed448");
    }
  }
  
  public static final class EdDSA extends SignatureSpi {
    public EdDSA() {
      super(null);
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/edec/SignatureSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */