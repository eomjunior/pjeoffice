package br.jus.cnj.pje.office.core.imp;

import br.jus.cnj.pje.office.core.IPjeRequestHandler;
import com.sun.net.httpserver.Filter;
import java.util.concurrent.ExecutorService;

interface IPjeWebServerSetup {
  int getPort();
  
  ExecutorService getExecutor();
  
  Filter[] getFilters();
  
  IPjeRequestHandler[] getHandlers();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/IPjeWebServerSetup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */