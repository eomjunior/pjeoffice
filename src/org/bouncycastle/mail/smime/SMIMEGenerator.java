package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.CMSEnvelopedGenerator;
import org.bouncycastle.util.Strings;

public class SMIMEGenerator {
  private static Map BASE_CIPHER_NAMES = new HashMap<Object, Object>();
  
  protected boolean useBase64 = true;
  
  protected String encoding = "base64";
  
  public void setContentTransferEncoding(String paramString) {
    this.encoding = paramString;
    this.useBase64 = Strings.toLowerCase(paramString).equals("base64");
  }
  
  protected MimeBodyPart makeContentBodyPart(MimeBodyPart paramMimeBodyPart) throws SMIMEException {
    try {
      MimeMessage mimeMessage = new MimeMessage((Session)null) {
          protected void updateMessageID() throws MessagingException {}
        };
      Enumeration<Header> enumeration = paramMimeBodyPart.getAllHeaders();
      mimeMessage.setDataHandler(paramMimeBodyPart.getDataHandler());
      while (enumeration.hasMoreElements()) {
        Header header = enumeration.nextElement();
        mimeMessage.setHeader(header.getName(), header.getValue());
      } 
      mimeMessage.saveChanges();
      enumeration = mimeMessage.getAllHeaders();
      while (enumeration.hasMoreElements()) {
        Header header = enumeration.nextElement();
        if (Strings.toLowerCase(header.getName()).startsWith("content-"))
          paramMimeBodyPart.setHeader(header.getName(), header.getValue()); 
      } 
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception saving message state.", messagingException);
    } 
    return paramMimeBodyPart;
  }
  
  protected MimeBodyPart makeContentBodyPart(MimeMessage paramMimeMessage) throws SMIMEException {
    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    try {
      paramMimeMessage.removeHeader("Message-Id");
      paramMimeMessage.removeHeader("Mime-Version");
      try {
        if (paramMimeMessage.getContent() instanceof javax.mail.Multipart) {
          mimeBodyPart.setContent(paramMimeMessage.getRawInputStream(), paramMimeMessage.getContentType());
          extractHeaders(mimeBodyPart, paramMimeMessage);
          return mimeBodyPart;
        } 
      } catch (MessagingException messagingException) {}
      mimeBodyPart.setContent(paramMimeMessage.getContent(), paramMimeMessage.getContentType());
      mimeBodyPart.setDataHandler(paramMimeMessage.getDataHandler());
      extractHeaders(mimeBodyPart, paramMimeMessage);
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception saving message state.", messagingException);
    } catch (IOException iOException) {
      throw new SMIMEException("exception getting message content.", iOException);
    } 
    return mimeBodyPart;
  }
  
  private void extractHeaders(MimeBodyPart paramMimeBodyPart, MimeMessage paramMimeMessage) throws MessagingException {
    Enumeration<Header> enumeration = paramMimeMessage.getAllHeaders();
    while (enumeration.hasMoreElements()) {
      Header header = enumeration.nextElement();
      paramMimeBodyPart.addHeader(header.getName(), header.getValue());
    } 
  }
  
  protected KeyGenerator createSymmetricKeyGenerator(String paramString, Provider paramProvider) throws NoSuchAlgorithmException {
    try {
      return createKeyGenerator(paramString, paramProvider);
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      try {
        String str = (String)BASE_CIPHER_NAMES.get(paramString);
        if (str != null)
          return createKeyGenerator(str, paramProvider); 
      } catch (NoSuchAlgorithmException noSuchAlgorithmException1) {}
      if (paramProvider != null)
        return createSymmetricKeyGenerator(paramString, null); 
      throw noSuchAlgorithmException;
    } 
  }
  
  private KeyGenerator createKeyGenerator(String paramString, Provider paramProvider) throws NoSuchAlgorithmException {
    return (paramProvider != null) ? KeyGenerator.getInstance(paramString, paramProvider) : KeyGenerator.getInstance(paramString);
  }
  
  static {
    BASE_CIPHER_NAMES.put(CMSEnvelopedGenerator.DES_EDE3_CBC, "DESEDE");
    BASE_CIPHER_NAMES.put(CMSEnvelopedGenerator.AES128_CBC, "AES");
    BASE_CIPHER_NAMES.put(CMSEnvelopedGenerator.AES192_CBC, "AES");
    BASE_CIPHER_NAMES.put(CMSEnvelopedGenerator.AES256_CBC, "AES");
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMEGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */