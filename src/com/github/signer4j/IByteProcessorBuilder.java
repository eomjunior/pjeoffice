package com.github.signer4j;

import java.util.concurrent.locks.ReentrantLock;

public interface IByteProcessorBuilder<B extends IByteProcessorBuilder<B, T>, T extends IByteProcessor> {
  B usingLock(ReentrantLock paramReentrantLock);
  
  T build();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IByteProcessorBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */