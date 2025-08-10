package org.zeroturnaround.zip.extra;

import java.util.zip.ZipException;

public interface ZipExtraField {
  ZipShort getHeaderId();
  
  ZipShort getLocalFileDataLength();
  
  ZipShort getCentralDirectoryLength();
  
  byte[] getLocalFileDataData();
  
  byte[] getCentralDirectoryData();
  
  void parseFromLocalFileData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/ZipExtraField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */