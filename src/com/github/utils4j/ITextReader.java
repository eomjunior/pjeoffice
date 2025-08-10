package com.github.utils4j;

import java.io.IOException;

@FunctionalInterface
public interface ITextReader {
  <T> T read(String paramString) throws IOException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/ITextReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */