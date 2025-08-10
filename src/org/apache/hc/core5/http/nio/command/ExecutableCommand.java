package org.apache.hc.core5.http.nio.command;

import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.concurrent.CancellableDependency;
import org.apache.hc.core5.reactor.Command;

@Internal
public abstract class ExecutableCommand implements Command {
  public abstract CancellableDependency getCancellableDependency();
  
  public abstract void failed(Exception paramException);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/command/ExecutableCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */