package org.bouncycastle.jcajce.provider.config;

import java.security.spec.DSAParameterSpec;
import java.util.Map;
import java.util.Set;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;

public interface ProviderConfiguration {
  ECParameterSpec getEcImplicitlyCa();
  
  DHParameterSpec getDHDefaultParameters(int paramInt);
  
  DSAParameterSpec getDSADefaultParameters(int paramInt);
  
  Set getAcceptableNamedCurves();
  
  Map getAdditionalECParameters();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/config/ProviderConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */