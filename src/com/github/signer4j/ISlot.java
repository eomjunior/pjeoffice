package com.github.signer4j;

public interface ISlot extends ISerialItem {
  long getNumber();
  
  IToken getToken();
  
  IDevice toDevice();
  
  String getDescription();
  
  String getHardwareVersion();
  
  String getFirmewareVersion();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ISlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */