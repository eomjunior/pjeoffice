package com.github.pdfhandler4j.imp;

interface ICloseablePdfReader extends AutoCloseable {
  public static final long LARGE_PDF_SIZE = 52428800L;
  
  public static final long SMALL_PDF_SIZE = 6553600L;
  
  int getNumberOfPages();
  
  void freeReader(Object paramObject) throws Exception;
  
  void addDocument(Object paramObject) throws Exception;
  
  void addPage(Object paramObject, int paramInt) throws Exception;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/ICloseablePdfReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */