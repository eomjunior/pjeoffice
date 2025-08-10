package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class SPHINCS256KeyGenParameterSpec implements AlgorithmParameterSpec {
  public static final String SHA512_256 = "SHA512-256";
  
  public static final String SHA3_256 = "SHA3-256";
  
  private final String treeHash;
  
  public SPHINCS256KeyGenParameterSpec() {
    this("SHA512-256");
  }
  
  public SPHINCS256KeyGenParameterSpec(String paramString) {
    this.treeHash = paramString;
  }
  
  public String getTreeDigest() {
    return this.treeHash;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/jcajce/spec/SPHINCS256KeyGenParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */