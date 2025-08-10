package org.bouncycastle.mail.smime.examples;

import java.io.FileInputStream;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider;
import org.bouncycastle.mail.smime.SMIMECompressed;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.operator.InputExpanderProvider;

public class ReadCompressedMail {
  public static void main(String[] paramArrayOfString) throws Exception {
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties, null);
    MimeMessage mimeMessage = new MimeMessage(session, new FileInputStream("compressed.message"));
    SMIMECompressed sMIMECompressed = new SMIMECompressed(mimeMessage);
    MimeBodyPart mimeBodyPart = SMIMEUtil.toMimeBodyPart(sMIMECompressed.getContent((InputExpanderProvider)new ZlibExpanderProvider()));
    System.out.println("Message Contents");
    System.out.println("----------------");
    System.out.println(mimeBodyPart.getContent());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ReadCompressedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */