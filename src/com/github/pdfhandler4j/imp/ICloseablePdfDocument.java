package com.github.pdfhandler4j.imp;

import java.io.Closeable;

interface ICloseablePdfDocument extends Closeable {
  void addPage(ICloseablePdfReader paramICloseablePdfReader, int paramInt) throws Exception;
  
  void addDocument(ICloseablePdfReader paramICloseablePdfReader) throws Exception;
  
  void freeReader(ICloseablePdfReader paramICloseablePdfReader) throws Exception;
  
  long getCurrentDocumentSize();
  
  void close();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ICloseablePdfDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */