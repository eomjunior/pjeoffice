package com.github.videohandler4j;

public interface IVideoSliceEvent extends IVideoInfoEvent {
  long getStartTime();
  
  long getTotalTime();
  
  String getStartTimeString();
  
  String getTotalTimeString();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/IVideoSliceEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */