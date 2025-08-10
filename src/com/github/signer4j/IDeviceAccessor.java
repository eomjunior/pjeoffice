package com.github.signer4j;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IDeviceAccessor {
  List<IDevice> getDevices();
  
  List<IDevice> getDevices(boolean paramBoolean);
  
  List<IDevice> getDevices(Predicate<IDevice> paramPredicate);
  
  List<IDevice> getDevices(Predicate<IDevice> paramPredicate, boolean paramBoolean);
  
  boolean isLoaded();
  
  Optional<IDevice> firstDevice();
  
  Optional<IDevice> firstDevice(boolean paramBoolean);
  
  Optional<IDevice> firstDevice(Predicate<IDevice> paramPredicate);
  
  Optional<IDevice> firstDevice(Predicate<IDevice> paramPredicate, boolean paramBoolean);
  
  void close();
  
  default void setStrategy(IDriverLookupStrategy strategy) {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IDeviceAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */