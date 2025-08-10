package org.bouncycastle.jce.interfaces;

import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;

public interface GOST3410Params {
  String getPublicKeyParamSetOID();
  
  String getDigestParamSetOID();
  
  String getEncryptionParamSetOID();
  
  GOST3410PublicKeyParameterSetSpec getPublicKeyParameters();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jce/interfaces/GOST3410Params.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */