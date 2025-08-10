package org.bouncycastle.mail.smime;

import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.rosstandart.RosstandartObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedDataStreamGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.mail.smime.util.CRLFOutputStream;
import org.bouncycastle.util.Store;

public class SMIMESignedGenerator extends SMIMEGenerator {
  public static final String DIGEST_SHA1 = OIWObjectIdentifiers.idSHA1.getId();
  
  public static final String DIGEST_MD5 = PKCSObjectIdentifiers.md5.getId();
  
  public static final String DIGEST_SHA224 = NISTObjectIdentifiers.id_sha224.getId();
  
  public static final String DIGEST_SHA256 = NISTObjectIdentifiers.id_sha256.getId();
  
  public static final String DIGEST_SHA384 = NISTObjectIdentifiers.id_sha384.getId();
  
  public static final String DIGEST_SHA512 = NISTObjectIdentifiers.id_sha512.getId();
  
  public static final String DIGEST_GOST3411 = CryptoProObjectIdentifiers.gostR3411.getId();
  
  public static final String DIGEST_RIPEMD128 = TeleTrusTObjectIdentifiers.ripemd128.getId();
  
  public static final String DIGEST_RIPEMD160 = TeleTrusTObjectIdentifiers.ripemd160.getId();
  
  public static final String DIGEST_RIPEMD256 = TeleTrusTObjectIdentifiers.ripemd256.getId();
  
  public static final String ENCRYPTION_RSA = PKCSObjectIdentifiers.rsaEncryption.getId();
  
  public static final String ENCRYPTION_DSA = X9ObjectIdentifiers.id_dsa_with_sha1.getId();
  
  public static final String ENCRYPTION_ECDSA = X9ObjectIdentifiers.ecdsa_with_SHA1.getId();
  
  public static final String ENCRYPTION_RSA_PSS = PKCSObjectIdentifiers.id_RSASSA_PSS.getId();
  
  public static final String ENCRYPTION_GOST3410 = CryptoProObjectIdentifiers.gostR3410_94.getId();
  
  public static final String ENCRYPTION_ECGOST3410 = CryptoProObjectIdentifiers.gostR3410_2001.getId();
  
  public static final String ENCRYPTION_ECGOST3410_2012_256 = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256.getId();
  
  public static final String ENCRYPTION_ECGOST3410_2012_512 = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512.getId();
  
  private static final String CERTIFICATE_MANAGEMENT_CONTENT = "application/pkcs7-mime; name=smime.p7c; smime-type=certs-only";
  
  private static final String DETACHED_SIGNATURE_TYPE = "application/pkcs7-signature; name=smime.p7s; smime-type=signed-data";
  
  private static final String ENCAPSULATED_SIGNED_CONTENT_TYPE = "application/pkcs7-mime; name=smime.p7m; smime-type=signed-data";
  
  public static final Map RFC3851_MICALGS;
  
  public static final Map RFC5751_MICALGS;
  
  public static final Map STANDARD_MICALGS;
  
  private final String defaultContentTransferEncoding;
  
  private final Map micAlgs;
  
  private List certStores = new ArrayList();
  
  private List crlStores = new ArrayList();
  
  private List attrCertStores = new ArrayList();
  
  private List signerInfoGens = new ArrayList();
  
  private List _signers = new ArrayList();
  
  private List _oldSigners = new ArrayList();
  
  private Map _digests = new HashMap<Object, Object>();
  
  public SMIMESignedGenerator() {
    this("7bit", STANDARD_MICALGS);
  }
  
  public SMIMESignedGenerator(String paramString) {
    this(paramString, STANDARD_MICALGS);
  }
  
  public SMIMESignedGenerator(Map paramMap) {
    this("7bit", paramMap);
  }
  
  public SMIMESignedGenerator(String paramString, Map paramMap) {
    this.defaultContentTransferEncoding = paramString;
    this.micAlgs = paramMap;
  }
  
  public void addSigners(SignerInformationStore paramSignerInformationStore) {
    Iterator iterator = paramSignerInformationStore.getSigners().iterator();
    while (iterator.hasNext())
      this._oldSigners.add(iterator.next()); 
  }
  
  public void addSignerInfoGenerator(SignerInfoGenerator paramSignerInfoGenerator) {
    this.signerInfoGens.add(paramSignerInfoGenerator);
  }
  
  public void addCertificates(Store paramStore) {
    this.certStores.add(paramStore);
  }
  
  public void addCRLs(Store paramStore) {
    this.crlStores.add(paramStore);
  }
  
  public void addAttributeCertificates(Store paramStore) {
    this.attrCertStores.add(paramStore);
  }
  
  private void addHashHeader(StringBuffer paramStringBuffer, List paramList) {
    byte b = 0;
    null = paramList.iterator();
    TreeSet<String> treeSet = new TreeSet();
    while (null.hasNext()) {
      ASN1ObjectIdentifier aSN1ObjectIdentifier;
      SignerInformation signerInformation = (SignerInformation)null.next();
      if (signerInformation instanceof SignerInformation) {
        aSN1ObjectIdentifier = ((SignerInformation)signerInformation).getDigestAlgorithmID().getAlgorithm();
      } else {
        aSN1ObjectIdentifier = ((SignerInfoGenerator)signerInformation).getDigestAlgorithm().getAlgorithm();
      } 
      String str = (String)this.micAlgs.get(aSN1ObjectIdentifier);
      if (str == null) {
        treeSet.add("unknown");
        continue;
      } 
      treeSet.add(str);
    } 
    for (String str : treeSet) {
      if (!b) {
        if (treeSet.size() != 1) {
          paramStringBuffer.append("; micalg=\"");
        } else {
          paramStringBuffer.append("; micalg=");
        } 
      } else {
        paramStringBuffer.append(',');
      } 
      paramStringBuffer.append(str);
      b++;
    } 
    if (b != 0 && treeSet.size() != 1)
      paramStringBuffer.append('"'); 
  }
  
  private MimeMultipart make(MimeBodyPart paramMimeBodyPart) throws SMIMEException {
    try {
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(new ContentSigner(paramMimeBodyPart, false), "application/pkcs7-signature; name=smime.p7s; smime-type=signed-data");
      mimeBodyPart.addHeader("Content-Type", "application/pkcs7-signature; name=smime.p7s; smime-type=signed-data");
      mimeBodyPart.addHeader("Content-Disposition", "attachment; filename=\"smime.p7s\"");
      mimeBodyPart.addHeader("Content-Description", "S/MIME Cryptographic Signature");
      mimeBodyPart.addHeader("Content-Transfer-Encoding", this.encoding);
      StringBuffer stringBuffer = new StringBuffer("signed; protocol=\"application/pkcs7-signature\"");
      ArrayList arrayList = new ArrayList(this._signers);
      arrayList.addAll(this._oldSigners);
      arrayList.addAll(this.signerInfoGens);
      addHashHeader(stringBuffer, arrayList);
      MimeMultipart mimeMultipart = new MimeMultipart(stringBuffer.toString());
      mimeMultipart.addBodyPart((BodyPart)paramMimeBodyPart);
      mimeMultipart.addBodyPart((BodyPart)mimeBodyPart);
      return mimeMultipart;
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception putting multi-part together.", messagingException);
    } 
  }
  
  private MimeBodyPart makeEncapsulated(MimeBodyPart paramMimeBodyPart) throws SMIMEException {
    try {
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(new ContentSigner(paramMimeBodyPart, true), "application/pkcs7-mime; name=smime.p7m; smime-type=signed-data");
      mimeBodyPart.addHeader("Content-Type", "application/pkcs7-mime; name=smime.p7m; smime-type=signed-data");
      mimeBodyPart.addHeader("Content-Disposition", "attachment; filename=\"smime.p7m\"");
      mimeBodyPart.addHeader("Content-Description", "S/MIME Cryptographic Signed Data");
      mimeBodyPart.addHeader("Content-Transfer-Encoding", this.encoding);
      return mimeBodyPart;
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception putting body part together.", messagingException);
    } 
  }
  
  public Map getGeneratedDigests() {
    return new HashMap<Object, Object>(this._digests);
  }
  
  public MimeMultipart generate(MimeBodyPart paramMimeBodyPart) throws SMIMEException {
    return make(makeContentBodyPart(paramMimeBodyPart));
  }
  
  public MimeMultipart generate(MimeMessage paramMimeMessage) throws SMIMEException {
    try {
      paramMimeMessage.saveChanges();
    } catch (MessagingException messagingException) {
      throw new SMIMEException("unable to save message", messagingException);
    } 
    return make(makeContentBodyPart(paramMimeMessage));
  }
  
  public MimeBodyPart generateEncapsulated(MimeBodyPart paramMimeBodyPart) throws SMIMEException {
    return makeEncapsulated(makeContentBodyPart(paramMimeBodyPart));
  }
  
  public MimeBodyPart generateEncapsulated(MimeMessage paramMimeMessage) throws SMIMEException {
    try {
      paramMimeMessage.saveChanges();
    } catch (MessagingException messagingException) {
      throw new SMIMEException("unable to save message", messagingException);
    } 
    return makeEncapsulated(makeContentBodyPart(paramMimeMessage));
  }
  
  public MimeBodyPart generateCertificateManagement() throws SMIMEException {
    try {
      MimeBodyPart mimeBodyPart = new MimeBodyPart();
      mimeBodyPart.setContent(new ContentSigner(null, true), "application/pkcs7-mime; name=smime.p7c; smime-type=certs-only");
      mimeBodyPart.addHeader("Content-Type", "application/pkcs7-mime; name=smime.p7c; smime-type=certs-only");
      mimeBodyPart.addHeader("Content-Disposition", "attachment; filename=\"smime.p7c\"");
      mimeBodyPart.addHeader("Content-Description", "S/MIME Certificate Management Message");
      mimeBodyPart.addHeader("Content-Transfer-Encoding", this.encoding);
      return mimeBodyPart;
    } catch (MessagingException messagingException) {
      throw new SMIMEException("exception putting body part together.", messagingException);
    } 
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
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    hashMap1.put(CMSAlgorithm.MD5, "md5");
    hashMap1.put(CMSAlgorithm.SHA1, "sha-1");
    hashMap1.put(CMSAlgorithm.SHA224, "sha-224");
    hashMap1.put(CMSAlgorithm.SHA256, "sha-256");
    hashMap1.put(CMSAlgorithm.SHA384, "sha-384");
    hashMap1.put(CMSAlgorithm.SHA512, "sha-512");
    hashMap1.put(CMSAlgorithm.GOST3411, "gostr3411-94");
    hashMap1.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
    hashMap1.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");
    RFC5751_MICALGS = Collections.unmodifiableMap(hashMap1);
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    hashMap2.put(CMSAlgorithm.MD5, "md5");
    hashMap2.put(CMSAlgorithm.SHA1, "sha1");
    hashMap2.put(CMSAlgorithm.SHA224, "sha224");
    hashMap2.put(CMSAlgorithm.SHA256, "sha256");
    hashMap2.put(CMSAlgorithm.SHA384, "sha384");
    hashMap2.put(CMSAlgorithm.SHA512, "sha512");
    hashMap2.put(CMSAlgorithm.GOST3411, "gostr3411-94");
    hashMap2.put(CMSAlgorithm.GOST3411_2012_256, "gostr3411-2012-256");
    hashMap2.put(CMSAlgorithm.GOST3411_2012_512, "gostr3411-2012-512");
    RFC3851_MICALGS = Collections.unmodifiableMap(hashMap2);
    STANDARD_MICALGS = RFC5751_MICALGS;
  }
  
  private class ContentSigner implements SMIMEStreamingProcessor {
    private final MimeBodyPart content;
    
    private final boolean encapsulate;
    
    private final boolean noProvider;
    
    ContentSigner(MimeBodyPart param1MimeBodyPart, boolean param1Boolean) {
      this.content = param1MimeBodyPart;
      this.encapsulate = param1Boolean;
      this.noProvider = true;
    }
    
    protected CMSSignedDataStreamGenerator getGenerator() throws CMSException {
      CMSSignedDataStreamGenerator cMSSignedDataStreamGenerator = new CMSSignedDataStreamGenerator();
      Iterator<Store> iterator = SMIMESignedGenerator.this.certStores.iterator();
      while (iterator.hasNext())
        cMSSignedDataStreamGenerator.addCertificates(iterator.next()); 
      iterator = SMIMESignedGenerator.this.crlStores.iterator();
      while (iterator.hasNext())
        cMSSignedDataStreamGenerator.addCRLs(iterator.next()); 
      iterator = SMIMESignedGenerator.this.attrCertStores.iterator();
      while (iterator.hasNext())
        cMSSignedDataStreamGenerator.addAttributeCertificates(iterator.next()); 
      iterator = SMIMESignedGenerator.this.signerInfoGens.iterator();
      while (iterator.hasNext())
        cMSSignedDataStreamGenerator.addSignerInfoGenerator((SignerInfoGenerator)iterator.next()); 
      cMSSignedDataStreamGenerator.addSigners(new SignerInformationStore(SMIMESignedGenerator.this._oldSigners));
      return cMSSignedDataStreamGenerator;
    }
    
    private void writeBodyPart(OutputStream param1OutputStream, MimeBodyPart param1MimeBodyPart) throws IOException, MessagingException {
      if (SMIMEUtil.isMultipartContent((Part)param1MimeBodyPart)) {
        MimeMultipart mimeMultipart;
        Object object = param1MimeBodyPart.getContent();
        if (object instanceof Multipart) {
          Multipart multipart = (Multipart)object;
        } else {
          mimeMultipart = new MimeMultipart(param1MimeBodyPart.getDataHandler().getDataSource());
        } 
        ContentType contentType = new ContentType(mimeMultipart.getContentType());
        String str = "--" + contentType.getParameter("boundary");
        SMIMEUtil.LineOutputStream lineOutputStream = new SMIMEUtil.LineOutputStream(param1OutputStream);
        Enumeration<String> enumeration = param1MimeBodyPart.getAllHeaderLines();
        while (enumeration.hasMoreElements())
          lineOutputStream.writeln(enumeration.nextElement()); 
        lineOutputStream.writeln();
        SMIMEUtil.outputPreamble(lineOutputStream, param1MimeBodyPart, str);
        for (byte b = 0; b < mimeMultipart.getCount(); b++) {
          lineOutputStream.writeln(str);
          writeBodyPart(param1OutputStream, (MimeBodyPart)mimeMultipart.getBodyPart(b));
          lineOutputStream.writeln();
        } 
        lineOutputStream.writeln(str + "--");
      } else {
        CRLFOutputStream cRLFOutputStream;
        if (SMIMEUtil.isCanonicalisationRequired(param1MimeBodyPart, SMIMESignedGenerator.this.defaultContentTransferEncoding))
          cRLFOutputStream = new CRLFOutputStream(param1OutputStream); 
        param1MimeBodyPart.writeTo((OutputStream)cRLFOutputStream);
      } 
    }
    
    public void write(OutputStream param1OutputStream) throws IOException {
      try {
        CMSSignedDataStreamGenerator cMSSignedDataStreamGenerator = getGenerator();
        OutputStream outputStream = cMSSignedDataStreamGenerator.open(param1OutputStream, this.encapsulate);
        if (this.content != null)
          if (!this.encapsulate) {
            writeBodyPart(outputStream, this.content);
          } else {
            CommandMap commandMap = CommandMap.getDefaultCommandMap();
            if (commandMap instanceof MailcapCommandMap)
              this.content.getDataHandler().setCommandMap((CommandMap)MailcapUtil.addCommands((MailcapCommandMap)commandMap)); 
            this.content.writeTo(outputStream);
          }  
        outputStream.close();
        SMIMESignedGenerator.this._digests = cMSSignedDataStreamGenerator.getGeneratedDigests();
      } catch (MessagingException messagingException) {
        throw new IOException(messagingException.toString());
      } catch (CMSException cMSException) {
        throw new IOException(cMSException.toString());
      } 
    }
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/mail/smime/SMIMESignedGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */