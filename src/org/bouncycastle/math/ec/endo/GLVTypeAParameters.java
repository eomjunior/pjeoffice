package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;

public class GLVTypeAParameters {
  protected final BigInteger i;
  
  protected final BigInteger lambda;
  
  protected final ScalarSplitParameters splitParams;
  
  public GLVTypeAParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, ScalarSplitParameters paramScalarSplitParameters) {
    this.i = paramBigInteger1;
    this.lambda = paramBigInteger2;
    this.splitParams = paramScalarSplitParameters;
  }
  
  public BigInteger getI() {
    return this.i;
  }
  
  public BigInteger getLambda() {
    return this.lambda;
  }
  
  public ScalarSplitParameters getSplitParams() {
    return this.splitParams;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/math/ec/endo/GLVTypeAParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */