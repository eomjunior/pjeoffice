/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.ASN1String;
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
/*     */ public class CertificateInfo
/*     */ {
/*     */   public static class X500Name
/*     */   {
/*  79 */     public static final ASN1ObjectIdentifier C = new ASN1ObjectIdentifier("2.5.4.6");
/*     */ 
/*     */     
/*  82 */     public static final ASN1ObjectIdentifier O = new ASN1ObjectIdentifier("2.5.4.10");
/*     */ 
/*     */     
/*  85 */     public static final ASN1ObjectIdentifier OU = new ASN1ObjectIdentifier("2.5.4.11");
/*     */ 
/*     */     
/*  88 */     public static final ASN1ObjectIdentifier T = new ASN1ObjectIdentifier("2.5.4.12");
/*     */ 
/*     */     
/*  91 */     public static final ASN1ObjectIdentifier CN = new ASN1ObjectIdentifier("2.5.4.3");
/*     */ 
/*     */     
/*  94 */     public static final ASN1ObjectIdentifier SN = new ASN1ObjectIdentifier("2.5.4.5");
/*     */ 
/*     */     
/*  97 */     public static final ASN1ObjectIdentifier L = new ASN1ObjectIdentifier("2.5.4.7");
/*     */ 
/*     */     
/* 100 */     public static final ASN1ObjectIdentifier ST = new ASN1ObjectIdentifier("2.5.4.8");
/*     */ 
/*     */     
/* 103 */     public static final ASN1ObjectIdentifier SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
/*     */ 
/*     */     
/* 106 */     public static final ASN1ObjectIdentifier GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
/*     */ 
/*     */     
/* 109 */     public static final ASN1ObjectIdentifier INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
/*     */ 
/*     */     
/* 112 */     public static final ASN1ObjectIdentifier GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
/*     */ 
/*     */     
/* 115 */     public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     public static final ASN1ObjectIdentifier EmailAddress = new ASN1ObjectIdentifier("1.2.840.113549.1.9.1");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     public static final ASN1ObjectIdentifier E = EmailAddress;
/*     */ 
/*     */     
/* 129 */     public static final ASN1ObjectIdentifier DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");
/*     */ 
/*     */     
/* 132 */     public static final ASN1ObjectIdentifier UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");
/*     */ 
/*     */     
/* 135 */     public static final Map<ASN1ObjectIdentifier, String> DefaultSymbols = new HashMap<ASN1ObjectIdentifier, String>();
/*     */     
/*     */     static {
/* 138 */       DefaultSymbols.put(C, "C");
/* 139 */       DefaultSymbols.put(O, "O");
/* 140 */       DefaultSymbols.put(T, "T");
/* 141 */       DefaultSymbols.put(OU, "OU");
/* 142 */       DefaultSymbols.put(CN, "CN");
/* 143 */       DefaultSymbols.put(L, "L");
/* 144 */       DefaultSymbols.put(ST, "ST");
/* 145 */       DefaultSymbols.put(SN, "SN");
/* 146 */       DefaultSymbols.put(EmailAddress, "E");
/* 147 */       DefaultSymbols.put(DC, "DC");
/* 148 */       DefaultSymbols.put(UID, "UID");
/* 149 */       DefaultSymbols.put(SURNAME, "SURNAME");
/* 150 */       DefaultSymbols.put(GIVENNAME, "GIVENNAME");
/* 151 */       DefaultSymbols.put(INITIALS, "INITIALS");
/* 152 */       DefaultSymbols.put(GENERATION, "GENERATION");
/*     */     }
/*     */ 
/*     */     
/* 156 */     public Map<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public X500Name(ASN1Sequence seq) {
/* 164 */       Enumeration<ASN1Set> e = seq.getObjects();
/*     */       
/* 166 */       while (e.hasMoreElements()) {
/* 167 */         ASN1Set set = e.nextElement();
/*     */         
/* 169 */         for (int i = 0; i < set.size(); i++) {
/* 170 */           ASN1Sequence s = (ASN1Sequence)set.getObjectAt(i);
/* 171 */           String id = DefaultSymbols.get(s.getObjectAt(0));
/* 172 */           if (id != null) {
/*     */             
/* 174 */             ArrayList<String> vs = this.values.get(id);
/* 175 */             if (vs == null) {
/* 176 */               vs = new ArrayList<String>();
/* 177 */               this.values.put(id, vs);
/*     */             } 
/* 179 */             vs.add(((ASN1String)s.getObjectAt(1)).getString());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public X500Name(String dirName) {
/* 189 */       CertificateInfo.X509NameTokenizer nTok = new CertificateInfo.X509NameTokenizer(dirName);
/*     */       
/* 191 */       while (nTok.hasMoreTokens()) {
/* 192 */         String token = nTok.nextToken();
/* 193 */         int index = token.indexOf('=');
/*     */         
/* 195 */         if (index == -1) {
/* 196 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("badly.formated.directory.string", new Object[0]));
/*     */         }
/*     */         
/* 199 */         String id = token.substring(0, index).toUpperCase();
/* 200 */         String value = token.substring(index + 1);
/* 201 */         ArrayList<String> vs = this.values.get(id);
/* 202 */         if (vs == null) {
/* 203 */           vs = new ArrayList<String>();
/* 204 */           this.values.put(id, vs);
/*     */         } 
/* 206 */         vs.add(value);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getField(String name) {
/* 217 */       List<String> vs = this.values.get(name);
/* 218 */       return (vs == null) ? null : vs.get(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> getFieldArray(String name) {
/* 227 */       return this.values.get(name);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<String, ArrayList<String>> getFields() {
/* 235 */       return this.values;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 243 */       return this.values.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class X509NameTokenizer
/*     */   {
/*     */     private String oid;
/*     */ 
/*     */     
/*     */     private int index;
/*     */     
/* 256 */     private StringBuffer buf = new StringBuffer();
/*     */     
/*     */     public X509NameTokenizer(String oid) {
/* 259 */       this.oid = oid;
/* 260 */       this.index = -1;
/*     */     }
/*     */     
/*     */     public boolean hasMoreTokens() {
/* 264 */       return (this.index != this.oid.length());
/*     */     }
/*     */     
/*     */     public String nextToken() {
/* 268 */       if (this.index == this.oid.length()) {
/* 269 */         return null;
/*     */       }
/*     */       
/* 272 */       int end = this.index + 1;
/* 273 */       boolean quoted = false;
/* 274 */       boolean escaped = false;
/*     */       
/* 276 */       this.buf.setLength(0);
/*     */       
/* 278 */       while (end != this.oid.length()) {
/* 279 */         char c = this.oid.charAt(end);
/*     */         
/* 281 */         if (c == '"') {
/* 282 */           if (!escaped) {
/* 283 */             quoted = !quoted;
/*     */           } else {
/*     */             
/* 286 */             this.buf.append(c);
/*     */           } 
/* 288 */           escaped = false;
/*     */         
/*     */         }
/* 291 */         else if (escaped || quoted) {
/* 292 */           this.buf.append(c);
/* 293 */           escaped = false;
/*     */         }
/* 295 */         else if (c == '\\') {
/* 296 */           escaped = true;
/*     */         } else {
/* 298 */           if (c == ',') {
/*     */             break;
/*     */           }
/*     */           
/* 302 */           this.buf.append(c);
/*     */         } 
/*     */         
/* 305 */         end++;
/*     */       } 
/*     */       
/* 308 */       this.index = end;
/* 309 */       return this.buf.toString().trim();
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
/*     */   public static X500Name getIssuerFields(X509Certificate cert) {
/*     */     try {
/* 322 */       return new X500Name((ASN1Sequence)getIssuer(cert.getTBSCertificate()));
/*     */     }
/* 324 */     catch (Exception e) {
/* 325 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASN1Primitive getIssuer(byte[] enc) {
/*     */     try {
/* 336 */       ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(enc));
/* 337 */       ASN1Sequence seq = (ASN1Sequence)in.readObject();
/* 338 */       return (ASN1Primitive)seq.getObjectAt((seq.getObjectAt(0) instanceof org.bouncycastle.asn1.ASN1TaggedObject) ? 3 : 2);
/*     */     }
/* 340 */     catch (IOException e) {
/* 341 */       throw new ExceptionConverter(e);
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
/*     */   public static X500Name getSubjectFields(X509Certificate cert) {
/*     */     try {
/* 354 */       if (cert != null) {
/* 355 */         return new X500Name((ASN1Sequence)getSubject(cert.getTBSCertificate()));
/*     */       }
/* 357 */     } catch (Exception e) {
/* 358 */       throw new ExceptionConverter(e);
/*     */     } 
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASN1Primitive getSubject(byte[] enc) {
/*     */     try {
/* 370 */       ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(enc));
/* 371 */       ASN1Sequence seq = (ASN1Sequence)in.readObject();
/* 372 */       return (ASN1Primitive)seq.getObjectAt((seq.getObjectAt(0) instanceof org.bouncycastle.asn1.ASN1TaggedObject) ? 5 : 4);
/*     */     }
/* 374 */     catch (IOException e) {
/* 375 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CertificateInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */