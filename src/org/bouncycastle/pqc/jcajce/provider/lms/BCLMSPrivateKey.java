package org.bouncycastle.pqc.jcajce.provider.lms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.lms.LMSKeyParameters;
import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.util.PrivateKeyFactory;
import org.bouncycastle.pqc.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.pqc.jcajce.interfaces.LMSPrivateKey;
import org.bouncycastle.util.Arrays;

public class BCLMSPrivateKey implements PrivateKey, LMSPrivateKey {
  private static final long serialVersionUID = 8568701712864512338L;
  
  private transient LMSKeyParameters keyParams;
  
  private transient ASN1Set attributes;
  
  public BCLMSPrivateKey(LMSKeyParameters paramLMSKeyParameters) {
    this.keyParams = paramLMSKeyParameters;
  }
  
  public BCLMSPrivateKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
    init(paramPrivateKeyInfo);
  }
  
  private void init(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
    this.attributes = paramPrivateKeyInfo.getAttributes();
    this.keyParams = (LMSKeyParameters)PrivateKeyFactory.createKey(paramPrivateKeyInfo);
  }
  
  public long getIndex() {
    if (getUsagesRemaining() == 0L)
      throw new IllegalStateException("key exhausted"); 
    return (this.keyParams instanceof LMSPrivateKeyParameters) ? ((LMSPrivateKeyParameters)this.keyParams).getIndex() : ((HSSPrivateKeyParameters)this.keyParams).getIndex();
  }
  
  public long getUsagesRemaining() {
    return (this.keyParams instanceof LMSPrivateKeyParameters) ? ((LMSPrivateKeyParameters)this.keyParams).getUsagesRemaining() : ((HSSPrivateKeyParameters)this.keyParams).getUsagesRemaining();
  }
  
  public LMSPrivateKey extractKeyShard(int paramInt) {
    return (this.keyParams instanceof LMSPrivateKeyParameters) ? new BCLMSPrivateKey((LMSKeyParameters)((LMSPrivateKeyParameters)this.keyParams).extractKeyShard(paramInt)) : new BCLMSPrivateKey((LMSKeyParameters)((HSSPrivateKeyParameters)this.keyParams).extractKeyShard(paramInt));
  }
  
  public String getAlgorithm() {
    return "LMS";
  }
  
  public String getFormat() {
    return "PKCS#8";
  }
  
  public byte[] getEncoded() {
    try {
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo((AsymmetricKeyParameter)this.keyParams, this.attributes);
      return privateKeyInfo.getEncoded();
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject instanceof BCLMSPrivateKey) {
      BCLMSPrivateKey bCLMSPrivateKey = (BCLMSPrivateKey)paramObject;
      try {
        return Arrays.areEqual(this.keyParams.getEncoded(), bCLMSPrivateKey.keyParams.getEncoded());
      } catch (IOException iOException) {
        throw new IllegalStateException("unable to perform equals");
      } 
    } 
    return false;
  }
  
  public int hashCode() {
    try {
      return Arrays.hashCode(this.keyParams.getEncoded());
    } catch (IOException iOException) {
      throw new IllegalStateException("unable to calculate hashCode");
    } 
  }
  
  CipherParameters getKeyParams() {
    return (CipherParameters)this.keyParams;
  }
  
  public int getLevels() {
    return (this.keyParams instanceof LMSPrivateKeyParameters) ? 1 : ((HSSPrivateKeyParameters)this.keyParams).getL();
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException {
    paramObjectInputStream.defaultReadObject();
    byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
    init(PrivateKeyInfo.getInstance(arrayOfByte));
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
    paramObjectOutputStream.defaultWriteObject();
    paramObjectOutputStream.writeObject(getEncoded());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/jcajce/provider/lms/BCLMSPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */