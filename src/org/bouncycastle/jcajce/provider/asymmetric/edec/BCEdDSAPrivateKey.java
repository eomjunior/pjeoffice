package org.bouncycastle.jcajce.provider.asymmetric.edec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.Ed448PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed448PublicKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.jcajce.interfaces.EdDSAPrivateKey;
import org.bouncycastle.jcajce.interfaces.EdDSAPublicKey;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Properties;

public class BCEdDSAPrivateKey implements EdDSAPrivateKey {
  static final long serialVersionUID = 1L;
  
  transient AsymmetricKeyParameter eddsaPrivateKey;
  
  private final boolean hasPublicKey = true;
  
  private final byte[] attributes = null;
  
  BCEdDSAPrivateKey(AsymmetricKeyParameter paramAsymmetricKeyParameter) {
    this.eddsaPrivateKey = paramAsymmetricKeyParameter;
  }
  
  BCEdDSAPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
    populateFromPrivateKeyInfo(paramPrivateKeyInfo);
  }
  
  private void populateFromPrivateKeyInfo(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
    ASN1Encodable aSN1Encodable = paramPrivateKeyInfo.parsePrivateKey();
    if (EdECObjectIdentifiers.id_Ed448.equals((ASN1Primitive)paramPrivateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm())) {
      this.eddsaPrivateKey = (AsymmetricKeyParameter)new Ed448PrivateKeyParameters(ASN1OctetString.getInstance(aSN1Encodable).getOctets(), 0);
    } else {
      this.eddsaPrivateKey = (AsymmetricKeyParameter)new Ed25519PrivateKeyParameters(ASN1OctetString.getInstance(aSN1Encodable).getOctets(), 0);
    } 
  }
  
  public String getAlgorithm() {
    return (this.eddsaPrivateKey instanceof Ed448PrivateKeyParameters) ? "Ed448" : "Ed25519";
  }
  
  public String getFormat() {
    return "PKCS#8";
  }
  
  public byte[] getEncoded() {
    try {
      ASN1Set aSN1Set = ASN1Set.getInstance(this.attributes);
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(this.eddsaPrivateKey, aSN1Set);
      return (this.hasPublicKey && !Properties.isOverrideSet("org.bouncycastle.pkcs8.v1_info_only")) ? privateKeyInfo.getEncoded() : (new PrivateKeyInfo(privateKeyInfo.getPrivateKeyAlgorithm(), privateKeyInfo.parsePrivateKey(), aSN1Set)).getEncoded();
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  public EdDSAPublicKey getPublicKey() {
    return (this.eddsaPrivateKey instanceof Ed448PrivateKeyParameters) ? new BCEdDSAPublicKey((AsymmetricKeyParameter)((Ed448PrivateKeyParameters)this.eddsaPrivateKey).generatePublicKey()) : new BCEdDSAPublicKey((AsymmetricKeyParameter)((Ed25519PrivateKeyParameters)this.eddsaPrivateKey).generatePublicKey());
  }
  
  AsymmetricKeyParameter engineGetKeyParameters() {
    return this.eddsaPrivateKey;
  }
  
  public String toString() {
    Ed25519PublicKeyParameters ed25519PublicKeyParameters;
    if (this.eddsaPrivateKey instanceof Ed448PrivateKeyParameters) {
      Ed448PublicKeyParameters ed448PublicKeyParameters = ((Ed448PrivateKeyParameters)this.eddsaPrivateKey).generatePublicKey();
    } else {
      ed25519PublicKeyParameters = ((Ed25519PrivateKeyParameters)this.eddsaPrivateKey).generatePublicKey();
    } 
    return Utils.keyToString("Private Key", getAlgorithm(), (AsymmetricKeyParameter)ed25519PublicKeyParameters);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof PrivateKey))
      return false; 
    PrivateKey privateKey = (PrivateKey)paramObject;
    return Arrays.areEqual(privateKey.getEncoded(), getEncoded());
  }
  
  public int hashCode() {
    return Arrays.hashCode(getEncoded());
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
    populateFromPrivateKeyInfo(PrivateKeyInfo.getInstance(arrayOfByte));
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(getEncoded());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/edec/BCEdDSAPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */