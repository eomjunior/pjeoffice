package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.pqc.crypto.lms.LMOtsParameters;
import org.bouncycastle.pqc.crypto.lms.LMSigParameters;

public class LMSKeyGenParameterSpec implements AlgorithmParameterSpec {
  private final LMSigParameters lmSigParams;
  
  private final LMOtsParameters lmOtsParameters;
  
  public LMSKeyGenParameterSpec(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters) {
    this.lmSigParams = paramLMSigParameters;
    this.lmOtsParameters = paramLMOtsParameters;
  }
  
  public LMSigParameters getSigParams() {
    return this.lmSigParams;
  }
  
  public LMOtsParameters getOtsParams() {
    return this.lmOtsParameters;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/jcajce/spec/LMSKeyGenParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */