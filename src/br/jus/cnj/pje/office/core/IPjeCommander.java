package br.jus.cnj.pje.office.core;

public interface IPjeCommander<I extends IPjeRequest, O extends IPjeResponse> extends IPjeLifeCycle {
  void execute(String paramString);
  
  void execute(I paramI, O paramO);
  
  String getServerEndpoint();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeCommander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */