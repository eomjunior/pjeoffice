package org.bouncycastle.crypto;

public enum PasswordConverter implements CharToByteConverter {
  ASCII {
    public String getType() {
      return "ASCII";
    }
    
    public byte[] convert(char[] param1ArrayOfchar) {
      return PBEParametersGenerator.PKCS5PasswordToBytes(param1ArrayOfchar);
    }
  },
  UTF8 {
    public String getType() {
      return "UTF8";
    }
    
    public byte[] convert(char[] param1ArrayOfchar) {
      return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(param1ArrayOfchar);
    }
  },
  PKCS12 {
    public String getType() {
      return "PKCS12";
    }
    
    public byte[] convert(char[] param1ArrayOfchar) {
      return PBEParametersGenerator.PKCS12PasswordToBytes(param1ArrayOfchar);
    }
  };
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/crypto/PasswordConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */