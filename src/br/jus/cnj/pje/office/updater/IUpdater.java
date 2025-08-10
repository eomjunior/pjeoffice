package br.jus.cnj.pje.office.updater;

public interface IUpdater {
  boolean isDisabled();
  
  void update(IUpdateStatus paramIUpdateStatus, boolean paramBoolean);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/IUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */