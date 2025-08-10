package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.DERIA5String;

public class VerisignCzagExtension extends DERIA5String {
  public VerisignCzagExtension(DERIA5String paramDERIA5String) {
    super(paramDERIA5String.getString());
  }
  
  public String toString() {
    return "VerisignCzagExtension: " + getString();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/misc/VerisignCzagExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */