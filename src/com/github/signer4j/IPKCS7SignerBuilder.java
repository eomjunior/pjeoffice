package com.github.signer4j;

public interface IPKCS7SignerBuilder extends IFileSignerBuilder<IPKCS7SignerBuilder>, IByteProcessorBuilder<IPKCS7SignerBuilder, IPKCS7Signer> {
  IPKCS7SignerBuilder usingEmailAddress(String... paramVarArgs);
  
  IPKCS7SignerBuilder usingUnstructuredName(String... paramVarArgs);
  
  IPKCS7SignerBuilder usingChallengePassword(String paramString);
  
  IPKCS7SignerBuilder usingUnstructuredAddress(String... paramVarArgs);
  
  IPKCS7SignerBuilder usingSignatureTimestamp(byte[] paramArrayOfbyte);
  
  IPKCS7Signer build();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IPKCS7SignerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */