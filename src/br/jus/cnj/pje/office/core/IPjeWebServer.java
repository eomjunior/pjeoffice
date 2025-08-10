package br.jus.cnj.pje.office.core;

public interface IPjeWebServer extends IPjeEndpointPaths {
  public static final int HTTP_PORT = 8800;
  
  public static final int HTTPS_PORT = 8801;
  
  public static final String LOCALHOST = "127.0.0.1";
  
  public static final String HTTP_ENDPOINT = "http://127.0.0.1:8800";
  
  public static final String HTTPS_ENDPOINT = "https://127.0.0.1:8801";
  
  public static final String HTTP_DEVMODE_ENDPOINT = "http://127.0.0.1:8800/pjeOffice/welcome?file=index.html&page=devguide/devguide.html";
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeWebServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */