package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class LMSHSSParameterSpec implements AlgorithmParameterSpec {
  private final LMSParameterSpec[] specs;
  
  public LMSHSSParameterSpec(LMSParameterSpec[] paramArrayOfLMSParameterSpec) {
    this.specs = (LMSParameterSpec[])paramArrayOfLMSParameterSpec.clone();
  }
  
  public LMSParameterSpec[] getLMSSpecs() {
    return (LMSParameterSpec[])this.specs.clone();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/jcajce/spec/LMSHSSParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */