package org.bouncycastle.x509;

import java.util.ArrayList;
import java.util.Collection;

public class X509CollectionStoreParameters implements X509StoreParameters {
  private Collection collection;
  
  public X509CollectionStoreParameters(Collection paramCollection) {
    if (paramCollection == null)
      throw new NullPointerException("collection cannot be null"); 
    this.collection = paramCollection;
  }
  
  public Object clone() {
    return new X509CollectionStoreParameters(this.collection);
  }
  
  public Collection getCollection() {
    return new ArrayList(this.collection);
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append("X509CollectionStoreParameters: [\n");
    stringBuffer.append("  collection: " + this.collection + "\n");
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/x509/X509CollectionStoreParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */