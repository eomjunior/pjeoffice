package com.github.videohandler4j;

import java.io.File;
import java.util.function.Consumer;
import javax.swing.JPanel;

public interface IVideoSliceView extends IVideoSlice {
  IVideoSliceView setOnDoSelect(Consumer<IVideoSliceView> paramConsumer);
  
  IVideoSliceView setOnClosed(Consumer<IVideoSliceView> paramConsumer);
  
  IVideoSliceView setOnPlay(Consumer<IVideoSliceView> paramConsumer);
  
  IVideoSliceView setOnStop(Consumer<IVideoSliceView> paramConsumer);
  
  IVideoSliceView setOnSave(Consumer<IVideoSliceView> paramConsumer);
  
  IVideoSliceView setOnSelected(Consumer<IVideoSliceView> paramConsumer);
  
  JPanel asPanel();
  
  void setEnd(long paramLong);
  
  void splitAndSave(File paramFile1, File paramFile2);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/videohandler4j/IVideoSliceView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */