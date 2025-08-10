package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSEnvelopedDataStreamGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.RecipientInfoGenerator;
import org.bouncycastle.operator.OutputEncryptor;

public class SMIMEEnvelopedGenerator extends SMIMEGenerator {
  public static final String DES_EDE3_CBC = CMSEnvelopedDataGenerator.DES_EDE3_CBC;
  
  public static final String RC2_CBC = CMSEnvelopedDataGenerator.RC2_CBC;
  
  public static final String IDEA_CBC = "1.3.6.1.4.1.188.7.1.1.2";
  
  public static final String CAST5_CBC = "1.2.840.113533.7.66.10";
  
  public static final String AES128_CBC = CMSEnvelopedDataGenerator.AES128_CBC;
  
  public static final String AES192_CBC = CMSEnvelopedDataGenerator.AES192_CBC;
  
  public static final String AES256_CBC = CMSEnvelopedDataGenerator.AES256_CBC;
  
  public static final String CAMELLIA128_CBC = CMSEnvelopedDataGenerator.CAMELLIA128_CBC;
  
  public static final String CAMELLIA192_CBC = CMSEnvelopedDataGenerator.CAMELLIA192_CBC;
  
  public static final String CAMELLIA256_CBC = CMSEnvelopedDataGenerator.CAMELLIA256_CBC;
  
  public static final String SEED_CBC = CMSEnvelopedDataGenerator.SEED_CBC;
  
  public static final String DES_EDE3_WRAP = CMSEnvelopedDataGenerator.DES_EDE3_WRAP;
  
  public static final String AES128_WRAP = CMSEnvelopedDataGenerator.AES128_WRAP;
  
  public static final String AES256_WRAP = CMSEnvelopedDataGenerator.AES256_WRAP;
  
  public static final String CAMELLIA128_WRAP = CMSEnvelopedDataGenerator.CAMELLIA128_WRAP;
  
  public static final String CAMELLIA192_WRAP = CMSEnvelopedDataGenerator.CAMELLIA192_WRAP;
  
  public static final String CAMELLIA256_WRAP = CMSEnvelopedDataGenerator.CAMELLIA256_WRAP;
  
  public static final String SEED_WRAP = CMSEnvelopedDataGenerator.SEED_WRAP;
  
  public static final String ECDH_SHA1KDF = CMSEnvelopedDataGenerator.ECDH_SHA1KDF;
  
  private static final String ENCRYPTED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data";
  
  private EnvelopedGenerator fact = new EnvelopedGenerator();
  
  public void addRecipientInfoGenerator(RecipientInfoGenerator paramRecipientInfoGenerator) throws IllegalArgumentException {
    this.fact.addRecipientInfoGenerator(paramRecipientInfoGenerator);
  }
  
  public void setBerEncodeRecipients(boolean paramBoolean) {
    this.fact.setBEREncodeRecipients(paramBoolean);
  }
  
  private MimeBodyPart make(MimeBodyPart paramMimeBodyPart, OutputEncryptor paramOutputEncryptor) throws SMIMEException {
    try {
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(new ContentEncryptor(paramMimeBodyPart, paramOutputEncryptor), "application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data");
      mimeBodyPart.addHeader("Content-Type", "application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data");
      mimeBodyPart.addHeader("Content-Disposition", "attachment; filename=\"smime.p7m\"");
      mimeBodyPart.addHeader("Content-Description", "S/MIME Encrypted Message");
      mimeBodyPart.addHeader("Content-Transfer-Encoding", this.encoding);
      return mimeBodyPart;
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception putting multi-part together.", messagingException);
    } 
  }
  
  public MimeBodyPart generate(MimeBodyPart paramMimeBodyPart, OutputEncryptor paramOutputEncryptor) throws SMIMEException {
    return make(makeContentBodyPart(paramMimeBodyPart), paramOutputEncryptor);
  }
  
  public MimeBodyPart generate(MimeMessage paramMimeMessage, OutputEncryptor paramOutputEncryptor) throws SMIMEException {
    try {
      paramMimeMessage.saveChanges();
    } catch (MessagingException messagingException) {
      throw new SMIMEException("unable to save message", messagingException);
    } 
    return make(makeContentBodyPart(paramMimeMessage), paramOutputEncryptor);
  }
  
  static {
    AccessController.doPrivileged(new PrivilegedAction() {
          public Object run() {
            CommandMap commandMap = CommandMap.getDefaultCommandMap();
            if (commandMap instanceof MailcapCommandMap)
              CommandMap.setDefaultCommandMap((CommandMap)MailcapUtil.addCommands((MailcapCommandMap)commandMap)); 
            return null;
          }
        });
  }
  
  private class ContentEncryptor implements SMIMEStreamingProcessor {
    private final MimeBodyPart _content;
    
    private OutputEncryptor _encryptor;
    
    private boolean _firstTime = true;
    
    ContentEncryptor(MimeBodyPart param1MimeBodyPart, OutputEncryptor param1OutputEncryptor) {
      this._content = param1MimeBodyPart;
      this._encryptor = param1OutputEncryptor;
    }
    
    public void write(OutputStream param1OutputStream) throws IOException {
      try {
        OutputStream outputStream;
        if (this._firstTime) {
          outputStream = SMIMEEnvelopedGenerator.this.fact.open(param1OutputStream, this._encryptor);
          this._firstTime = false;
        } else {
          outputStream = SMIMEEnvelopedGenerator.this.fact.regenerate(param1OutputStream, this._encryptor);
        } 
        CommandMap commandMap = CommandMap.getDefaultCommandMap();
        if (commandMap instanceof MailcapCommandMap)
          this._content.getDataHandler().setCommandMap((CommandMap)MailcapUtil.addCommands((MailcapCommandMap)commandMap)); 
        this._content.writeTo(outputStream);
        outputStream.close();
      } catch (MessagingException messagingException) {
        throw new SMIMEEnvelopedGenerator.WrappingIOException(messagingException.toString(), messagingException);
      } catch (CMSException cMSException) {
        throw new SMIMEEnvelopedGenerator.WrappingIOException(cMSException.toString(), cMSException);
      } 
    }
  }
  
  private class EnvelopedGenerator extends CMSEnvelopedDataStreamGenerator {
    private ASN1ObjectIdentifier dataType;
    
    private ASN1EncodableVector recipientInfos;
    
    private EnvelopedGenerator() {}
    
    protected OutputStream open(ASN1ObjectIdentifier param1ASN1ObjectIdentifier, OutputStream param1OutputStream, ASN1EncodableVector param1ASN1EncodableVector, OutputEncryptor param1OutputEncryptor) throws IOException {
      this.dataType = param1ASN1ObjectIdentifier;
      this.recipientInfos = param1ASN1EncodableVector;
      return super.open(param1ASN1ObjectIdentifier, param1OutputStream, param1ASN1EncodableVector, param1OutputEncryptor);
    }
    
    OutputStream regenerate(OutputStream param1OutputStream, OutputEncryptor param1OutputEncryptor) throws IOException {
      return super.open(this.dataType, param1OutputStream, this.recipientInfos, param1OutputEncryptor);
    }
  }
  
  private static class WrappingIOException extends IOException {
    private Throwable cause;
    
    WrappingIOException(String param1String, Throwable param1Throwable) {
      super(param1String);
      this.cause = param1Throwable;
    }
    
    public Throwable getCause() {
      return this.cause;
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMEEnvelopedGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */