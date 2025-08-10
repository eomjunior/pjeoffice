package br.jus.cnj.pje.office.core;

public interface IPjeLifeCycleHook {
  default void onShutdown() {}
  
  default void onStartup() {}
  
  default void onKill() {}
  
  default void onFailStart(Exception e) {}
  
  default void onFailShutdown(Exception e) {}
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeLifeCycleHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */