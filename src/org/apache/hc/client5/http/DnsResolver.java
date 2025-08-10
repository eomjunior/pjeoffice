package org.apache.hc.client5.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.STATELESS)
public interface DnsResolver {
  InetAddress[] resolve(String paramString) throws UnknownHostException;
  
  String resolveCanonicalHostname(String paramString) throws UnknownHostException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/DnsResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */