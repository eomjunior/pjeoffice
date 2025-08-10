package org.bouncycastle.mail.smime.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.RecipientInfoGenerator;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;

public class CreateEncryptedMail {
  public static void main(String[] paramArrayOfString) throws Exception {
    if (paramArrayOfString.length != 2) {
      System.err.println("usage: CreateEncryptedMail pkcs12Keystore password");
      System.exit(0);
    } 
    if (Security.getProvider("BC") == null)
      Security.addProvider((Provider)new BouncyCastleProvider()); 
    KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
    keyStore.load(new FileInputStream(paramArrayOfString[0]), paramArrayOfString[1].toCharArray());
    Enumeration<String> enumeration = keyStore.aliases();
    String str = null;
    while (enumeration.hasMoreElements()) {
      String str1 = enumeration.nextElement();
      if (keyStore.isKeyEntry(str1))
        str = str1; 
    } 
    if (str == null) {
      System.err.println("can't find a private key!");
      System.exit(0);
    } 
    Certificate[] arrayOfCertificate = keyStore.getCertificateChain(str);
    SMIMEEnvelopedGenerator sMIMEEnvelopedGenerator = new SMIMEEnvelopedGenerator();
    sMIMEEnvelopedGenerator.addRecipientInfoGenerator((RecipientInfoGenerator)(new JceKeyTransRecipientInfoGenerator((X509Certificate)arrayOfCertificate[0])).setProvider("BC"));
    MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
    mimeBodyPart1.setText("Hello world!");
    MimeBodyPart mimeBodyPart2 = sMIMEEnvelopedGenerator.generate(mimeBodyPart1, (new JceCMSContentEncryptorBuilder(CMSAlgorithm.RC2_CBC)).setProvider("BC").build());
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties, null);
    InternetAddress internetAddress1 = new InternetAddress("\"Eric H. Echidna\"<eric@bouncycastle.org>");
    InternetAddress internetAddress2 = new InternetAddress("example@bouncycastle.org");
    MimeMessage mimeMessage = new MimeMessage(session);
    mimeMessage.setFrom((Address)internetAddress1);
    mimeMessage.setRecipient(Message.RecipientType.TO, (Address)internetAddress2);
    mimeMessage.setSubject("example encrypted message");
    mimeMessage.setContent(mimeBodyPart2.getContent(), mimeBodyPart2.getContentType());
    mimeMessage.saveChanges();
    mimeMessage.writeTo(new FileOutputStream("encrypted.message"));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/CreateEncryptedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */