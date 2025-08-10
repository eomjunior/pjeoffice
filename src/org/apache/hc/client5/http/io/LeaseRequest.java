package org.apache.hc.client5.http.io;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.apache.hc.core5.concurrent.Cancellable;
import org.apache.hc.core5.util.Timeout;

public interface LeaseRequest extends Cancellable {
  ConnectionEndpoint get(Timeout paramTimeout) throws InterruptedException, ExecutionException, TimeoutException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/io/LeaseRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */