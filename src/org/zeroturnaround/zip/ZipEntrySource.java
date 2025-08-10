package org.zeroturnaround.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public interface ZipEntrySource {
  String getPath();
  
  ZipEntry getEntry();
  
  InputStream getInputStream() throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipEntrySource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */