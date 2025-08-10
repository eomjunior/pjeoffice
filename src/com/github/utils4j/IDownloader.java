package com.github.utils4j;

import io.reactivex.Observable;
import java.io.IOException;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;

public interface IDownloader {
  Observable<HttpUriRequest> newRequest();
  
  String match(String paramString);
  
  void download(String paramString, IDownloadStatus paramIDownloadStatus) throws IOException, InterruptedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IDownloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */