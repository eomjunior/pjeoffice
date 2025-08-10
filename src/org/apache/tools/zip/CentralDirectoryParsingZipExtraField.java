package org.apache.tools.zip;

import java.util.zip.ZipException;

public interface CentralDirectoryParsingZipExtraField extends ZipExtraField {
  void parseFromCentralDirectoryData(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws ZipException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/CentralDirectoryParsingZipExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */