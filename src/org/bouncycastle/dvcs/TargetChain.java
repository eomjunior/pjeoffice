package org.bouncycastle.dvcs;

import org.bouncycastle.asn1.dvcs.TargetEtcChain;

public class TargetChain {
  private final TargetEtcChain certs;
  
  public TargetChain(TargetEtcChain paramTargetEtcChain) {
    this.certs = paramTargetEtcChain;
  }
  
  public TargetEtcChain toASN1Structure() {
    return this.certs;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/dvcs/TargetChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */