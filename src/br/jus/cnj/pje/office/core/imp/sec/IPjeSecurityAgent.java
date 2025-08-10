package br.jus.cnj.pje.office.core.imp.sec;

import br.jus.cnj.pje.office.core.IPjeSecurityGrantor;
import br.jus.cnj.pje.office.core.IPjeServerAccess;
import java.util.List;

interface IPjeSecurityAgent extends IPjeSecurityGrantor {
  void refresh();
  
  boolean isDevMode();
  
  boolean setDevMode(boolean paramBoolean);
  
  List<IPjeServerAccess> getServers();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/sec/IPjeSecurityAgent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */