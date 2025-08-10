package org.bouncycastle.pqc.crypto.lms;

import java.io.IOException;
import org.bouncycastle.util.Encodable;

class LMSSignedPubKey implements Encodable {
  private final LMSSignature signature;
  
  private final LMSPublicKeyParameters publicKey;
  
  public LMSSignedPubKey(LMSSignature paramLMSSignature, LMSPublicKeyParameters paramLMSPublicKeyParameters) {
    this.signature = paramLMSSignature;
    this.publicKey = paramLMSPublicKeyParameters;
  }
  
  public LMSSignature getSignature() {
    return this.signature;
  }
  
  public LMSPublicKeyParameters getPublicKey() {
    return this.publicKey;
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    LMSSignedPubKey lMSSignedPubKey = (LMSSignedPubKey)paramObject;
    return ((this.signature != null) ? !this.signature.equals(lMSSignedPubKey.signature) : (lMSSignedPubKey.signature != null)) ? false : ((this.publicKey != null) ? this.publicKey.equals(lMSSignedPubKey.publicKey) : ((lMSSignedPubKey.publicKey == null)));
  }
  
  public int hashCode() {
    null = (this.signature != null) ? this.signature.hashCode() : 0;
    return 31 * null + ((this.publicKey != null) ? this.publicKey.hashCode() : 0);
  }
  
  public byte[] getEncoded() throws IOException {
    return Composer.compose().bytes(this.signature.getEncoded()).bytes(this.publicKey.getEncoded()).build();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/lms/LMSSignedPubKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */