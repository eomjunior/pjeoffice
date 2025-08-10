package com.github.signer4j.imp;

import com.github.signer4j.IDevice;
import com.github.signer4j.ISlot;
import com.github.signer4j.IToken;
import java.util.List;
import java.util.Optional;

interface IDriver extends ILoadCycle {
  String getId();
  
  List<ISlot> getSlots();
  
  List<IDevice> getDevices();
  
  boolean isLibraryAware();
  
  Optional<IToken> firstToken();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/IDriver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */