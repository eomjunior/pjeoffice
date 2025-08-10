package br.jus.cnj.pje.office.core;

import br.jus.cnj.pje.office.core.imp.PjeAccessTime;

public interface IPjePermissionAccessor {
  PjeAccessTime tryAccess(IPjeServerAccess paramIPjeServerAccess);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjePermissionAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */