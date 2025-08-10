package org.bouncycastle.pqc.crypto.lms;

public class LMSParameters {
  private final LMSigParameters lmSigParam;
  
  private final LMOtsParameters lmOTSParam;
  
  public LMSParameters(LMSigParameters paramLMSigParameters, LMOtsParameters paramLMOtsParameters) {
    this.lmSigParam = paramLMSigParameters;
    this.lmOTSParam = paramLMOtsParameters;
  }
  
  public LMSigParameters getLMSigParam() {
    return this.lmSigParam;
  }
  
  public LMOtsParameters getLMOTSParam() {
    return this.lmOTSParam;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */