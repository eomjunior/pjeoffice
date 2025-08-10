package org.bouncycastle.asn1.edec;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface EdECObjectIdentifiers {
  public static final ASN1ObjectIdentifier id_edwards_curve_algs = new ASN1ObjectIdentifier("1.3.101");
  
  public static final ASN1ObjectIdentifier id_X25519 = id_edwards_curve_algs.branch("110").intern();
  
  public static final ASN1ObjectIdentifier id_X448 = id_edwards_curve_algs.branch("111").intern();
  
  public static final ASN1ObjectIdentifier id_Ed25519 = id_edwards_curve_algs.branch("112").intern();
  
  public static final ASN1ObjectIdentifier id_Ed448 = id_edwards_curve_algs.branch("113").intern();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/edec/EdECObjectIdentifiers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */