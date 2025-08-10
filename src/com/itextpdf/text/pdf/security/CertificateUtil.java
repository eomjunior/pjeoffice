/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.CRLException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERIA5String;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.x509.CRLDistPoint;
/*     */ import org.bouncycastle.asn1.x509.DistributionPoint;
/*     */ import org.bouncycastle.asn1.x509.DistributionPointName;
/*     */ import org.bouncycastle.asn1.x509.Extension;
/*     */ import org.bouncycastle.asn1.x509.GeneralName;
/*     */ import org.bouncycastle.asn1.x509.GeneralNames;
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
/*     */ public class CertificateUtil
/*     */ {
/*     */   public static CRL getCRL(X509Certificate certificate) throws CertificateException, CRLException, IOException {
/*  90 */     return getCRL(getCRLURL(certificate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCRLURL(X509Certificate certificate) throws CertificateParsingException {
/*     */     ASN1Primitive obj;
/*     */     try {
/* 103 */       obj = getExtensionValue(certificate, Extension.cRLDistributionPoints.getId());
/* 104 */     } catch (IOException e) {
/* 105 */       obj = null;
/*     */     } 
/* 107 */     if (obj == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     CRLDistPoint dist = CRLDistPoint.getInstance(obj);
/* 111 */     DistributionPoint[] dists = dist.getDistributionPoints();
/* 112 */     for (DistributionPoint p : dists) {
/* 113 */       DistributionPointName distributionPointName = p.getDistributionPoint();
/* 114 */       if (0 == distributionPointName.getType()) {
/*     */ 
/*     */         
/* 117 */         GeneralNames generalNames = (GeneralNames)distributionPointName.getName();
/* 118 */         GeneralName[] names = generalNames.getNames(); GeneralName[] arrayOfGeneralName1; int i; byte b;
/* 119 */         for (arrayOfGeneralName1 = names, i = arrayOfGeneralName1.length, b = 0; b < i; ) { GeneralName name = arrayOfGeneralName1[b];
/* 120 */           if (name.getTagNo() != 6) {
/*     */             b++; continue;
/*     */           } 
/* 123 */           DERIA5String derStr = DERIA5String.getInstance((ASN1TaggedObject)name.toASN1Primitive(), false);
/* 124 */           return derStr.getString(); }
/*     */       
/*     */       } 
/* 127 */     }  return null;
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
/*     */   public static CRL getCRL(String url) throws IOException, CertificateException, CRLException {
/* 139 */     if (url == null)
/* 140 */       return null; 
/* 141 */     InputStream is = (new URL(url)).openStream();
/* 142 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 143 */     return cf.generateCRL(is);
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
/*     */   public static String getOCSPURL(X509Certificate certificate) {
/*     */     try {
/* 157 */       ASN1Primitive obj = getExtensionValue(certificate, Extension.authorityInfoAccess.getId());
/* 158 */       if (obj == null) {
/* 159 */         return null;
/*     */       }
/* 161 */       ASN1Sequence AccessDescriptions = (ASN1Sequence)obj;
/* 162 */       for (int i = 0; i < AccessDescriptions.size(); i++) {
/* 163 */         ASN1Sequence AccessDescription = (ASN1Sequence)AccessDescriptions.getObjectAt(i);
/* 164 */         if (AccessDescription.size() == 2)
/*     */         {
/*     */           
/* 167 */           if (AccessDescription.getObjectAt(0) instanceof ASN1ObjectIdentifier) {
/* 168 */             ASN1ObjectIdentifier id = (ASN1ObjectIdentifier)AccessDescription.getObjectAt(0);
/* 169 */             if ("1.3.6.1.5.5.7.48.1".equals(id.getId())) {
/* 170 */               ASN1Primitive description = (ASN1Primitive)AccessDescription.getObjectAt(1);
/* 171 */               String AccessLocation = getStringFromGeneralName(description);
/* 172 */               if (AccessLocation == null) {
/* 173 */                 return "";
/*     */               }
/*     */               
/* 176 */               return AccessLocation;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/* 181 */     } catch (IOException e) {
/* 182 */       return null;
/*     */     } 
/* 184 */     return null;
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
/*     */   public static String getTSAURL(X509Certificate certificate) {
/* 196 */     byte[] der = certificate.getExtensionValue("1.2.840.113583.1.1.9.1");
/* 197 */     if (der == null) {
/* 198 */       return null;
/*     */     }
/*     */     try {
/* 201 */       ASN1Primitive asn1obj = ASN1Primitive.fromByteArray(der);
/* 202 */       DEROctetString octets = (DEROctetString)asn1obj;
/* 203 */       asn1obj = ASN1Primitive.fromByteArray(octets.getOctets());
/* 204 */       ASN1Sequence asn1seq = ASN1Sequence.getInstance(asn1obj);
/* 205 */       return getStringFromGeneralName(asn1seq.getObjectAt(1).toASN1Primitive());
/* 206 */     } catch (IOException e) {
/* 207 */       return null;
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
/*     */   private static ASN1Primitive getExtensionValue(X509Certificate certificate, String oid) throws IOException {
/* 220 */     byte[] bytes = certificate.getExtensionValue(oid);
/* 221 */     if (bytes == null) {
/* 222 */       return null;
/*     */     }
/* 224 */     ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
/* 225 */     ASN1OctetString octs = (ASN1OctetString)aIn.readObject();
/* 226 */     aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
/* 227 */     return aIn.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getStringFromGeneralName(ASN1Primitive names) throws IOException {
/* 237 */     ASN1TaggedObject taggedObject = (ASN1TaggedObject)names;
/* 238 */     return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets(), "ISO-8859-1");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CertificateUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */