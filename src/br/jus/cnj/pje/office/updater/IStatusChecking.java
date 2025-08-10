package br.jus.cnj.pje.office.updater;

import br.jus.cnj.pje.office.updater.imp.VersionStatus;

public interface IStatusChecking {
  VersionStatus getStatus();
  
  String getPatcherHash();
  
  boolean isAcceptable();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/IStatusChecking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */