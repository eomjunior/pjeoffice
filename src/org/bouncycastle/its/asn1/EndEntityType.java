package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;

public class EndEntityType extends ASN1Object {
  public static final int app = 128;
  
  public static final int enrol = 64;
  
  private final ASN1BitString type;
  
  public EndEntityType(int paramInt) {
    if (paramInt != 128 && paramInt != 64)
      throw new IllegalArgumentException("value out of range"); 
    this.type = (ASN1BitString)new DERBitString(paramInt);
  }
  
  private EndEntityType(DERBitString paramDERBitString) {
    this.type = (ASN1BitString)paramDERBitString;
  }
  
  public static EndEntityType getInstance(Object paramObject) {
    return (paramObject instanceof EndEntityType) ? (EndEntityType)paramObject : ((paramObject != null) ? new EndEntityType(DERBitString.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.type;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/EndEntityType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */