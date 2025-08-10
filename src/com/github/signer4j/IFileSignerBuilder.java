package com.github.signer4j;

public interface IFileSignerBuilder<T extends IFileSignerBuilder<T>> {
  T usingSignatureAlgorithm(ISignatureAlgorithm paramISignatureAlgorithm);
  
  T usingSignatureType(ISignatureType paramISignatureType);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IFileSignerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */