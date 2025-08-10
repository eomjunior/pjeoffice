package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.FixedPointCombMultiplier;

public class SM2KeyExchangePrivateParameters implements CipherParameters {
  private final boolean initiator;
  
  private final ECPrivateKeyParameters staticPrivateKey;
  
  private final ECPoint staticPublicPoint;
  
  private final ECPrivateKeyParameters ephemeralPrivateKey;
  
  private final ECPoint ephemeralPublicPoint;
  
  public SM2KeyExchangePrivateParameters(boolean paramBoolean, ECPrivateKeyParameters paramECPrivateKeyParameters1, ECPrivateKeyParameters paramECPrivateKeyParameters2) {
    if (paramECPrivateKeyParameters1 == null)
      throw new NullPointerException("staticPrivateKey cannot be null"); 
    if (paramECPrivateKeyParameters2 == null)
      throw new NullPointerException("ephemeralPrivateKey cannot be null"); 
    ECDomainParameters eCDomainParameters = paramECPrivateKeyParameters1.getParameters();
    if (!eCDomainParameters.equals(paramECPrivateKeyParameters2.getParameters()))
      throw new IllegalArgumentException("Static and ephemeral private keys have different domain parameters"); 
    FixedPointCombMultiplier fixedPointCombMultiplier = new FixedPointCombMultiplier();
    this.initiator = paramBoolean;
    this.staticPrivateKey = paramECPrivateKeyParameters1;
    this.staticPublicPoint = fixedPointCombMultiplier.multiply(eCDomainParameters.getG(), paramECPrivateKeyParameters1.getD()).normalize();
    this.ephemeralPrivateKey = paramECPrivateKeyParameters2;
    this.ephemeralPublicPoint = fixedPointCombMultiplier.multiply(eCDomainParameters.getG(), paramECPrivateKeyParameters2.getD()).normalize();
  }
  
  public boolean isInitiator() {
    return this.initiator;
  }
  
  public ECPrivateKeyParameters getStaticPrivateKey() {
    return this.staticPrivateKey;
  }
  
  public ECPoint getStaticPublicPoint() {
    return this.staticPublicPoint;
  }
  
  public ECPrivateKeyParameters getEphemeralPrivateKey() {
    return this.ephemeralPrivateKey;
  }
  
  public ECPoint getEphemeralPublicPoint() {
    return this.ephemeralPublicPoint;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/params/SM2KeyExchangePrivateParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */