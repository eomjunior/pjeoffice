package org.bouncycastle.asn1.bc;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;

public class ObjectStoreIntegrityCheck extends ASN1Object implements ASN1Choice {
  public static final int PBKD_MAC_CHECK = 0;
  
  public static final int SIG_CHECK = 1;
  
  private final int type;
  
  private final ASN1Object integrityCheck;
  
  public ObjectStoreIntegrityCheck(PbkdMacIntegrityCheck paramPbkdMacIntegrityCheck) {
    this((ASN1Encodable)paramPbkdMacIntegrityCheck);
  }
  
  public ObjectStoreIntegrityCheck(SignatureCheck paramSignatureCheck) {
    this((ASN1Encodable)new DERTaggedObject(0, (ASN1Encodable)paramSignatureCheck));
  }
  
  private ObjectStoreIntegrityCheck(ASN1Encodable paramASN1Encodable) {
    if (paramASN1Encodable instanceof org.bouncycastle.asn1.ASN1Sequence || paramASN1Encodable instanceof PbkdMacIntegrityCheck) {
      this.type = 0;
      this.integrityCheck = PbkdMacIntegrityCheck.getInstance(paramASN1Encodable);
    } else if (paramASN1Encodable instanceof ASN1TaggedObject) {
      this.type = 1;
      this.integrityCheck = SignatureCheck.getInstance(((ASN1TaggedObject)paramASN1Encodable).getObject());
    } else {
      throw new IllegalArgumentException("Unknown check object in integrity check.");
    } 
  }
  
  public static ObjectStoreIntegrityCheck getInstance(Object paramObject) {
    if (paramObject instanceof ObjectStoreIntegrityCheck)
      return (ObjectStoreIntegrityCheck)paramObject; 
    if (paramObject instanceof byte[])
      try {
        return new ObjectStoreIntegrityCheck((ASN1Encodable)ASN1Primitive.fromByteArray((byte[])paramObject));
      } catch (IOException iOException) {
        throw new IllegalArgumentException("Unable to parse integrity check details.");
      }  
    return (paramObject != null) ? new ObjectStoreIntegrityCheck((ASN1Encodable)paramObject) : null;
  }
  
  public int getType() {
    return this.type;
  }
  
  public ASN1Object getIntegrityCheck() {
    return this.integrityCheck;
  }
  
  public ASN1Primitive toASN1Primitive() {
    return (ASN1Primitive)((this.integrityCheck instanceof SignatureCheck) ? new DERTaggedObject(0, (ASN1Encodable)this.integrityCheck) : this.integrityCheck.toASN1Primitive());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/asn1/bc/ObjectStoreIntegrityCheck.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */