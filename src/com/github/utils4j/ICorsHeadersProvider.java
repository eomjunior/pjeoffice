package com.github.utils4j;

import java.util.Optional;

public interface ICorsHeadersProvider {
  Optional<String> getAccessControlMaxAgeHeader();
  
  Optional<String> getAccessControlAllowMethods();
  
  Optional<String> getAccessControlAllowHeaders();
  
  Optional<String> getAccessControlAllowCredentials();
  
  Optional<String> getAccessControlAllowPrivateNetwork();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ICorsHeadersProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */