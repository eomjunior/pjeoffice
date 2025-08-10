package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;

public class HashedData extends ASN1Object implements ASN1Choice {
  private ASN1OctetString hashData;
  
  public HashedData(byte[] paramArrayOfbyte) {
    this.hashData = (ASN1OctetString)new DEROctetString(paramArrayOfbyte);
  }
  
  private HashedData(ASN1OctetString paramASN1OctetString) {
    this.hashData = paramASN1OctetString;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)this.hashData;
  }
  
  public ASN1OctetString getHashData() {
    return this.hashData;
  }
  
  public void setHashData(ASN1OctetString paramASN1OctetString) {
    this.hashData = paramASN1OctetString;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/HashedData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */