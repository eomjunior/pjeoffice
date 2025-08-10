package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERIA5String;

public class SPuri {
  private DERIA5String uri;
  
  public static SPuri getInstance(Object paramObject) {
    return (paramObject instanceof SPuri) ? (SPuri)paramObject : ((paramObject instanceof DERIA5String) ? new SPuri(DERIA5String.getInstance(paramObject)) : null);
  }
  
  public SPuri(DERIA5String paramDERIA5String) {
    this.uri = paramDERIA5String;
  }
  
  public DERIA5String getUri() {
    return this.uri;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return this.uri.toASN1Primitive();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/esf/SPuri.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */