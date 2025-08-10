/*     */ package com.github.signer4j.cert.imp;
/*     */ 
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.cert.ICertificatePF;
/*     */ import com.github.signer4j.cert.ICertificatePJ;
/*     */ import com.github.signer4j.cert.IDistinguishedName;
/*     */ import com.github.signer4j.cert.ISubjectAlternativeNames;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Certificates;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
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
/*     */ class BrazilianCertificate
/*     */   implements ICertificate
/*     */ {
/*     */   private final X509Certificate certificate;
/*  67 */   private IDistinguishedName certificateFor = null;
/*  68 */   private IDistinguishedName certificateFrom = null;
/*  69 */   private KeyUsage keyUsage = null;
/*     */   
/*  71 */   private ISubjectAlternativeNames subjectAlternativeNames = null;
/*     */   
/*     */   private String aliasName;
/*     */   
/*     */   BrazilianCertificate(InputStream is, String aliasName) throws CertificateException {
/*  76 */     this(Certificates.create(is), aliasName);
/*     */   }
/*     */   
/*     */   BrazilianCertificate(X509Certificate certificate, String aliasName) {
/*  80 */     this.certificate = (X509Certificate)Args.requireNonNull(certificate, "certificate is null");
/*  81 */     this.aliasName = aliasName;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getAlias() {
/*  86 */     return Optional.ofNullable(this.aliasName);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setAlias(String aliasName) {
/*  91 */     this.aliasName = aliasName;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Date getAfterDate() {
/*  96 */     return this.certificate.getNotAfter();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Date getBeforeDate() {
/* 101 */     return this.certificate.getNotBefore();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isExpired() {
/* 106 */     return (System.currentTimeMillis() > getAfterDate().getTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getManufacturer() {
/* 111 */     return getCertificateIssuerDN().getFullName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getSerial() {
/* 116 */     return toString(this.certificate.getSerialNumber());
/*     */   }
/*     */ 
/*     */   
/*     */   public final X509Certificate toX509() {
/* 121 */     return this.certificate;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDistinguishedName getCertificateIssuerDN() {
/* 126 */     if (this.certificateFrom == null) {
/* 127 */       this.certificateFrom = new DistinguishedName(this.certificate.getIssuerDN().getName());
/*     */     }
/* 129 */     return this.certificateFrom;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IDistinguishedName getCertificateSubjectDN() {
/* 134 */     if (this.certificateFor == null) {
/* 135 */       this.certificateFor = new DistinguishedName(this.certificate.getSubjectDN().getName());
/*     */     }
/* 137 */     return this.certificateFor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificatePF> getCertificatePF() {
/* 142 */     if (getSubjectAlternativeNames() == null) {
/* 143 */       return Optional.empty();
/*     */     }
/* 145 */     return getSubjectAlternativeNames().getCertificatePF();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificatePJ> getCertificatePJ() {
/* 150 */     if (getSubjectAlternativeNames() == null) {
/* 151 */       return Optional.empty();
/*     */     }
/* 153 */     return getSubjectAlternativeNames().getCertificatePJ();
/*     */   }
/*     */ 
/*     */   
/*     */   public final KeyUsage getKeyUsage() {
/* 158 */     if (this.keyUsage == null) {
/* 159 */       this.keyUsage = new KeyUsage(this.certificate);
/*     */     }
/* 161 */     return this.keyUsage;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ISubjectAlternativeNames getSubjectAlternativeNames() {
/* 166 */     if (this.subjectAlternativeNames == null) {
/* 167 */       this.subjectAlternativeNames = new SubjectAlternativeNames(this.certificate);
/*     */     }
/* 169 */     return this.subjectAlternativeNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getEmail() {
/* 174 */     if (getSubjectAlternativeNames() == null) {
/* 175 */       return Optional.empty();
/*     */     }
/* 177 */     return getSubjectAlternativeNames().getEmail();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCertificatePJ() {
/* 182 */     if (getSubjectAlternativeNames() == null) {
/* 183 */       return false;
/*     */     }
/* 185 */     return getSubjectAlternativeNames().hasCertificatePJ();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCertificatePF() {
/* 190 */     if (getSubjectAlternativeNames() == null) {
/* 191 */       return false;
/*     */     }
/* 193 */     return getSubjectAlternativeNames().hasCertificatePF();
/*     */   }
/*     */   
/*     */   private String toString(BigInteger i) {
/* 197 */     if (i == null) {
/* 198 */       return null;
/*     */     }
/* 200 */     String out = i.toString(16);
/* 201 */     if (out.length() % 2 == 1) {
/* 202 */       out = "0" + out;
/*     */     }
/* 204 */     return out.toUpperCase();
/*     */   }
/*     */   
/*     */   public final ASN1Primitive getExtensionValue(String oid) {
/*     */     try {
/* 209 */       byte[] extensionValue = this.certificate.getExtensionValue(oid);
/* 210 */       if (extensionValue == null) {
/* 211 */         return null;
/*     */       }
/* 213 */       try (ASN1InputStream ie = new ASN1InputStream(extensionValue)) {
/* 214 */         DEROctetString oct = (DEROctetString)ie.readObject();
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 219 */     catch (Exception e) {
/* 220 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*     */     try {
/* 227 */       String name = getCertificateSubjectDN().getProperty("CN").orElse(Strings.empty());
/*     */       
/* 229 */       int pos = name.indexOf(':');
/* 230 */       if (pos > 0) {
/* 231 */         return name.substring(0, pos);
/*     */       }
/* 233 */       return name;
/* 234 */     } catch (Exception e) {
/* 235 */       e.printStackTrace();
/* 236 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<String> getCRLDistributionPoint() {
/* 242 */     ASN1Primitive primitive = getExtensionValue(Extension.cRLDistributionPoints.getId());
/* 243 */     if (primitive == null) {
/* 244 */       return Collections.emptyList();
/*     */     }
/* 246 */     List<String> crl = new ArrayList<>();
/* 247 */     CRLDistPoint crlDistPoint = CRLDistPoint.getInstance(primitive);
/* 248 */     DistributionPoint[] distributionPoints = crlDistPoint.getDistributionPoints();
/*     */     
/* 250 */     for (DistributionPoint distributionPoint : distributionPoints) {
/* 251 */       DistributionPointName dpn = distributionPoint.getDistributionPoint();
/* 252 */       if (dpn != null && 
/* 253 */         0 == dpn.getType()) {
/* 254 */         GeneralName[] genNames = GeneralNames.getInstance(dpn.getName()).getNames();
/* 255 */         for (GeneralName genName : genNames) {
/* 256 */           if (6 == genName.getTagNo()) {
/* 257 */             String url = DERIA5String.getInstance(genName.getName()).getString();
/* 258 */             crl.add(url);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 264 */     return crl;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 269 */     StringBuilder sb = new StringBuilder(0);
/* 270 */     SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
/* 271 */     sb.append(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n");
/* 272 */     append(sb, "alias", this.aliasName);
/* 273 */     append(sb, "issuerDN", getCertificateIssuerDN());
/* 274 */     append(sb, "subjectDN", getCertificateSubjectDN());
/* 275 */     append(sb, "serialNumber", getSerial());
/* 276 */     append(sb, "valid.from", formatter.format(getBeforeDate()));
/* 277 */     append(sb, "valid.to", formatter.format(getAfterDate()));
/* 278 */     append(sb, "name", getName());
/* 279 */     append(sb, "is.pf", Boolean.valueOf(hasCertificatePF()));
/* 280 */     if (hasCertificatePJ()) {
/* 281 */       ICertificatePJ pj = getCertificatePJ().get();
/* 282 */       append(sb, "cnpj", pj.getCNPJ());
/* 283 */       append(sb, "responsible", pj.getResponsibleName());
/* 284 */       append(sb, "business.name", pj.getBusinessName());
/* 285 */       append(sb, "nis", pj.getNis());
/* 286 */       append(sb, "cei", pj.getCEI());
/*     */     } 
/* 288 */     append(sb, "is.pj", Optional.of(Boolean.toString(hasCertificatePJ())));
/* 289 */     if (hasCertificatePF()) {
/* 290 */       ICertificatePF pf = getCertificatePF().get();
/* 291 */       append(sb, "cpf", pf.getCPF());
/* 292 */       append(sb, "birth.date", pf.getBirthDate().orElse(null));
/* 293 */       append(sb, "email", getEmail());
/* 294 */       append(sb, "pis", pf.getNis());
/* 295 */       append(sb, "rg", pf.getRg());
/* 296 */       append(sb, "rg.uf", pf.getUfIssuingAgencyRg());
/* 297 */       append(sb, "rg.issuing.agency", pf.getIssuingAgencyRg());
/* 298 */       append(sb, "voter.document", pf.getElectoralDocument());
/* 299 */       append(sb, "voter.city", pf.getCityElectoralDocument());
/* 300 */       append(sb, "voter.uf", pf.getUFElectoralDocument());
/* 301 */       append(sb, "zone", pf.getZoneElectoralDocument());
/* 302 */       append(sb, "section", pf.getSectionElectoralDocument());
/* 303 */       append(sb, "cei", pf.getCEI());
/*     */     } 
/* 305 */     sb.append("clr: ").append("\n");
/* 306 */     getCRLDistributionPoint().forEach(crl -> sb.append(' ').append(crl).append("\n"));
/* 307 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static StringBuilder append(StringBuilder sb, String field, Optional<String> value) {
/* 311 */     return append(sb, field, value.orElse(""));
/*     */   }
/*     */   
/*     */   private static StringBuilder append(StringBuilder sb, String field, Object value) {
/* 315 */     return sb.append(field).append(": ").append(Optional.<T>ofNullable((T)value).orElse((T)"").toString()).append("\n");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/BrazilianCertificate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */