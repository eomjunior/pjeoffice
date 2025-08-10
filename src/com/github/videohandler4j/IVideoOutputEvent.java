package com.github.videohandler4j;

import com.github.filehandler4j.IFileOutputEvent;

public interface IVideoOutputEvent extends IFileOutputEvent, IVideoInfoEvent {
  long getTotalTime();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/IVideoOutputEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */