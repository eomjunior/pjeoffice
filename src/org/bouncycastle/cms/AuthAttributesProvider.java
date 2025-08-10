package org.bouncycastle.cms;

import org.bouncycastle.asn1.ASN1Set;

interface AuthAttributesProvider {
  ASN1Set getAuthAttributes();
  
  boolean isAead();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/AuthAttributesProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */