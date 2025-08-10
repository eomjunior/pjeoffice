package org.apache.hc.client5.http.impl.auth;

public interface NTLMEngine {
  String generateType1Msg(String paramString1, String paramString2) throws NTLMEngineException;
  
  String generateType3Msg(String paramString1, char[] paramArrayOfchar, String paramString2, String paramString3, String paramString4) throws NTLMEngineException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/NTLMEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */