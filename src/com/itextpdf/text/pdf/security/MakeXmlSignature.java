/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.XmlSignatureAppearance;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.Key;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMStructure;
/*     */ import javax.xml.crypto.dsig.DigestMethod;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.SignedInfo;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.XMLObject;
/*     */ import javax.xml.crypto.dsig.XMLSignatureFactory;
/*     */ import javax.xml.crypto.dsig.dom.DOMSignContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyValue;
/*     */ import javax.xml.crypto.dsig.keyinfo.X509Data;
/*     */ import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.TransformParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.XPathType;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.DOMReference;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.DOMUtils;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.DOMXMLSignature;
/*     */ import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
/*     */ import org.apache.xml.security.utils.Base64;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MakeXmlSignature
/*     */ {
/*     */   private static class EmptyKey
/*     */     implements Key
/*     */   {
/*  97 */     private static EmptyKey instance = new EmptyKey();
/*     */     
/*     */     public static EmptyKey getInstance() {
/* 100 */       return instance;
/*     */     }
/*     */     
/*     */     public String getAlgorithm() {
/* 104 */       return null;
/*     */     }
/*     */     
/*     */     public String getFormat() {
/* 108 */       return null;
/*     */     }
/*     */     
/*     */     public byte[] getEncoded() {
/* 112 */       return new byte[0];
/*     */     }
/*     */   }
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
/*     */   public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, KeyInfo keyInfo) throws GeneralSecurityException, IOException, DocumentException {
/* 128 */     verifyArguments(sap, externalSignature);
/* 129 */     XMLSignatureFactory fac = createSignatureFactory();
/* 130 */     Reference reference = generateContentReference(fac, sap, null);
/* 131 */     String signatureMethod = null;
/* 132 */     if (externalSignature.getEncryptionAlgorithm().equals("RSA")) {
/* 133 */       signatureMethod = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/* 134 */     } else if (externalSignature.getEncryptionAlgorithm().equals("DSA")) {
/* 135 */       signatureMethod = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*     */     } 
/*     */     
/* 138 */     DOMSignedInfo signedInfo = (DOMSignedInfo)fac.newSignedInfo(fac
/* 139 */         .newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac
/* 140 */         .newSignatureMethod(signatureMethod, null), Collections.singletonList(reference));
/*     */     
/* 142 */     sign(fac, externalSignature, sap.getXmlLocator(), signedInfo, null, keyInfo, null);
/*     */     
/* 144 */     sap.close();
/*     */   }
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
/*     */   public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain) throws DocumentException, GeneralSecurityException, IOException {
/* 158 */     signXmlDSig(sap, externalSignature, generateKeyInfo(chain, sap));
/*     */   }
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
/*     */   public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, PublicKey publicKey) throws GeneralSecurityException, DocumentException, IOException {
/* 172 */     signXmlDSig(sap, externalSignature, generateKeyInfo(publicKey));
/*     */   }
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
/*     */   public static void signXades(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain, boolean includeSignaturePolicy) throws GeneralSecurityException, DocumentException, IOException {
/* 189 */     verifyArguments(sap, externalSignature);
/*     */     
/* 191 */     String signatureMethod = null;
/* 192 */     if (externalSignature.getEncryptionAlgorithm().equals("RSA")) {
/* 193 */       signatureMethod = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/* 194 */     } else if (externalSignature.getEncryptionAlgorithm().equals("DSA")) {
/* 195 */       signatureMethod = "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*     */     } 
/* 197 */     String contentReferenceId = "Reference-" + getRandomId();
/* 198 */     String signedPropertiesId = "SignedProperties-" + getRandomId();
/* 199 */     String signatureId = "Signature-" + getRandomId();
/*     */     
/* 201 */     XMLSignatureFactory fac = createSignatureFactory();
/*     */     
/* 203 */     KeyInfo keyInfo = generateKeyInfo(chain, sap);
/* 204 */     String[] signaturePolicy = null;
/* 205 */     if (includeSignaturePolicy) {
/* 206 */       signaturePolicy = new String[2];
/* 207 */       if (signatureMethod.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1")) {
/* 208 */         signaturePolicy[0] = "urn:oid:1.2.840.113549.1.1.5";
/* 209 */         signaturePolicy[1] = "RSA (PKCS #1 v1.5) with SHA-1 signature";
/*     */       } else {
/* 211 */         signaturePolicy[0] = "urn:oid:1.2.840.10040.4.3";
/* 212 */         signaturePolicy[1] = "ANSI X9.57 DSA signature generated with SHA-1 hash (DSA x9.30)";
/*     */       } 
/*     */     } 
/* 215 */     XMLObject xmlObject = generateXadesObject(fac, sap, signatureId, contentReferenceId, signedPropertiesId, signaturePolicy);
/* 216 */     Reference contentReference = generateContentReference(fac, sap, contentReferenceId);
/* 217 */     Reference signedPropertiesReference = generateCustomReference(fac, "#" + signedPropertiesId, "http://uri.etsi.org/01903#SignedProperties", null);
/*     */     
/* 219 */     List<Reference> references = Arrays.asList(new Reference[] { signedPropertiesReference, contentReference });
/*     */ 
/*     */     
/* 222 */     DOMSignedInfo signedInfo = (DOMSignedInfo)fac.newSignedInfo(fac
/* 223 */         .newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null), fac
/* 224 */         .newSignatureMethod(signatureMethod, null), references, null);
/*     */     
/* 226 */     sign(fac, externalSignature, sap.getXmlLocator(), signedInfo, xmlObject, keyInfo, signatureId);
/*     */     
/* 228 */     sap.close();
/*     */   }
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
/*     */   public static void signXadesBes(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain) throws GeneralSecurityException, DocumentException, IOException {
/* 242 */     signXades(sap, externalSignature, chain, false);
/*     */   }
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
/*     */   public static void signXadesEpes(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain) throws GeneralSecurityException, DocumentException, IOException {
/* 256 */     signXades(sap, externalSignature, chain, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void verifyArguments(XmlSignatureAppearance sap, ExternalSignature externalSignature) throws DocumentException {
/* 261 */     if (sap.getXmlLocator() == null)
/* 262 */       throw new DocumentException(MessageLocalization.getComposedMessage("xmllocator.cannot.be.null", new Object[0])); 
/* 263 */     if (!externalSignature.getHashAlgorithm().equals("SHA1")) {
/* 264 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.sha1.hash.algorithm", new Object[0]));
/*     */     }
/* 266 */     if (!externalSignature.getEncryptionAlgorithm().equals("RSA") && !externalSignature.getEncryptionAlgorithm().equals("DSA")) {
/* 267 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.rsa.and.dsa.algorithms", new Object[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Element findElement(NodeList nodes, String localName) {
/* 274 */     for (int i = nodes.getLength() - 1; i >= 0; i--) {
/* 275 */       Node currNode = nodes.item(i);
/* 276 */       if (currNode.getNodeType() == 1 && currNode.getLocalName().equals(localName))
/* 277 */         return (Element)currNode; 
/*     */     } 
/* 279 */     return null;
/*     */   }
/*     */   
/*     */   private static KeyInfo generateKeyInfo(Certificate[] chain, XmlSignatureAppearance sap) {
/* 283 */     Certificate certificate = chain[0];
/* 284 */     sap.setCertificate(certificate);
/* 285 */     DOMKeyInfoFactory dOMKeyInfoFactory = new DOMKeyInfoFactory();
/*     */     
/* 287 */     X509Data x509d = dOMKeyInfoFactory.newX509Data(Collections.singletonList(certificate));
/*     */     
/* 289 */     return dOMKeyInfoFactory.newKeyInfo(Collections.singletonList(x509d));
/*     */   }
/*     */   
/*     */   private static KeyInfo generateKeyInfo(PublicKey publicKey) throws GeneralSecurityException {
/* 293 */     DOMKeyInfoFactory dOMKeyInfoFactory = new DOMKeyInfoFactory();
/* 294 */     KeyValue kv = dOMKeyInfoFactory.newKeyValue(publicKey);
/* 295 */     return dOMKeyInfoFactory.newKeyInfo(Collections.singletonList(kv));
/*     */   }
/*     */   
/*     */   private static String getRandomId() {
/* 299 */     return UUID.randomUUID().toString().substring(24);
/*     */   }
/*     */   
/*     */   private static XMLSignatureFactory createSignatureFactory() {
/* 303 */     return XMLSignatureFactory.getInstance("DOM", (Provider)new XMLDSigRI());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static XMLObject generateXadesObject(XMLSignatureFactory fac, XmlSignatureAppearance sap, String signatureId, String contentReferenceId, String signedPropertiesId, String[] signaturePolicy) throws GeneralSecurityException {
/* 310 */     MessageDigest md = MessageDigest.getInstance("SHA1");
/* 311 */     Certificate cert = sap.getCertificate();
/*     */     
/* 313 */     Document doc = sap.getXmlLocator().getDocument();
/*     */     
/* 315 */     Element QualifyingProperties = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:QualifyingProperties");
/* 316 */     QualifyingProperties.setAttribute("Target", "#" + signatureId);
/* 317 */     Element SignedProperties = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SignedProperties");
/* 318 */     SignedProperties.setAttribute("Id", signedPropertiesId);
/* 319 */     SignedProperties.setIdAttribute("Id", true);
/* 320 */     Element SignedSignatureProperties = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SignedSignatureProperties");
/* 321 */     Element SigningTime = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SigningTime");
/* 322 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
/* 323 */     String result = sdf.format(sap.getSignDate().getTime());
/* 324 */     result = result.substring(0, result.length() - 2).concat(":").concat(result.substring(result.length() - 2));
/* 325 */     SigningTime.appendChild(doc.createTextNode(result));
/* 326 */     SignedSignatureProperties.appendChild(SigningTime);
/* 327 */     Element SigningCertificate = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SigningCertificate");
/* 328 */     Element Cert = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:Cert");
/* 329 */     Element CertDigest = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:CertDigest");
/* 330 */     Element DigestMethod = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
/* 331 */     DigestMethod.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
/* 332 */     CertDigest.appendChild(DigestMethod);
/* 333 */     Element DigestValue = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
/* 334 */     DigestValue.appendChild(doc.createTextNode(Base64.encode(md.digest(cert.getEncoded()))));
/* 335 */     CertDigest.appendChild(DigestValue);
/* 336 */     Cert.appendChild(CertDigest);
/* 337 */     if (cert instanceof X509Certificate) {
/* 338 */       Element IssueSerial = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:IssuerSerial");
/* 339 */       Element X509IssuerName = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509IssuerName");
/* 340 */       X509IssuerName.appendChild(doc.createTextNode(getX509IssuerName((X509Certificate)cert)));
/* 341 */       IssueSerial.appendChild(X509IssuerName);
/* 342 */       Element X509SerialNumber = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber");
/* 343 */       X509SerialNumber.appendChild(doc.createTextNode(getX509SerialNumber((X509Certificate)cert)));
/* 344 */       IssueSerial.appendChild(X509SerialNumber);
/* 345 */       Cert.appendChild(IssueSerial);
/*     */     } 
/* 347 */     SigningCertificate.appendChild(Cert);
/* 348 */     SignedSignatureProperties.appendChild(SigningCertificate);
/* 349 */     if (signaturePolicy != null) {
/* 350 */       Element SignaturePolicyIdentifier = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SignaturePolicyIdentifier");
/* 351 */       Element SignaturePolicyId = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SignaturePolicyId");
/* 352 */       Element SigPolicyId = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SigPolicyId");
/* 353 */       Element Identifier = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:Identifier");
/* 354 */       Identifier.appendChild(doc.createTextNode(signaturePolicy[0]));
/* 355 */       Identifier.setAttribute("Qualifier", "OIDAsURN");
/* 356 */       SigPolicyId.appendChild(Identifier);
/*     */       
/* 358 */       Element Description = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:Description");
/* 359 */       Description.appendChild(doc.createTextNode(signaturePolicy[1]));
/* 360 */       SigPolicyId.appendChild(Description);
/* 361 */       SignaturePolicyId.appendChild(SigPolicyId);
/* 362 */       Element SigPolicyHash = doc.createElementNS("http://uri.etsi.org/01903/v1.3.2#", "xades:SigPolicyHash");
/* 363 */       DigestMethod = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "DigestMethod");
/* 364 */       DigestMethod.setAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
/* 365 */       SigPolicyHash.appendChild(DigestMethod);
/* 366 */       DigestValue = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
/* 367 */       byte[] policyIdContent = getByteArrayOfNode(SigPolicyId);
/* 368 */       DigestValue.appendChild(doc.createTextNode(Base64.encode(md.digest(policyIdContent))));
/* 369 */       SigPolicyHash.appendChild(DigestValue);
/* 370 */       SignaturePolicyId.appendChild(SigPolicyHash);
/* 371 */       SignaturePolicyIdentifier.appendChild(SignaturePolicyId);
/* 372 */       SignedSignatureProperties.appendChild(SignaturePolicyIdentifier);
/*     */     } 
/* 374 */     SignedProperties.appendChild(SignedSignatureProperties);
/* 375 */     Element SignedDataObjectProperties = doc.createElement("xades:SignedDataObjectProperties");
/* 376 */     Element DataObjectFormat = doc.createElement("xades:DataObjectFormat");
/* 377 */     DataObjectFormat.setAttribute("ObjectReference", "#" + contentReferenceId);
/* 378 */     String descr = sap.getDescription();
/* 379 */     if (descr != null) {
/* 380 */       Element Description = doc.createElement("xades:Description");
/* 381 */       Description.appendChild(doc.createTextNode(descr));
/* 382 */       DataObjectFormat.appendChild(Description);
/*     */     } 
/* 384 */     Element MimeType = doc.createElement("xades:MimeType");
/* 385 */     MimeType.appendChild(doc.createTextNode(sap.getMimeType()));
/* 386 */     DataObjectFormat.appendChild(MimeType);
/* 387 */     String enc = sap.getXmlLocator().getEncoding();
/* 388 */     if (enc != null) {
/* 389 */       Element Encoding = doc.createElement("xades:Encoding");
/* 390 */       Encoding.appendChild(doc.createTextNode(enc));
/* 391 */       DataObjectFormat.appendChild(Encoding);
/*     */     } 
/* 393 */     SignedDataObjectProperties.appendChild(DataObjectFormat);
/* 394 */     SignedProperties.appendChild(SignedDataObjectProperties);
/* 395 */     QualifyingProperties.appendChild(SignedProperties);
/*     */     
/* 397 */     XMLStructure content = new DOMStructure(QualifyingProperties);
/* 398 */     return fac.newXMLObject(Collections.singletonList(content), null, null, null);
/*     */   }
/*     */   
/*     */   private static String getX509IssuerName(X509Certificate cert) {
/* 402 */     return cert.getIssuerX500Principal().toString();
/*     */   }
/*     */   
/*     */   private static String getX509SerialNumber(X509Certificate cert) {
/* 406 */     return cert.getSerialNumber().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Reference generateContentReference(XMLSignatureFactory fac, XmlSignatureAppearance sap, String referenceId) throws GeneralSecurityException {
/* 411 */     DigestMethod digestMethodSHA1 = fac.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null);
/*     */     
/* 413 */     List<Transform> transforms = new ArrayList<Transform>();
/* 414 */     transforms.add(fac.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", (TransformParameterSpec)null));
/*     */ 
/*     */     
/* 417 */     XpathConstructor xpathConstructor = sap.getXpathConstructor();
/* 418 */     if (xpathConstructor != null && xpathConstructor.getXpathExpression().length() > 0) {
/* 419 */       XPathFilter2ParameterSpec xpath2Spec = new XPathFilter2ParameterSpec(Collections.singletonList(new XPathType(xpathConstructor.getXpathExpression(), XPathType.Filter.INTERSECT)));
/* 420 */       transforms.add(fac.newTransform("http://www.w3.org/2002/06/xmldsig-filter2", xpath2Spec));
/*     */     } 
/* 422 */     return fac.newReference("", digestMethodSHA1, transforms, null, referenceId);
/*     */   }
/*     */   
/*     */   private static Reference generateCustomReference(XMLSignatureFactory fac, String uri, String type, String id) throws GeneralSecurityException {
/* 426 */     DigestMethod dsDigestMethod = fac.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null);
/* 427 */     return fac.newReference(uri, dsDigestMethod, null, type, id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void sign(XMLSignatureFactory fac, ExternalSignature externalSignature, XmlLocator locator, DOMSignedInfo si, XMLObject xo, KeyInfo ki, String signatureId) throws DocumentException {
/* 433 */     Document doc = locator.getDocument();
/*     */     
/* 435 */     DOMSignContext domSignContext = new DOMSignContext(EmptyKey.getInstance(), doc.getDocumentElement());
/*     */     
/* 437 */     List<XMLObject> objects = null;
/* 438 */     if (xo != null)
/* 439 */       objects = Collections.singletonList(xo); 
/* 440 */     DOMXMLSignature signature = (DOMXMLSignature)fac.newXMLSignature((SignedInfo)si, ki, objects, signatureId, null);
/*     */     
/* 442 */     ByteArrayOutputStream byteRange = new ByteArrayOutputStream();
/*     */     try {
/* 444 */       signature.marshal(domSignContext.getParent(), domSignContext.getNextSibling(), 
/* 445 */           DOMUtils.getSignaturePrefix(domSignContext), domSignContext);
/* 446 */       Element signElement = findElement(doc.getDocumentElement().getChildNodes(), "Signature");
/* 447 */       if (signatureId != null) {
/* 448 */         signElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xades", "http://uri.etsi.org/01903/v1.3.2#");
/*     */       }
/* 450 */       List<DOMReference> references = si.getReferences();
/* 451 */       for (int i = 0; i < references.size(); i++)
/* 452 */         ((DOMReference)references.get(i)).digest(domSignContext); 
/* 453 */       si.canonicalize(domSignContext, byteRange);
/*     */       
/* 455 */       Element signValue = findElement(signElement.getChildNodes(), "SignatureValue");
/*     */ 
/*     */       
/* 458 */       String valueBase64 = Base64.encode(externalSignature.sign(byteRange.toByteArray()));
/*     */       
/* 460 */       signValue.appendChild(doc.createTextNode(valueBase64));
/* 461 */       locator.setDocument(doc);
/* 462 */     } catch (Exception e) {
/* 463 */       throw new DocumentException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] getByteArrayOfNode(Node node) {
/* 468 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*     */     try {
/* 470 */       StreamResult xmlOutput = new StreamResult(new StringWriter());
/* 471 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 472 */       transformerFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*     */       
/*     */       try {
/* 475 */         transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
/* 476 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 479 */         transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
/* 480 */       } catch (Exception exception) {}
/*     */       
/* 482 */       Transformer transformer = transformerFactory.newTransformer();
/* 483 */       transformer.setOutputProperty("omit-xml-declaration", "yes");
/* 484 */       transformer.transform(new DOMSource(node), xmlOutput);
/* 485 */       return xmlOutput.getWriter().toString().getBytes();
/* 486 */     } catch (Exception exception) {
/*     */       
/* 488 */       return stream.toByteArray();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/MakeXmlSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */