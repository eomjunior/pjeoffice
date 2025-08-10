package org.bouncycastle.mail.smime.examples;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import org.bouncycastle.cms.jcajce.ZlibExpanderProvider;
import org.bouncycastle.mail.smime.SMIMECompressedParser;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.mail.smime.util.FileBackedMimeBodyPart;
import org.bouncycastle.mail.smime.util.SharedFileInputStream;
import org.bouncycastle.operator.InputExpanderProvider;

public class ReadLargeCompressedMail {
  public static void main(String[] paramArrayOfString) throws Exception {
    Properties properties = System.getProperties();
    Session session = Session.getDefaultInstance(properties, null);
    MimeMessage mimeMessage = new MimeMessage(session, (InputStream)new SharedFileInputStream("compressed.message"));
    SMIMECompressedParser sMIMECompressedParser = new SMIMECompressedParser(mimeMessage);
    FileBackedMimeBodyPart fileBackedMimeBodyPart = SMIMEUtil.toMimeBodyPart(sMIMECompressedParser.getContent((InputExpanderProvider)new ZlibExpanderProvider()));
    ExampleUtils.dumpContent((MimeBodyPart)fileBackedMimeBodyPart, paramArrayOfString[0]);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/examples/ReadLargeCompressedMail.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */