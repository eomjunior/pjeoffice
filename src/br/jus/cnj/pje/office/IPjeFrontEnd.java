package br.jus.cnj.pje.office;

import br.jus.cnj.pje.office.core.IPjeOffice;
import java.awt.PopupMenu;

public interface IPjeFrontEnd {
  String getTitle();
  
  IPjeFrontEnd fallback();
  
  IPjeFrontEnd show(long paramLong);
  
  IPjeFrontEnd install(IPjeOffice paramIPjeOffice, PopupMenu paramPopupMenu) throws Exception;
  
  void uninstall();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/IPjeFrontEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */