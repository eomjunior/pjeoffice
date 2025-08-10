package org.bouncycastle.est;

public interface TLSUniqueProvider {
  boolean isTLSUniqueAvailable();
  
  byte[] getTLSUnique();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/est/TLSUniqueProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */