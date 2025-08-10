module org.slf4j {
  requires java.base;
  
  exports org.slf4j;
  exports org.slf4j.spi;
  exports org.slf4j.event;
  exports org.slf4j.helpers;
  
  uses org.slf4j.spi.SLF4JServiceProvider;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/module-info.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */