package com.itextpdf.text.log;

public interface Counter {
  Counter getCounter(Class<?> paramClass);
  
  void read(long paramLong);
  
  void written(long paramLong);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/log/Counter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */