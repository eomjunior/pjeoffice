package org.bouncycastle.jcajce.provider.asymmetric.ec;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

public class GMSignatureSpi extends SignatureSpi {
  private final JcaJceHelper helper = (JcaJceHelper)new BCJcaJceHelper();
  
  private AlgorithmParameters engineParams;
  
  private SM2ParameterSpec paramSpec;
  
  private final SM2Signer signer;
  
  GMSignatureSpi(SM2Signer paramSM2Signer) {
    this.signer = paramSM2Signer;
  }
  
  protected void engineInitVerify(PublicKey paramPublicKey) throws InvalidKeyException {
    ParametersWithID parametersWithID;
    AsymmetricKeyParameter asymmetricKeyParameter = ECUtils.generatePublicKeyParameter(paramPublicKey);
    if (this.paramSpec != null)
      parametersWithID = new ParametersWithID((CipherParameters)asymmetricKeyParameter, this.paramSpec.getID()); 
    this.signer.init(false, (CipherParameters)parametersWithID);
  }
  
  protected void engineInitSign(PrivateKey paramPrivateKey) throws InvalidKeyException {
    ParametersWithRandom parametersWithRandom;
    AsymmetricKeyParameter asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(paramPrivateKey);
    if (this.appRandom != null)
      parametersWithRandom = new ParametersWithRandom((CipherParameters)asymmetricKeyParameter, this.appRandom); 
    if (this.paramSpec != null) {
      this.signer.init(true, (CipherParameters)new ParametersWithID((CipherParameters)parametersWithRandom, this.paramSpec.getID()));
    } else {
      this.signer.init(true, (CipherParameters)parametersWithRandom);
    } 
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
      throw new SignatureException("unable to create signature: " + cryptoException.getMessage());
    } 
  }
  
  protected boolean engineVerify(byte[] paramArrayOfbyte) throws SignatureException {
    return this.signer.verifySignature(paramArrayOfbyte);
  }
  
  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
    if (paramAlgorithmParameterSpec instanceof SM2ParameterSpec) {
      this.paramSpec = (SM2ParameterSpec)paramAlgorithmParameterSpec;
    } else {
      throw new InvalidAlgorithmParameterException("only SM2ParameterSpec supported");
    } 
  }
  
  protected AlgorithmParameters engineGetParameters() {
    if (this.engineParams == null && this.paramSpec != null)
      try {
        this.engineParams = this.helper.createAlgorithmParameters("PSS");
        this.engineParams.init((AlgorithmParameterSpec)this.paramSpec);
      } catch (Exception exception) {
        throw new RuntimeException(exception.toString());
      }  
    return this.engineParams;
  }
  
  protected void engineSetParameter(String paramString, Object paramObject) {
    throw new UnsupportedOperationException("engineSetParameter unsupported");
  }
  
  protected Object engineGetParameter(String paramString) {
    throw new UnsupportedOperationException("engineGetParameter unsupported");
  }
  
  public static class sha256WithSM2 extends GMSignatureSpi {
    public sha256WithSM2() {
      super(new SM2Signer((Digest)new SHA256Digest()));
    }
  }
  
  public static class sm3WithSM2 extends GMSignatureSpi {
    public sm3WithSM2() {
      super(new SM2Signer());
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/ec/GMSignatureSpi.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */