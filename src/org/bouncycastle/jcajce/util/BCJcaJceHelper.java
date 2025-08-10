package org.bouncycastle.jcajce.util;

import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BCJcaJceHelper extends ProviderJcaJceHelper {
  private static volatile Provider bcProvider;
  
  private static synchronized Provider getBouncyCastleProvider() {
    Provider provider = Security.getProvider("BC");
    if (provider instanceof BouncyCastleProvider)
      return provider; 
    if (bcProvider != null)
      return bcProvider; 
    bcProvider = (Provider)new BouncyCastleProvider();
    return bcProvider;
  }
  
  public BCJcaJceHelper() {
    super(getBouncyCastleProvider());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/util/BCJcaJceHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */