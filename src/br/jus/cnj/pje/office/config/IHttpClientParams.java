package br.jus.cnj.pje.office.config;

import org.apache.hc.core5.util.Timeout;

public interface IHttpClientParams {
  String http_client_user_agent();
  
  int http_client_connections_maxTotal();
  
  int http_client_connections_maxPerRoute();
  
  Timeout http_client_connectTimeout();
  
  Timeout http_client_responseTimeout();
  
  Timeout http_client_connectionRequestTimeout();
  
  Timeout http_client_connectionKeepAliveTimeout();
  
  Timeout http_client_evictIdleConnectionTimeout();
  
  Timeout http_client_validateAfterInactivityTimeout();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/config/IHttpClientParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */