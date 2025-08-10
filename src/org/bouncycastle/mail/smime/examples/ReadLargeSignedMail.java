package org.bouncycastle.mail.smime.examples;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.mail.smime.SMIMESignedParser;
import org.bouncycastle.mail.smime.util.SharedFileInputStream;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public class ReadLargeSignedMail {
  private static final String BC = "BC";
  
  private static void verify(SMIMESignedParser paramSMIMESignedParser) throws Exception {
    Store store = paramSMIMESignedParser.getCertificates();
    SignerInformationStore signerInformationStore = paramSMIMESignedParser.getSignerInfos();
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
    MimeMessage mimeMessage = new MimeMessage(session, (InputStream)new SharedFileInputStream("signed.message"));
    if (mimeMessage.isMimeType("multipart/signed")) {
      SMIMESignedParser sMIMESignedParser = new SMIMESignedParser((new JcaDigestCalculatorProviderBuilder()).build(), (MimeMultipart)mimeMessage.getContent());
      System.out.println("Status:");
      verify(sMIMESignedParser);
    } else if (mimeMessage.isMimeType("application/pkcs7-mime")) {
      SMIMESignedParser sMIMESignedParser = new SMIMESignedParser((new JcaDigestCalculatorProviderBuilder()).build(), (Part)mimeMessage);
      System.out.println("Status:");
      verify(sMIMESignedParser);
    } else {
      System.err.println("Not a signed message!");
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ReadLargeSignedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */