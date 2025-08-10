package org.bouncycastle.mail.smime.examples;

import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public class ReadSignedMail {
  private static final String BC = "BC";
  
  private static void verify(SMIMESigned paramSMIMESigned) throws Exception {
    Store store = paramSMIMESigned.getCertificates();
    SignerInformationStore signerInformationStore = paramSMIMESigned.getSignerInfos();
    Collection collection = signerInformationStore.getSigners();
    for (SignerInformation signerInformation : collection) {
      Collection collection1 = store.getMatches((Selector)signerInformation.getSID());
      Iterator<X509CertificateHolder> iterator = collection1.iterator();
      X509Certificate x509Certificate = (new JcaX509CertificateConverter()).setProvider("BC").getCertificate(iterator.next());
      if (signerInformation.verify((new JcaSimpleSignerInfoVerifierBuilder()).setProvider("BC").build(x509Certificate))) {
        System.out.println("signature verified");
        continue;
      } 
      System.out.println("signature failed!");
    } 
  }
  
  public static void main(String[] paramArrayOfString) throws Exception {
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties, null);
    MimeMessage mimeMessage = new MimeMessage(session, new FileInputStream("signed.message"));
    if (mimeMessage.isMimeType("multipart/signed")) {
      SMIMESigned sMIMESigned = new SMIMESigned((MimeMultipart)mimeMessage.getContent());
      MimeBodyPart mimeBodyPart = sMIMESigned.getContent();
      System.out.println("Content:");
      Object object = mimeBodyPart.getContent();
      if (object instanceof String) {
        System.out.println((String)object);
      } else if (object instanceof Multipart) {
        Multipart multipart = (Multipart)object;
        int i = multipart.getCount();
        for (byte b = 0; b < i; b++) {
          BodyPart bodyPart = multipart.getBodyPart(b);
          Object object1 = bodyPart.getContent();
          System.out.println("Part " + b);
          System.out.println("---------------------------");
          if (object1 instanceof String) {
            System.out.println((String)object1);
          } else {
            System.out.println("can't print...");
          } 
        } 
      } 
      System.out.println("Status:");
      verify(sMIMESigned);
    } else if (mimeMessage.isMimeType("application/pkcs7-mime") || mimeMessage.isMimeType("application/x-pkcs7-mime")) {
      SMIMESigned sMIMESigned = new SMIMESigned((Part)mimeMessage);
      MimeBodyPart mimeBodyPart = sMIMESigned.getContent();
      System.out.println("Content:");
      Object object = mimeBodyPart.getContent();
      if (object instanceof String)
        System.out.println((String)object); 
      System.out.println("Status:");
      verify(sMIMESigned);
    } else {
      System.err.println("Not a signed message!");
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ReadSignedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */