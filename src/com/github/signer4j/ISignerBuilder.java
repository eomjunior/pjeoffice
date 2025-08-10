package com.github.signer4j;

public interface ISignerBuilder extends IByteProcessorBuilder<ISignerBuilder, ISimpleSigner> {
  ISignerBuilder usingAlgorithm(ISignatureAlgorithm paramISignatureAlgorithm);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ISignerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */