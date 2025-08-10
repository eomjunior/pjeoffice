package com.github.utils4j;

import com.github.utils4j.imp.function.IProvider;
import org.apache.hc.client5.http.classic.methods.HttpGet;

public interface IGetCodec {
  void get(IProvider<HttpGet> paramIProvider, IDownloadStatus paramIDownloadStatus) throws Exception;
  
  String get(IProvider<HttpGet> paramIProvider) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IGetCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */