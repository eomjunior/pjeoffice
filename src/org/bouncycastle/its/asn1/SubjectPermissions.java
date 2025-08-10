package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;

public class SubjectPermissions extends ASN1Object implements ASN1Choice {
  public static SubjectPermissions getInstance(Object paramObject) {
    return (SubjectPermissions)((paramObject instanceof SubjectPermissions) ? paramObject : ((paramObject != null) ? null : null));
  }
  
  public ASN1Primitive toASN1Primitive() {
    return null;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/SubjectPermissions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */