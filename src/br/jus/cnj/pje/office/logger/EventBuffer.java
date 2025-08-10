package br.jus.cnj.pje.office.logger;

public interface EventBuffer {
  public static final int DEFAULT_EVENTS_WINDOW_SIZE = 50;
  
  void add(String paramString);
  
  void setMaxSize(int paramInt);
  
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/logger/EventBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */