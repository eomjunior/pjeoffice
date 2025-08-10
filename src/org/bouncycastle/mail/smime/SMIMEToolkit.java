package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.Recipient;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInfoGenerator;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;

public class SMIMEToolkit {
  private final DigestCalculatorProvider digestCalculatorProvider;
  
  public SMIMEToolkit(DigestCalculatorProvider paramDigestCalculatorProvider) {
    this.digestCalculatorProvider = paramDigestCalculatorProvider;
  }
  
  public boolean isEncrypted(Part paramPart) throws MessagingException {
    return paramPart.getHeader("Content-Type")[0].equals("application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data");
  }
  
  public boolean isSigned(Part paramPart) throws MessagingException {
    return (paramPart.getHeader("Content-Type")[0].startsWith("multipart/signed") || paramPart.getHeader("Content-Type")[0].equals("application/pkcs7-mime; name=smime.p7m; smime-type=signed-data"));
  }
  
  public boolean isSigned(MimeMultipart paramMimeMultipart) throws MessagingException {
    return paramMimeMultipart.getBodyPart(1).getHeader("Content-Type")[0].equals("application/pkcs7-signature; name=smime.p7s; smime-type=signed-data");
  }
  
  public boolean isValidSignature(Part paramPart, SignerInformationVerifier paramSignerInformationVerifier) throws SMIMEException, MessagingException {
    try {
      SMIMESignedParser sMIMESignedParser;
      if (paramPart.isMimeType("multipart/signed")) {
        sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, (MimeMultipart)paramPart.getContent());
      } else {
        sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, paramPart);
      } 
      return isAtLeastOneValidSigner(sMIMESignedParser, paramSignerInformationVerifier);
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } catch (IOException iOException) {
      throw new SMIMEException("Parsing failure: " + iOException.getMessage(), iOException);
    } 
  }
  
  private boolean isAtLeastOneValidSigner(SMIMESignedParser paramSMIMESignedParser, SignerInformationVerifier paramSignerInformationVerifier) throws CMSException {
    if (paramSignerInformationVerifier.hasAssociatedCertificate()) {
      X509CertificateHolder x509CertificateHolder = paramSignerInformationVerifier.getAssociatedCertificate();
      SignerInformation signerInformation = paramSMIMESignedParser.getSignerInfos().get(new SignerId(x509CertificateHolder.getIssuer(), x509CertificateHolder.getSerialNumber()));
      if (signerInformation != null)
        return signerInformation.verify(paramSignerInformationVerifier); 
    } 
    Collection collection = paramSMIMESignedParser.getSignerInfos().getSigners();
    for (SignerInformation signerInformation : collection) {
      if (signerInformation.verify(paramSignerInformationVerifier))
        return true; 
    } 
    return false;
  }
  
  public boolean isValidSignature(MimeMultipart paramMimeMultipart, SignerInformationVerifier paramSignerInformationVerifier) throws SMIMEException, MessagingException {
    try {
      SMIMESignedParser sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, paramMimeMultipart);
      return isAtLeastOneValidSigner(sMIMESignedParser, paramSignerInformationVerifier);
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } 
  }
  
  public X509CertificateHolder extractCertificate(Part paramPart, SignerInformation paramSignerInformation) throws SMIMEException, MessagingException {
    try {
      SMIMESignedParser sMIMESignedParser;
      if (paramPart instanceof MimeMessage && paramPart.isMimeType("multipart/signed")) {
        sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, (MimeMultipart)paramPart.getContent());
      } else {
        sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, paramPart);
      } 
      Collection collection = sMIMESignedParser.getCertificates().getMatches((Selector)paramSignerInformation.getSID());
      Iterator<X509CertificateHolder> iterator = collection.iterator();
      return iterator.hasNext() ? iterator.next() : null;
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } catch (IOException iOException) {
      throw new SMIMEException("Parsing failure: " + iOException.getMessage(), iOException);
    } 
  }
  
  public X509CertificateHolder extractCertificate(MimeMultipart paramMimeMultipart, SignerInformation paramSignerInformation) throws SMIMEException, MessagingException {
    try {
      SMIMESignedParser sMIMESignedParser = new SMIMESignedParser(this.digestCalculatorProvider, paramMimeMultipart);
      Collection collection = sMIMESignedParser.getCertificates().getMatches((Selector)paramSignerInformation.getSID());
      Iterator<X509CertificateHolder> iterator = collection.iterator();
      return iterator.hasNext() ? iterator.next() : null;
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } 
  }
  
  public MimeMultipart sign(MimeBodyPart paramMimeBodyPart, SignerInfoGenerator paramSignerInfoGenerator) throws SMIMEException {
    SMIMESignedGenerator sMIMESignedGenerator = new SMIMESignedGenerator();
    if (paramSignerInfoGenerator.hasAssociatedCertificate()) {
      ArrayList<X509CertificateHolder> arrayList = new ArrayList();
      arrayList.add(paramSignerInfoGenerator.getAssociatedCertificate());
      sMIMESignedGenerator.addCertificates((Store)new CollectionStore(arrayList));
    } 
    sMIMESignedGenerator.addSignerInfoGenerator(paramSignerInfoGenerator);
    return sMIMESignedGenerator.generate(paramMimeBodyPart);
  }
  
  public MimeBodyPart signEncapsulated(MimeBodyPart paramMimeBodyPart, SignerInfoGenerator paramSignerInfoGenerator) throws SMIMEException {
    SMIMESignedGenerator sMIMESignedGenerator = new SMIMESignedGenerator();
    if (paramSignerInfoGenerator.hasAssociatedCertificate()) {
      ArrayList<X509CertificateHolder> arrayList = new ArrayList();
      arrayList.add(paramSignerInfoGenerator.getAssociatedCertificate());
      sMIMESignedGenerator.addCertificates((Store)new CollectionStore(arrayList));
    } 
    sMIMESignedGenerator.addSignerInfoGenerator(paramSignerInfoGenerator);
    return sMIMESignedGenerator.generateEncapsulated(paramMimeBodyPart);
  }
  
  public MimeBodyPart encrypt(MimeBodyPart paramMimeBodyPart, OutputEncryptor paramOutputEncryptor, RecipientInfoGenerator paramRecipientInfoGenerator) throws SMIMEException {
    SMIMEEnvelopedGenerator sMIMEEnvelopedGenerator = new SMIMEEnvelopedGenerator();
    sMIMEEnvelopedGenerator.addRecipientInfoGenerator(paramRecipientInfoGenerator);
    return sMIMEEnvelopedGenerator.generate(paramMimeBodyPart, paramOutputEncryptor);
  }
  
  public MimeBodyPart encrypt(MimeMultipart paramMimeMultipart, OutputEncryptor paramOutputEncryptor, RecipientInfoGenerator paramRecipientInfoGenerator) throws SMIMEException, MessagingException {
    SMIMEEnvelopedGenerator sMIMEEnvelopedGenerator = new SMIMEEnvelopedGenerator();
    sMIMEEnvelopedGenerator.addRecipientInfoGenerator(paramRecipientInfoGenerator);
    MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent((Multipart)paramMimeMultipart);
    return sMIMEEnvelopedGenerator.generate(mimeBodyPart, paramOutputEncryptor);
  }
  
  public MimeBodyPart encrypt(MimeMessage paramMimeMessage, OutputEncryptor paramOutputEncryptor, RecipientInfoGenerator paramRecipientInfoGenerator) throws SMIMEException {
    SMIMEEnvelopedGenerator sMIMEEnvelopedGenerator = new SMIMEEnvelopedGenerator();
    sMIMEEnvelopedGenerator.addRecipientInfoGenerator(paramRecipientInfoGenerator);
    return sMIMEEnvelopedGenerator.generate(paramMimeMessage, paramOutputEncryptor);
  }
  
  public MimeBodyPart decrypt(MimeBodyPart paramMimeBodyPart, RecipientId paramRecipientId, Recipient paramRecipient) throws SMIMEException, MessagingException {
    try {
      SMIMEEnvelopedParser sMIMEEnvelopedParser = new SMIMEEnvelopedParser(paramMimeBodyPart);
      RecipientInformationStore recipientInformationStore = sMIMEEnvelopedParser.getRecipientInfos();
      RecipientInformation recipientInformation = recipientInformationStore.get(paramRecipientId);
      return (recipientInformation == null) ? null : SMIMEUtil.toMimeBodyPart(recipientInformation.getContent(paramRecipient));
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } catch (IOException iOException) {
      throw new SMIMEException("Parsing failure: " + iOException.getMessage(), iOException);
    } 
  }
  
  public MimeBodyPart decrypt(MimeMessage paramMimeMessage, RecipientId paramRecipientId, Recipient paramRecipient) throws SMIMEException, MessagingException {
    try {
      SMIMEEnvelopedParser sMIMEEnvelopedParser = new SMIMEEnvelopedParser(paramMimeMessage);
      RecipientInformationStore recipientInformationStore = sMIMEEnvelopedParser.getRecipientInfos();
      RecipientInformation recipientInformation = recipientInformationStore.get(paramRecipientId);
      return (recipientInformation == null) ? null : SMIMEUtil.toMimeBodyPart(recipientInformation.getContent(paramRecipient));
    } catch (CMSException cMSException) {
      throw new SMIMEException("CMS processing failure: " + cMSException.getMessage(), cMSException);
    } catch (IOException iOException) {
      throw new SMIMEException("Parsing failure: " + iOException.getMessage(), iOException);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMEToolkit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */