package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class SPHINCSKeyParameters extends AsymmetricKeyParameter {
  public static final String SHA512_256 = "SHA-512/256";
  
  public static final String SHA3_256 = "SHA3-256";
  
  private final String treeDigest;
  
  protected SPHINCSKeyParameters(boolean paramBoolean, String paramString) {
    super(paramBoolean);
    this.treeDigest = paramString;
  }
  
  public String getTreeDigest() {
    return this.treeDigest;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/sphincs/SPHINCSKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */