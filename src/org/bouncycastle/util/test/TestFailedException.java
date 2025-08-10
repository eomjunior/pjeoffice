package org.bouncycastle.util.test;

public class TestFailedException extends RuntimeException {
  private TestResult _result;
  
  public TestFailedException(TestResult paramTestResult) {
    this._result = paramTestResult;
  }
  
  public TestResult getResult() {
    return this._result;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/util/test/TestFailedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */