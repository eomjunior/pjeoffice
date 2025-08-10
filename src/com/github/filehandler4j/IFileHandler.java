package com.github.filehandler4j;

import io.reactivex.Observable;
import java.util.function.Function;

public interface IFileHandler<T extends IFileInfoEvent> extends Function<IInputDescriptor, Observable<T>> {
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/IFileHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */