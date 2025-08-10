package org.bouncycastle.mail.smime.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Enumeration;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

public class ExampleUtils {
  public static void dumpContent(MimeBodyPart paramMimeBodyPart, String paramString) throws MessagingException, IOException {
    System.out.println("content type: " + paramMimeBodyPart.getContentType());
    FileOutputStream fileOutputStream = new FileOutputStream(paramString);
    InputStream inputStream = paramMimeBodyPart.getInputStream();
    byte[] arrayOfByte = new byte[10000];
    int i;
    while ((i = inputStream.read(arrayOfByte, 0, arrayOfByte.length)) > 0)
      fileOutputStream.write(arrayOfByte, 0, i); 
    fileOutputStream.close();
  }
  
  public static String findKeyAlias(KeyStore paramKeyStore, String paramString, char[] paramArrayOfchar) throws Exception {
    paramKeyStore.load(new FileInputStream(paramString), paramArrayOfchar);
    Enumeration<String> enumeration = paramKeyStore.aliases();
    String str = null;
    while (enumeration.hasMoreElements()) {
      String str1 = enumeration.nextElement();
      if (paramKeyStore.isKeyEntry(str1))
        str = str1; 
    } 
    if (str == null)
      throw new IllegalArgumentException("can't find a private key in keyStore: " + paramString); 
    return str;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ExampleUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */