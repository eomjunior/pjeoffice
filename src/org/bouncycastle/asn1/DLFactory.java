package org.bouncycastle.asn1;

class DLFactory {
  static final ASN1Sequence EMPTY_SEQUENCE = new DLSequence();
  
  static final ASN1Set EMPTY_SET = new DLSet();
  
  static ASN1Sequence createSequence(ASN1EncodableVector paramASN1EncodableVector) {
    return (paramASN1EncodableVector.size() < 1) ? EMPTY_SEQUENCE : new DLSequence(paramASN1EncodableVector);
  }
  
  static ASN1Set createSet(ASN1EncodableVector paramASN1EncodableVector) {
    return (paramASN1EncodableVector.size() < 1) ? EMPTY_SET : new DLSet(paramASN1EncodableVector);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/DLFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */