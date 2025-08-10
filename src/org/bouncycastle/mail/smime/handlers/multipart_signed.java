package org.bouncycastle.mail.smime.handlers;

import java.awt.datatransfer.DataFlavor;
import java.io.BufferedInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.mail.smime.SMIMEStreamingProcessor;
import org.bouncycastle.mail.smime.SMIMEUtil;

public class multipart_signed implements DataContentHandler {
  private static final ActivationDataFlavor ADF = new ActivationDataFlavor(MimeMultipart.class, "multipart/signed", "Multipart Signed");
  
  private static final DataFlavor[] DFS = new DataFlavor[] { (DataFlavor)ADF };
  
  public Object getContent(DataSource paramDataSource) throws IOException {
    try {
      return new MimeMultipart(paramDataSource);
    } catch (MessagingException messagingException) {
      return null;
    } 
  }
  
  public Object getTransferData(DataFlavor paramDataFlavor, DataSource paramDataSource) throws IOException {
    return ADF.equals(paramDataFlavor) ? getContent(paramDataSource) : null;
  }
  
  public DataFlavor[] getTransferDataFlavors() {
    return DFS;
  }
  
  public void writeTo(Object paramObject, String paramString, OutputStream paramOutputStream) throws IOException {
    if (paramObject instanceof MimeMultipart) {
      try {
        outputBodyPart(paramOutputStream, paramObject);
      } catch (MessagingException messagingException) {
        throw new IOException(messagingException.getMessage());
      } 
    } else if (paramObject instanceof byte[]) {
      paramOutputStream.write((byte[])paramObject);
    } else if (paramObject instanceof InputStream) {
      InputStream inputStream = (InputStream)paramObject;
      if (!(inputStream instanceof BufferedInputStream))
        inputStream = new BufferedInputStream(inputStream); 
      int i;
      while ((i = inputStream.read()) >= 0)
        paramOutputStream.write(i); 
      inputStream.close();
    } else if (paramObject instanceof SMIMEStreamingProcessor) {
      SMIMEStreamingProcessor sMIMEStreamingProcessor = (SMIMEStreamingProcessor)paramObject;
      sMIMEStreamingProcessor.write(paramOutputStream);
    } else {
      throw new IOException("unknown object in writeTo " + paramObject);
    } 
  }
  
  private void outputBodyPart(OutputStream paramOutputStream, Object paramObject) throws MessagingException, IOException {
    if (paramObject instanceof Multipart) {
      Multipart multipart = (Multipart)paramObject;
      ContentType contentType = new ContentType(multipart.getContentType());
      String str = "--" + contentType.getParameter("boundary");
      LineOutputStream lineOutputStream = new LineOutputStream(paramOutputStream);
      for (byte b = 0; b < multipart.getCount(); b++) {
        lineOutputStream.writeln(str);
        outputBodyPart(paramOutputStream, multipart.getBodyPart(b));
        lineOutputStream.writeln();
      } 
      lineOutputStream.writeln(str + "--");
      return;
    } 
    MimeBodyPart mimeBodyPart = (MimeBodyPart)paramObject;
    if (SMIMEUtil.isMultipartContent((Part)mimeBodyPart)) {
      Object object = mimeBodyPart.getContent();
      if (object instanceof Multipart) {
        Multipart multipart = (Multipart)object;
        ContentType contentType = new ContentType(multipart.getContentType());
        String str = "--" + contentType.getParameter("boundary");
        LineOutputStream lineOutputStream = new LineOutputStream(paramOutputStream);
        Enumeration<String> enumeration = mimeBodyPart.getAllHeaderLines();
        while (enumeration.hasMoreElements())
          lineOutputStream.writeln(enumeration.nextElement()); 
        lineOutputStream.writeln();
        outputPreamble(lineOutputStream, mimeBodyPart, str);
        outputBodyPart(paramOutputStream, multipart);
        return;
      } 
    } 
    mimeBodyPart.writeTo(paramOutputStream);
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
  
  private static String readLine(InputStream paramInputStream) throws IOException {
    StringBuffer stringBuffer = new StringBuffer();
    int i;
    while ((i = paramInputStream.read()) >= 0 && i != 10) {
      if (i != 13)
        stringBuffer.append((char)i); 
    } 
    return (i < 0) ? null : stringBuffer.toString();
  }
  
  private static class LineOutputStream extends FilterOutputStream {
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
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/handlers/multipart_signed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */