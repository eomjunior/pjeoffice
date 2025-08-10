package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class DHMQVPublicParameters implements CipherParameters {
  private DHPublicKeyParameters staticPublicKey;
  
  private DHPublicKeyParameters ephemeralPublicKey;
  
  public DHMQVPublicParameters(DHPublicKeyParameters paramDHPublicKeyParameters1, DHPublicKeyParameters paramDHPublicKeyParameters2) {
    if (paramDHPublicKeyParameters1 == null)
      throw new NullPointerException("staticPublicKey cannot be null"); 
    if (paramDHPublicKeyParameters2 == null)
      throw new NullPointerException("ephemeralPublicKey cannot be null"); 
    if (!paramDHPublicKeyParameters1.getParameters().equals(paramDHPublicKeyParameters2.getParameters()))
      throw new IllegalArgumentException("Static and ephemeral public keys have different domain parameters"); 
    this.staticPublicKey = paramDHPublicKeyParameters1;
    this.ephemeralPublicKey = paramDHPublicKeyParameters2;
  }
  
  public DHPublicKeyParameters getStaticPublicKey() {
    return this.staticPublicKey;
  }
  
  public DHPublicKeyParameters getEphemeralPublicKey() {
    return this.ephemeralPublicKey;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/DHMQVPublicParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */