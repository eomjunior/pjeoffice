package com.github.videohandler4j;

import com.github.filehandler4j.IFileSlice;
import com.github.utils4j.IHasDuration;

public interface IVideoSlice extends IFileSlice {
  long getTime();
  
  long getTime(IHasDuration paramIHasDuration);
  
  long end(IHasDuration paramIHasDuration);
  
  String startString();
  
  String endString();
  
  String timeString();
  
  String outputFileName();
  
  String outputFileName(IHasDuration paramIHasDuration);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/IVideoSlice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */