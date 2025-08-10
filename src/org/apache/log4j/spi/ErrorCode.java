package org.apache.log4j.spi;

public interface ErrorCode {
  public static final int GENERIC_FAILURE = 0;
  
  public static final int WRITE_FAILURE = 1;
  
  public static final int FLUSH_FAILURE = 2;
  
  public static final int CLOSE_FAILURE = 3;
  
  public static final int FILE_OPEN_FAILURE = 4;
  
  public static final int MISSING_LAYOUT = 5;
  
  public static final int ADDRESS_PARSE_FAILURE = 6;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/ErrorCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */