package com.github.utils4j;

public interface IUploadStatus {
  void onStartUpload(long paramLong) throws InterruptedException;
  
  void onEndUpload() throws InterruptedException;
  
  void onStatus(long paramLong1, long paramLong2) throws InterruptedException;
  
  void onUploadFail(Throwable paramThrowable) throws InterruptedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IUploadStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */