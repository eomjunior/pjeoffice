package org.bouncycastle.mail.smime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSTypedStream;
import org.bouncycastle.mail.smime.util.CRLFOutputStream;
import org.bouncycastle.mail.smime.util.FileBackedMimeBodyPart;
import org.bouncycastle.util.Strings;

public class SMIMEUtil {
  private static final String MULTIPART = "multipart";
  
  private static final int BUF_SIZE = 32760;
  
  public static boolean isMultipartContent(Part paramPart) throws MessagingException {
    String str = Strings.toLowerCase(paramPart.getContentType());
    return str.startsWith("multipart");
  }
  
  static boolean isCanonicalisationRequired(MimeBodyPart paramMimeBodyPart, String paramString) throws MessagingException {
    String str;
    String[] arrayOfString = paramMimeBodyPart.getHeader("Content-Transfer-Encoding");
    if (arrayOfString == null) {
      str = paramString;
    } else {
      str = arrayOfString[0];
    } 
    return !str.equalsIgnoreCase("binary");
  }
  
  static void outputPreamble(LineOutputStream paramLineOutputStream, MimeBodyPart paramMimeBodyPart, String paramString) throws MessagingException, IOException {
    InputStream inputStream;
    try {
      inputStream = paramMimeBodyPart.getRawInputStream();
    } catch (MessagingException messagingException) {
      return;
    } 
    String str;
    while ((str = readLine(inputStream)) != null && !str.equals(paramString))
      paramLineOutputStream.writeln(str); 
    inputStream.close();
    if (str == null)
      throw new MessagingException("no boundary found"); 
  }
  
  static void outputPostamble(LineOutputStream paramLineOutputStream, MimeBodyPart paramMimeBodyPart, int paramInt, String paramString) throws MessagingException, IOException {
    InputStream inputStream;
    try {
      inputStream = paramMimeBodyPart.getRawInputStream();
    } catch (MessagingException messagingException) {
      return;
    } 
    int i = paramInt + 1;
    String str;
    do {
    
    } while ((str = readLine(inputStream)) != null && (!str.startsWith(paramString) || --i != 0));
    while ((str = readLine(inputStream)) != null)
      paramLineOutputStream.writeln(str); 
    inputStream.close();
    if (i != 0)
      throw new MessagingException("all boundaries not found for: " + paramString); 
  }
  
  static void outputPostamble(LineOutputStream paramLineOutputStream, BodyPart paramBodyPart1, String paramString, BodyPart paramBodyPart2) throws MessagingException, IOException {
    InputStream inputStream;
    try {
      inputStream = ((MimeBodyPart)paramBodyPart1).getRawInputStream();
    } catch (MessagingException messagingException) {
      return;
    } 
    MimeMultipart mimeMultipart = (MimeMultipart)paramBodyPart2.getContent();
    ContentType contentType = new ContentType(mimeMultipart.getContentType());
    String str1 = "--" + contentType.getParameter("boundary");
    int i = mimeMultipart.getCount() + 1;
    String str2;
    while (i != 0 && (str2 = readLine(inputStream)) != null) {
      if (str2.startsWith(str1))
        i--; 
    } 
    while ((str2 = readLine(inputStream)) != null && !str2.startsWith(paramString))
      paramLineOutputStream.writeln(str2); 
    inputStream.close();
  }
  
  private static String readLine(InputStream paramInputStream) throws IOException {
    StringBuffer stringBuffer = new StringBuffer();
    int i;
    while ((i = paramInputStream.read()) >= 0 && i != 10) {
      if (i != 13)
        stringBuffer.append((char)i); 
    } 
    return (i < 0 && stringBuffer.length() == 0) ? null : stringBuffer.toString();
  }
  
  static void outputBodyPart(OutputStream paramOutputStream, boolean paramBoolean, BodyPart paramBodyPart, String paramString) throws MessagingException, IOException {
    CRLFOutputStream cRLFOutputStream;
    if (paramBodyPart instanceof MimeBodyPart) {
      String str;
      InputStream inputStream;
      CRLFOutputStream cRLFOutputStream1;
      MimeBodyPart mimeBodyPart = (MimeBodyPart)paramBodyPart;
      String[] arrayOfString = mimeBodyPart.getHeader("Content-Transfer-Encoding");
      if (isMultipartContent((Part)mimeBodyPart)) {
        MimeMultipart mimeMultipart;
        Object object = paramBodyPart.getContent();
        if (object instanceof Multipart) {
          Multipart multipart = (Multipart)object;
        } else {
          mimeMultipart = new MimeMultipart(paramBodyPart.getDataHandler().getDataSource());
        } 
        ContentType contentType = new ContentType(mimeMultipart.getContentType());
        String str1 = "--" + contentType.getParameter("boundary");
        LineOutputStream lineOutputStream1 = new LineOutputStream(paramOutputStream);
        Enumeration<String> enumeration1 = mimeBodyPart.getAllHeaderLines();
        while (enumeration1.hasMoreElements()) {
          String str2 = enumeration1.nextElement();
          lineOutputStream1.writeln(str2);
        } 
        lineOutputStream1.writeln();
        outputPreamble(lineOutputStream1, mimeBodyPart, str1);
        for (byte b = 0; b < mimeMultipart.getCount(); b++) {
          lineOutputStream1.writeln(str1);
          BodyPart bodyPart = mimeMultipart.getBodyPart(b);
          outputBodyPart(paramOutputStream, false, bodyPart, paramString);
          if (!isMultipartContent((Part)bodyPart)) {
            lineOutputStream1.writeln();
          } else {
            outputPostamble(lineOutputStream1, (BodyPart)mimeBodyPart, str1, bodyPart);
          } 
        } 
        lineOutputStream1.writeln(str1 + "--");
        if (paramBoolean)
          outputPostamble(lineOutputStream1, mimeBodyPart, mimeMultipart.getCount(), str1); 
        return;
      } 
      if (arrayOfString == null) {
        str = paramString;
      } else {
        str = arrayOfString[0];
      } 
      if (!str.equalsIgnoreCase("base64") && !str.equalsIgnoreCase("quoted-printable")) {
        if (!str.equalsIgnoreCase("binary"))
          cRLFOutputStream = new CRLFOutputStream(paramOutputStream); 
        paramBodyPart.writeTo((OutputStream)cRLFOutputStream);
        cRLFOutputStream.flush();
        return;
      } 
      boolean bool = str.equalsIgnoreCase("base64");
      try {
        inputStream = mimeBodyPart.getRawInputStream();
      } catch (MessagingException messagingException) {
        cRLFOutputStream = new CRLFOutputStream((OutputStream)cRLFOutputStream);
        paramBodyPart.writeTo((OutputStream)cRLFOutputStream);
        cRLFOutputStream.flush();
        return;
      } 
      LineOutputStream lineOutputStream = new LineOutputStream((OutputStream)cRLFOutputStream);
      Enumeration<String> enumeration = mimeBodyPart.getAllHeaderLines();
      while (enumeration.hasMoreElements()) {
        String str1 = enumeration.nextElement();
        lineOutputStream.writeln(str1);
      } 
      lineOutputStream.writeln();
      lineOutputStream.flush();
      if (bool) {
        Base64CRLFOutputStream base64CRLFOutputStream = new Base64CRLFOutputStream((OutputStream)cRLFOutputStream);
      } else {
        cRLFOutputStream1 = new CRLFOutputStream((OutputStream)cRLFOutputStream);
      } 
      byte[] arrayOfByte = new byte[32760];
      int i;
      while ((i = inputStream.read(arrayOfByte, 0, arrayOfByte.length)) > 0)
        cRLFOutputStream1.write(arrayOfByte, 0, i); 
      inputStream.close();
      cRLFOutputStream1.flush();
    } else {
      if (!paramString.equalsIgnoreCase("binary"))
        cRLFOutputStream = new CRLFOutputStream((OutputStream)cRLFOutputStream); 
      paramBodyPart.writeTo((OutputStream)cRLFOutputStream);
      cRLFOutputStream.flush();
    } 
  }
  
  public static MimeBodyPart toMimeBodyPart(byte[] paramArrayOfbyte) throws SMIMEException {
    return toMimeBodyPart(new ByteArrayInputStream(paramArrayOfbyte));
  }
  
  public static MimeBodyPart toMimeBodyPart(InputStream paramInputStream) throws SMIMEException {
    try {
      return new MimeBodyPart(paramInputStream);
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception creating body part.", messagingException);
    } 
  }
  
  static FileBackedMimeBodyPart toWriteOnceBodyPart(CMSTypedStream paramCMSTypedStream) throws SMIMEException {
    try {
      return new WriteOnceFileBackedMimeBodyPart(paramCMSTypedStream.getContentStream(), File.createTempFile("bcMail", ".mime"));
    } catch (IOException iOException) {
      throw new SMIMEException("IOException creating tmp file:" + iOException.getMessage(), iOException);
    } catch (MessagingException messagingException) {
      throw new SMIMEException("can't create part: " + messagingException, messagingException);
    } 
  }
  
  public static FileBackedMimeBodyPart toMimeBodyPart(CMSTypedStream paramCMSTypedStream) throws SMIMEException {
    try {
      return toMimeBodyPart(paramCMSTypedStream, File.createTempFile("bcMail", ".mime"));
    } catch (IOException iOException) {
      throw new SMIMEException("IOException creating tmp file:" + iOException.getMessage(), iOException);
    } 
  }
  
  public static FileBackedMimeBodyPart toMimeBodyPart(CMSTypedStream paramCMSTypedStream, File paramFile) throws SMIMEException {
    try {
      return new FileBackedMimeBodyPart(paramCMSTypedStream.getContentStream(), paramFile);
    } catch (IOException iOException) {
      throw new SMIMEException("can't save content to file: " + iOException, iOException);
    } catch (MessagingException messagingException) {
      throw new SMIMEException("can't create part: " + messagingException, messagingException);
    } 
  }
  
  public static IssuerAndSerialNumber createIssuerAndSerialNumberFor(X509Certificate paramX509Certificate) throws CertificateParsingException {
    try {
      return new IssuerAndSerialNumber((new JcaX509CertificateHolder(paramX509Certificate)).getIssuer(), paramX509Certificate.getSerialNumber());
    } catch (Exception exception) {
      throw new CertificateParsingException("exception extracting issuer and serial number: " + exception);
    } 
  }
  
  static class Base64CRLFOutputStream extends FilterOutputStream {
    protected int lastb = -1;
    
    protected static byte[] newline = new byte[2];
    
    private boolean isCrlfStream;
    
    public Base64CRLFOutputStream(OutputStream param1OutputStream) {
      super(param1OutputStream);
    }
    
    public void write(int param1Int) throws IOException {
      if (param1Int == 13) {
        this.out.write(newline);
      } else if (param1Int == 10) {
        if (this.lastb != 13) {
          if (!this.isCrlfStream || this.lastb != 10)
            this.out.write(newline); 
        } else {
          this.isCrlfStream = true;
        } 
      } else {
        this.out.write(param1Int);
      } 
      this.lastb = param1Int;
    }
    
    public void write(byte[] param1ArrayOfbyte) throws IOException {
      write(param1ArrayOfbyte, 0, param1ArrayOfbyte.length);
    }
    
    public void write(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws IOException {
      for (int i = param1Int1; i != param1Int1 + param1Int2; i++)
        write(param1ArrayOfbyte[i]); 
    }
    
    public void writeln() throws IOException {
      this.out.write(newline);
    }
    
    static {
      newline[0] = 13;
      newline[1] = 10;
    }
  }
  
  static class LineOutputStream extends FilterOutputStream {
    private static byte[] newline = new byte[2];
    
    public LineOutputStream(OutputStream param1OutputStream) {
      super(param1OutputStream);
    }
    
    public void writeln(String param1String) throws MessagingException {
      try {
        byte[] arrayOfByte = getBytes(param1String);
        this.out.write(arrayOfByte);
        this.out.write(newline);
      } catch (Exception exception) {
        throw new MessagingException("IOException", exception);
      } 
    }
    
    public void writeln() throws MessagingException {
      try {
        this.out.write(newline);
      } catch (Exception exception) {
        throw new MessagingException("IOException", exception);
      } 
    }
    
    private static byte[] getBytes(String param1String) {
      char[] arrayOfChar = param1String.toCharArray();
      int i = arrayOfChar.length;
      byte[] arrayOfByte = new byte[i];
      byte b = 0;
      while (b < i)
        arrayOfByte[b] = (byte)arrayOfChar[b++]; 
      return arrayOfByte;
    }
    
    static {
      newline[0] = 13;
      newline[1] = 10;
    }
  }
  
  private static class WriteOnceFileBackedMimeBodyPart extends FileBackedMimeBodyPart {
    public WriteOnceFileBackedMimeBodyPart(InputStream param1InputStream, File param1File) throws MessagingException, IOException {
      super(param1InputStream, param1File);
    }
    
    public void writeTo(OutputStream param1OutputStream) throws MessagingException, IOException {
      super.writeTo(param1OutputStream);
      dispose();
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMEUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */