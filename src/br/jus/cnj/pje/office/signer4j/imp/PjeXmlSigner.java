/*     */ package br.jus.cnj.pje.office.signer4j.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeXmlSigner;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeXmlSignerBuilder;
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.IByteProcessorBuilder;
/*     */ import com.github.signer4j.ICertificateChooser;
/*     */ import com.github.signer4j.IChoice;
/*     */ import com.github.signer4j.IPersonalData;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.imp.ByteProcessor;
/*     */ import com.github.signer4j.imp.SignException;
/*     */ import com.github.signer4j.imp.SignedData;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.signer4j.provider.ProviderInstaller;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.OpenByteArrayOutputStream;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.SignedInfo;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.XMLSignature;
/*     */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*     */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeXmlSigner
/*     */   extends ByteProcessor
/*     */   implements IPjeXmlSigner
/*     */ {
/*     */   private XMLSignatureFactory xmlSignatureFactory;
/*     */   private DocumentBuilder documentBuilder;
/*     */   private KeyInfoFactory keyInfoFactory;
/*     */   private Transformer outputTransformer;
/*     */   private SignedInfo signedInfo;
/*     */   
/*     */   private PjeXmlSigner(ICertificateChooser chooser, Runnable dispose, Optional<ReentrantLock> lock) {
/*  95 */     super(chooser, dispose, lock);
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(byte[] content, int offset, int length) throws Signer4JException {
/* 100 */     Args.requireNonEmpty(content, "content is null");
/* 101 */     Args.requireZeroPositive(offset, "offset is negative");
/* 102 */     Args.requireZeroPositive(length, "length is negative");
/* 103 */     try (InputStream is = new ByteArrayInputStream(content, offset, length)) {
/* 104 */       return process(is);
/* 105 */     } catch (IOException e) {
/* 106 */       throw new Signer4JException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(File content) throws Signer4JException, IOException {
/* 112 */     Args.requireNonNull(content, "content is null");
/* 113 */     try (InputStream is = new BufferedInputStream(new FileInputStream(content), 32768)) {
/* 114 */       return process(is);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ISignedData process(InputStream input) throws Signer4JException {
/* 120 */     Args.requireNonNull(input, "input is null");
/* 121 */     return (ISignedData)invoke(() -> {
/*     */           IChoice choice = select();
/*     */           return SignedData.from(sign(input, createKeyInfo(choice.getCertificate()), choice.getPrivateKey()), (IPersonalData)choice);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyInfo createKeyInfo(Certificate certificate) {
/* 135 */     return this.keyInfoFactory.newKeyInfo(Collections.singletonList(this.keyInfoFactory.newX509Data(Arrays.asList((Object[])new Certificate[] { certificate }))));
/*     */   }
/*     */   
/*     */   private byte[] sign(InputStream input, KeyInfo keyInfo, PrivateKey privateKey) throws Exception {
/* 139 */     Document document = this.documentBuilder.parse(input);
/* 140 */     Element elSignature = document.getDocumentElement();
/* 141 */     DOMSignContext dsc = new DOMSignContext(privateKey, elSignature);
/* 142 */     XMLSignature signature = this.xmlSignatureFactory.newXMLSignature(this.signedInfo, keyInfo);
/* 143 */     signature.sign(dsc);
/* 144 */     return toByteArray(document);
/*     */   }
/*     */   
/* 147 */   private static final String[] SEARCH_CLEANER = new String[] { "\n", "\r", " standalone=\"no\"" };
/* 148 */   private static final String[] REPLACE_CLEANER = new String[] { Strings.empty(), Strings.empty(), Strings.empty() };
/*     */   
/*     */   private byte[] toByteArray(Document document) throws Exception {
/* 151 */     try (OpenByteArrayOutputStream out = new OpenByteArrayOutputStream()) {
/* 152 */       this.outputTransformer.transform(new DOMSource(document), new StreamResult((OutputStream)out));
/* 153 */       String content = Strings.replaceEach(
/* 154 */           Strings.trim(out.asString(IConstants.DEFAULT_CHARSET)), SEARCH_CLEANER, REPLACE_CLEANER);
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (content.isEmpty()) {
/* 159 */         throw new SignException("Arquivo final não pôde ser assinado (vazio)");
/*     */       }
/* 161 */       return content.getBytes(IConstants.DEFAULT_CHARSET);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class Builder
/*     */     implements IPjeXmlSignerBuilder
/*     */   {
/*     */     private final ICertificateChooser chooser;
/*     */     private final Runnable dispose;
/* 171 */     private Optional<ReentrantLock> lock = Optional.empty();
/*     */     
/* 173 */     private String hashPath = "http://www.w3.org/2001/04/xmlenc#sha256";
/*     */     
/* 175 */     private String asymetricPath = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/*     */     
/* 177 */     private String c14nTransformPath = "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
/*     */     
/* 179 */     private String envelopedTransformPath = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
/*     */     
/*     */     public Builder(ICertificateChooser chooser, Runnable dispose) {
/* 182 */       this.chooser = (ICertificateChooser)Args.requireNonNull(chooser, "chooser is null");
/* 183 */       this.dispose = (Runnable)Args.requireNonNull(dispose, "dispose is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final IPjeXmlSignerBuilder usingLock(ReentrantLock lock) {
/* 188 */       this.lock = Optional.ofNullable(lock);
/* 189 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPjeXmlSignerBuilder usingHashPath(String hashPath) {
/* 194 */       this.hashPath = Strings.needText(hashPath, this.hashPath);
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPjeXmlSignerBuilder usingAsymetricPath(String asymetricPath) {
/* 200 */       this.asymetricPath = Strings.needText(asymetricPath, this.asymetricPath);
/* 201 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPjeXmlSignerBuilder usingC14nTransformPath(String c14nTransformPath) {
/* 206 */       this.c14nTransformPath = Strings.needText(c14nTransformPath, this.c14nTransformPath);
/* 207 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPjeXmlSignerBuilder usingEnvelopedTransform(String envelopedTransform) {
/* 212 */       this.envelopedTransformPath = Strings.needText(envelopedTransform, this.envelopedTransformPath);
/* 213 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public final IPjeXmlSigner build() {
/* 218 */       PjeXmlSigner signer = new PjeXmlSigner(this.chooser, this.dispose, this.lock);
/* 219 */       Throwables.runtime(() -> { XMLSignatureFactory xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", ProviderInstaller.JSR105.install()); KeyInfoFactory keyInfoFactory = xmlSignatureFactory.getKeyInfoFactory(); Transform envelopedTransform = xmlSignatureFactory.newTransform(this.envelopedTransformPath, (TransformParameterSpec)null); Transform c14nTransform = xmlSignatureFactory.newTransform(this.c14nTransformPath, (TransformParameterSpec)null); Reference ref = xmlSignatureFactory.newReference("", xmlSignatureFactory.newDigestMethod(this.hashPath, null), Arrays.asList(new Transform[] { envelopedTransform, c14nTransform }, ), null, null); SignedInfo signedInfo = xmlSignatureFactory.newSignedInfo(xmlSignatureFactory.newCanonicalizationMethod(this.c14nTransformPath, (C14NMethodParameterSpec)null), xmlSignatureFactory.newSignatureMethod(this.asymetricPath, null), Collections.singletonList(ref)); DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); documentBuilderFactory.setNamespaceAware(true); signer.xmlSignatureFactory = xmlSignatureFactory; signer.keyInfoFactory = keyInfoFactory; signer.documentBuilder = documentBuilderFactory.newDocumentBuilder(); signer.outputTransformer = TransformerFactory.newInstance().newTransformer(); signer.signedInfo = signedInfo; }"Não foi possível instanciar PjeXmlSigner");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 254 */       return signer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/imp/PjeXmlSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */