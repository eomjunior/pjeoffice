package com.github.utils4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public interface IDownloadStatus {
  OutputStream onNewTry() throws IOException;
  
  void onStartDownload(long paramLong) throws InterruptedException;
  
  void onEndDownload() throws InterruptedException;
  
  void onStatus(long paramLong1, long paramLong2) throws InterruptedException;
  
  void onDownloadFail(Throwable paramThrowable);
  
  Optional<File> getDownloadedFile();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IDownloadStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */