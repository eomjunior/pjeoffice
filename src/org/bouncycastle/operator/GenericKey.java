package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class GenericKey {
  private AlgorithmIdentifier algorithmIdentifier = null;
  
  private Object representation;
  
  public GenericKey(Object paramObject) {
    this.representation = paramObject;
  }
  
  public GenericKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
    this.representation = paramArrayOfbyte;
  }
  
  protected GenericKey(AlgorithmIdentifier paramAlgorithmIdentifier, Object paramObject) {
    this.representation = paramObject;
  }
  
  public AlgorithmIdentifier getAlgorithmIdentifier() {
    return this.algorithmIdentifier;
  }
  
  public Object getRepresentation() {
    return this.representation;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/operator/GenericKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */