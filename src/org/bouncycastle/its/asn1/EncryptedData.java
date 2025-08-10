package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Sequence;

public class EncryptedData {
  private EncryptedData(ASN1Sequence paramASN1Sequence) {}
  
  public static EncryptedData getInstance(Object paramObject) {
    return (paramObject instanceof EncryptedData) ? (EncryptedData)paramObject : ((paramObject != null) ? new EncryptedData(ASN1Sequence.getInstance(paramObject)) : null);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/EncryptedData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */