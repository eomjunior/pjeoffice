package org.bouncycastle.mail.smime.examples;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.mail.smime.SMIMEEnvelopedParser;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.mail.smime.util.FileBackedMimeBodyPart;
import org.bouncycastle.mail.smime.util.SharedFileInputStream;

public class ReadLargeEncryptedMail {
  public static void main(String[] paramArrayOfString) throws Exception {
    if (paramArrayOfString.length != 3) {
      System.err.println("usage: ReadLargeEncryptedMail pkcs12Keystore password outputFile");
      System.exit(0);
    } 
    KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
    String str = ExampleUtils.findKeyAlias(keyStore, paramArrayOfString[0], paramArrayOfString[1].toCharArray());
    X509Certificate x509Certificate = (X509Certificate)keyStore.getCertificate(str);
    JceKeyTransRecipientId jceKeyTransRecipientId = new JceKeyTransRecipientId(x509Certificate);
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties, null);
    MimeMessage mimeMessage = new MimeMessage(session, (InputStream)new SharedFileInputStream("encrypted.message"));
    SMIMEEnvelopedParser sMIMEEnvelopedParser = new SMIMEEnvelopedParser(mimeMessage);
    RecipientInformationStore recipientInformationStore = sMIMEEnvelopedParser.getRecipientInfos();
    RecipientInformation recipientInformation = recipientInformationStore.get((RecipientId)jceKeyTransRecipientId);
    FileBackedMimeBodyPart fileBackedMimeBodyPart = SMIMEUtil.toMimeBodyPart(recipientInformation.getContentStream((Recipient)(new JceKeyTransEnvelopedRecipient((PrivateKey)keyStore.getKey(str, null))).setProvider("BC")));
    ExampleUtils.dumpContent((MimeBodyPart)fileBackedMimeBodyPart, paramArrayOfString[2]);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ReadLargeEncryptedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */