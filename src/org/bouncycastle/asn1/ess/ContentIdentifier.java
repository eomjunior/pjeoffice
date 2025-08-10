package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;

public class ContentIdentifier extends ASN1Object {
  ASN1OctetString value;
  
  public static ContentIdentifier getInstance(Object paramObject) {
    return (paramObject instanceof ContentIdentifier) ? (ContentIdentifier)paramObject : ((paramObject != null) ? new ContentIdentifier(ASN1OctetString.getInstance(paramObject)) : null);
  }
  
  private ContentIdentifier(ASN1OctetString paramASN1OctetString) {
    this.value = paramASN1OctetString;
  }
  
  public ContentIdentifier(byte[] paramArrayOfbyte) {
    this((ASN1OctetString)new DEROctetString(paramArrayOfbyte));
  }
  
  public ASN1OctetString getValue() {
    return this.value;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.value;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/ess/ContentIdentifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */