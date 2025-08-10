package org.bouncycastle.its.asn1;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;

public class PsidGroupPermissions extends ASN1Object {
  private final SubjectPermissions subjectPermissions;
  
  private final BigInteger minChainLength;
  
  private final BigInteger chainLengthRange;
  
  private final Object eeType;
  
  private PsidGroupPermissions(ASN1Sequence paramASN1Sequence) {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("sequence not length 2"); 
    this.subjectPermissions = SubjectPermissions.getInstance(paramASN1Sequence.getObjectAt(0));
    this.minChainLength = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(1)).getValue();
    this.chainLengthRange = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(2)).getValue();
    this.eeType = EndEntityType.getInstance(paramASN1Sequence.getObjectAt(3));
  }
  
  public static PsidGroupPermissions getInstance(Object paramObject) {
    return (paramObject instanceof PsidGroupPermissions) ? (PsidGroupPermissions)paramObject : ((paramObject != null) ? new PsidGroupPermissions(ASN1Sequence.getInstance(paramObject)) : null);
  }
  
  public ASN1Primitive toASN1Primitive() {
    return null;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/PsidGroupPermissions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */