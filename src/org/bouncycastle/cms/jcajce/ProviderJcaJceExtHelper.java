package org.bouncycastle.cms.jcajce;

import java.security.PrivateKey;
import java.security.Provider;
import javax.crypto.SecretKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.util.ProviderJcaJceHelper;
import org.bouncycastle.operator.SymmetricKeyUnwrapper;
import org.bouncycastle.operator.jcajce.JceAsymmetricKeyUnwrapper;
import org.bouncycastle.operator.jcajce.JceKTSKeyUnwrapper;
import org.bouncycastle.operator.jcajce.JceSymmetricKeyUnwrapper;

class ProviderJcaJceExtHelper extends ProviderJcaJceHelper implements JcaJceExtHelper {
  public ProviderJcaJceExtHelper(Provider paramProvider) {
    super(paramProvider);
  }
  
  public JceAsymmetricKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier, PrivateKey paramPrivateKey) {
    paramPrivateKey = CMSUtils.cleanPrivateKey(paramPrivateKey);
    return (new JceAsymmetricKeyUnwrapper(paramAlgorithmIdentifier, paramPrivateKey)).setProvider(this.provider);
  }
  
  public JceKTSKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier, PrivateKey paramPrivateKey, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    paramPrivateKey = CMSUtils.cleanPrivateKey(paramPrivateKey);
    return (new JceKTSKeyUnwrapper(paramAlgorithmIdentifier, paramPrivateKey, paramArrayOfbyte1, paramArrayOfbyte2)).setProvider(this.provider);
  }
  
  public SymmetricKeyUnwrapper createSymmetricUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier, SecretKey paramSecretKey) {
    return (SymmetricKeyUnwrapper)(new JceSymmetricKeyUnwrapper(paramAlgorithmIdentifier, paramSecretKey)).setProvider(this.provider);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/jcajce/ProviderJcaJceExtHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */