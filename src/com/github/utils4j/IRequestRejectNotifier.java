package com.github.utils4j;

import com.sun.net.httpserver.HttpExchange;

public interface IRequestRejectNotifier {
  void notifyReject(HttpExchange paramHttpExchange, String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IRequestRejectNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */