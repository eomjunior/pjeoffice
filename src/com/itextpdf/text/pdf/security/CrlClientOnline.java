/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ public class CrlClientOnline
/*     */   implements CrlClient
/*     */ {
/*  71 */   private static final Logger LOGGER = LoggerFactory.getLogger(CrlClientOnline.class);
/*     */ 
/*     */   
/*  74 */   protected List<URL> urls = new ArrayList<URL>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CrlClientOnline() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CrlClientOnline(String... crls) {
/*  87 */     for (String url : crls) {
/*  88 */       addUrl(url);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CrlClientOnline(URL... crls) {
/*  96 */     for (URL url : this.urls) {
/*  97 */       addUrl(url);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CrlClientOnline(Certificate[] chain) {
/* 105 */     for (int i = 0; i < chain.length; i++) {
/* 106 */       X509Certificate cert = (X509Certificate)chain[i];
/* 107 */       LOGGER.info("Checking certificate: " + cert.getSubjectDN());
/*     */       try {
/* 109 */         addUrl(CertificateUtil.getCRLURL(cert));
/* 110 */       } catch (CertificateParsingException e) {
/* 111 */         LOGGER.info("Skipped CRL url (certificate could not be parsed)");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addUrl(String url) {
/*     */     try {
/* 122 */       addUrl(new URL(url));
/* 123 */     } catch (MalformedURLException e) {
/* 124 */       LOGGER.info("Skipped CRL url (malformed): " + url);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addUrl(URL url) {
/* 133 */     if (this.urls.contains(url)) {
/* 134 */       LOGGER.info("Skipped CRL url (duplicate): " + url);
/*     */       return;
/*     */     } 
/* 137 */     this.urls.add(url);
/* 138 */     LOGGER.info("Added CRL url: " + url);
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
/*     */   public Collection<byte[]> getEncoded(X509Certificate checkCert, String url) {
/* 150 */     if (checkCert == null)
/* 151 */       return null; 
/* 152 */     List<URL> urllist = new ArrayList<URL>(this.urls);
/* 153 */     if (urllist.size() == 0) {
/* 154 */       LOGGER.info("Looking for CRL for certificate " + checkCert.getSubjectDN());
/*     */       try {
/* 156 */         if (url == null)
/* 157 */           url = CertificateUtil.getCRLURL(checkCert); 
/* 158 */         if (url == null)
/* 159 */           throw new NullPointerException(); 
/* 160 */         urllist.add(new URL(url));
/* 161 */         LOGGER.info("Found CRL url: " + url);
/*     */       }
/* 163 */       catch (Exception e) {
/* 164 */         LOGGER.info("Skipped CRL url: " + e.getMessage());
/*     */       } 
/*     */     } 
/* 167 */     ArrayList<byte[]> ar = (ArrayList)new ArrayList<byte>();
/* 168 */     for (URL urlt : urllist) {
/*     */       try {
/* 170 */         LOGGER.info("Checking CRL: " + urlt);
/* 171 */         HttpURLConnection con = (HttpURLConnection)urlt.openConnection();
/* 172 */         if (con.getResponseCode() / 100 != 2) {
/* 173 */           throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
/*     */         }
/*     */         
/* 176 */         InputStream inp = (InputStream)con.getContent();
/* 177 */         byte[] buf = new byte[1024];
/* 178 */         ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */         while (true) {
/* 180 */           int n = inp.read(buf, 0, buf.length);
/* 181 */           if (n <= 0)
/*     */             break; 
/* 183 */           bout.write(buf, 0, n);
/*     */         } 
/* 185 */         inp.close();
/* 186 */         ar.add(bout.toByteArray());
/* 187 */         LOGGER.info("Added CRL found at: " + urlt);
/*     */       }
/* 189 */       catch (Exception e) {
/* 190 */         LOGGER.info("Skipped CRL: " + e.getMessage() + " for " + urlt);
/*     */       } 
/*     */     } 
/* 193 */     return (Collection<byte[]>)ar;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CrlClientOnline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */