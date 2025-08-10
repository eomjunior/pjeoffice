package org.apache.hc.client5.http.auth;

public interface CredentialsStore extends CredentialsProvider {
  void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials);
  
  void clear();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/CredentialsStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */