package org.bouncycastle.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.util.Arrays;

public class UserKeyingMaterialSpec implements AlgorithmParameterSpec {
  private final byte[] userKeyingMaterial;
  
  public UserKeyingMaterialSpec(byte[] paramArrayOfbyte) {
    this.userKeyingMaterial = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getUserKeyingMaterial() {
    return Arrays.clone(this.userKeyingMaterial);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/spec/UserKeyingMaterialSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */