/*     */ package com.github.signer4j.cert.oid;
/*     */ 
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OIDReader
/*     */ {
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(OIDReader.class);
/*     */   
/*  49 */   static final Integer HEADER = Integer.valueOf(0);
/*  50 */   static final Integer CONTENT = Integer.valueOf(1);
/*     */   
/*  52 */   private static final Integer OID_TYPE = Integer.valueOf(0);
/*  53 */   private static final Integer EMAIL_TYPE = Integer.valueOf(1);
/*     */ 
/*     */   
/*  56 */   private final Map<String, OIDBasic> oidPool = new HashMap<>();
/*     */   
/*  58 */   private String email = null;
/*     */   
/*     */   public OIDReader(X509Certificate certificate) {
/*  61 */     setup(certificate);
/*     */   }
/*     */ 
/*     */   
/*     */   private final <T extends OIDBasic> Optional<T> get(String oid) {
/*  66 */     return Optional.ofNullable((T)this.oidPool.get(oid));
/*     */   }
/*     */   
/*     */   public final Optional<String> getEmail() {
/*  70 */     return Optional.ofNullable(this.email);
/*     */   }
/*     */   
/*     */   public final boolean isCertificatePF() {
/*  74 */     return get("2.16.76.1.3.1").isPresent();
/*     */   }
/*     */   
/*     */   public final boolean isCertificatePJ() {
/*  78 */     return get("2.16.76.1.3.7").isPresent();
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_1> getOID_2_16_76_1_3_1() {
/*  82 */     return get("2.16.76.1.3.1");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_5> getOID_2_16_76_1_3_5() {
/*  86 */     return get("2.16.76.1.3.5");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_6> getOID_2_16_76_1_3_6() {
/*  90 */     return get("2.16.76.1.3.6");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_2> getOID_2_16_76_1_3_2() {
/*  94 */     return get("2.16.76.1.3.2");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_3> getOID_2_16_76_1_3_3() {
/*  98 */     return get("2.16.76.1.3.3");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_4> getOID_2_16_76_1_3_4() {
/* 102 */     return get("2.16.76.1.3.4");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_7> getOID_2_16_76_1_3_7() {
/* 106 */     return get("2.16.76.1.3.7");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_8> getOID_2_16_76_1_3_8() {
/* 110 */     return get("2.16.76.1.3.8");
/*     */   }
/*     */   
/*     */   public final Optional<OID_1_3_6_1_4_1_311_20_2_3> getOID_1_3_6_1_4_1_311_20_2_3() {
/* 114 */     return get("1.3.6.1.4.1.311.20.2.3");
/*     */   }
/*     */   
/*     */   public final Optional<OID_2_16_76_1_3_9> getOID_2_16_76_1_3_9() {
/* 118 */     return get("2.16.76.1.3.9");
/*     */   }
/*     */   
/*     */   private void setup(X509Certificate certificate) {
/* 122 */     Args.requireNonNull(certificate, "certificate is null");
/* 123 */     Collection<List<?>> alternativeNames = null;
/*     */     try {
/* 125 */       alternativeNames = certificate.getSubjectAlternativeNames();
/* 126 */     } catch (CertificateParsingException e1) {
/* 127 */       LOGGER.warn("Não foi possível ler 'subjectAlternativeNames' do certificado. Provável certificado não pertencente ao ICPBRASIL", e1);
/*     */     } 
/* 129 */     if (alternativeNames == null) {
/*     */       return;
/*     */     }
/* 132 */     for (List<?> nameItem : alternativeNames) {
/* 133 */       if (nameItem.size() != 2) {
/* 134 */         LOGGER.warn("Subject alternative list must have at least 2 entries (keystore bug?)");
/*     */         
/*     */         continue;
/*     */       } 
/* 138 */       Object typeHeader = nameItem.get(HEADER.intValue());
/* 139 */       Object content = nameItem.get(CONTENT.intValue());
/*     */       
/* 141 */       if (!(typeHeader instanceof Number)) {
/* 142 */         LOGGER.warn("typeHeader is not a number (keystore bug?)");
/*     */         
/*     */         continue;
/*     */       } 
/* 146 */       Integer type = Integer.valueOf(((Number)typeHeader).intValue());
/*     */       
/* 148 */       if (EMAIL_TYPE.equals(type)) {
/* 149 */         this.email = content.toString(); continue;
/* 150 */       }  if (OID_TYPE.equals(type))
/*     */         
/*     */         try {
/* 153 */           OIDBasic oid = OIDFactory.create((byte[])content);
/* 154 */           this.oidPool.put(oid.getOid(), oid);
/* 155 */         } catch (Exception e) {
/* 156 */           LOGGER.warn("Não foi possível criar OID a partir do array de bytes", e);
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OIDReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */