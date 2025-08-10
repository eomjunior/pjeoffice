package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.util.Arrays;

public class LinkageValue extends ASN1Object {
  private final byte[] value;
  
  private LinkageValue(ASN1OctetString paramASN1OctetString) {
    this.value = Arrays.clone(Utils.octetStringFixed(paramASN1OctetString.getOctets(), 9));
  }
  
  public static LinkageValue getInstance(Object paramObject) {
    return (paramObject instanceof LinkageValue) ? (LinkageValue)paramObject : ((paramObject != null) ? new LinkageValue(ASN1OctetString.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)new DEROctetString(Arrays.clone(this.value));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/LinkageValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */