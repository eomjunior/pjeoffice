package org.apache.tools.zip;

public interface UnixStat {
  public static final int PERM_MASK = 4095;
  
  public static final int LINK_FLAG = 40960;
  
  public static final int FILE_FLAG = 32768;
  
  public static final int DIR_FLAG = 16384;
  
  public static final int DEFAULT_LINK_PERM = 511;
  
  public static final int DEFAULT_DIR_PERM = 493;
  
  public static final int DEFAULT_FILE_PERM = 420;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnixStat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */