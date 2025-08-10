package org.apache.hc.client5.http.auth;

import java.security.Principal;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.protocol.HttpContext;

public interface AuthScheme {
  String getName();
  
  boolean isConnectionBased();
  
  void processChallenge(AuthChallenge paramAuthChallenge, HttpContext paramHttpContext) throws MalformedChallengeException;
  
  boolean isChallengeComplete();
  
  String getRealm();
  
  boolean isResponseReady(HttpHost paramHttpHost, CredentialsProvider paramCredentialsProvider, HttpContext paramHttpContext) throws AuthenticationException;
  
  Principal getPrincipal();
  
  String generateAuthResponse(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws AuthenticationException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */