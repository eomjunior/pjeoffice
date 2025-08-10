package org.apache.hc.client5.http.auth;

import java.security.Principal;

public interface Credentials {
  Principal getUserPrincipal();
  
  char[] getPassword();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/Credentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */