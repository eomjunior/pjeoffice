package com.github.signer4j;

public interface ICMSSignerBuilder extends IFileSignerBuilder<ICMSSignerBuilder>, IByteProcessorBuilder<ICMSSignerBuilder, ICMSSigner> {
  ICMSSignerBuilder usingMemoryLimit(long paramLong);
  
  ICMSSignerBuilder usingConfig(ICMSConfigSetup paramICMSConfigSetup);
  
  ICMSSignerBuilder usingAttributes(boolean paramBoolean);
  
  ICMSSignerBuilder usingProvider(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICMSSignerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */