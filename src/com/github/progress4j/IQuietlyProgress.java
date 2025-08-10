package com.github.progress4j;

public interface IQuietlyProgress extends IProgress {
  void begin(IStage paramIStage);
  
  void begin(IStage paramIStage, int paramInt);
  
  void begin(String paramString);
  
  void begin(String paramString, int paramInt);
  
  void step(String paramString, Object... paramVarArgs);
  
  void skip(long paramLong);
  
  void info(String paramString, Object... paramVarArgs);
  
  void end();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IQuietlyProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */