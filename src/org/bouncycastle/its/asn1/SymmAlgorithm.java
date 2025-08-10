package org.bouncycastle.its.asn1;

import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;

public class SymmAlgorithm extends ASN1Object {
  public static SymmAlgorithm aes128Ccm = new SymmAlgorithm(new ASN1Enumerated(0));
  
  private ASN1Enumerated symmAlgorithm;
  
  private SymmAlgorithm(ASN1Enumerated paramASN1Enumerated) {
    this.symmAlgorithm = paramASN1Enumerated;
  }
  
  public SymmAlgorithm(int paramInt) {
    this.symmAlgorithm = new ASN1Enumerated(paramInt);
  }
  
  public SymmAlgorithm getInstance(Object paramObject) {
    return (paramObject == null) ? null : ((paramObject instanceof SymmAlgorithm) ? (SymmAlgorithm)paramObject : new SymmAlgorithm(ASN1Enumerated.getInstance(paramObject)));
  }
  
  public ASN1Enumerated getSymmAlgorithm() {
    return this.symmAlgorithm;
  }
  
  public void setSymmAlgorithm(ASN1Enumerated paramASN1Enumerated) {
    this.symmAlgorithm = paramASN1Enumerated;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return this.symmAlgorithm.toASN1Primitive();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/its/asn1/SymmAlgorithm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */