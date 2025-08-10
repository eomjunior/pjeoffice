/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.core.imp.PjeJsonTaskResponse;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.task.ITarefaTeste;
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.signer4j.ISignerBuilder;
/*     */ import com.github.signer4j.ISimpleSigner;
/*     */ import com.github.signer4j.cert.ICertificatePF;
/*     */ import com.github.signer4j.cert.ICertificatePJ;
/*     */ import com.github.signer4j.cert.imp.CertificateFactory;
/*     */ import com.github.signer4j.imp.SignatureAlgorithm;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.imp.Dates;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjeTestTask
/*     */   extends PjeAbstractTask<ITarefaTeste>
/*     */ {
/*     */   private String message;
/*     */   
/*     */   public PjeTestTask(Params params, ITarefaTeste pojo) {
/*  57 */     super(params, pojo, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean forTest() {
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException {
/*  67 */     ITarefaTeste pojo = getPojoParams();
/*  68 */     this.message = PjeTaskChecker.<String>checkIfPresent(pojo.getMessage(), "mensagem");
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/*  73 */     IPjeToken token = loginToken();
/*     */     try {
/*     */       ISignedData data;
/*     */       try {
/*  77 */         data = ((ISimpleSigner)((ISignerBuilder)token.signerBuilder().usingAlgorithm((ISignatureAlgorithm)SignatureAlgorithm.MD5withRSA).usingLock(tokenLock())).build()).process(this.message);
/*  78 */       } catch (Signer4JException e) {
/*  79 */         throw new TaskException("Não foi possível assinar mensagem de teste", e);
/*     */       } 
/*     */       
/*  82 */       List<JSONObject> chain = new ArrayList<>();
/*     */       
/*  84 */       for (Certificate c : data.getCertificateChain()) {
/*     */         ICertificate certificate;
/*     */         try {
/*  87 */           certificate = CertificateFactory.DEFAULT.create(c, "");
/*  88 */         } catch (CertificateException e) {
/*  89 */           throw new TaskException("Não foi possível ler todas as informações da cadeia de certificados", e);
/*     */         } 
/*     */         
/*  92 */         JSONObject certJson = new JSONObject();
/*     */         
/*  94 */         JSONObject person = new JSONObject();
/*  95 */         person.put("pf.name", certificate.getName());
/*  96 */         certificate.getEmail().ifPresent(value -> person.put("pf.email", value));
/*     */         
/*  98 */         JSONObject infra = new JSONObject();
/*  99 */         infra.put("serial", certificate.getSerial());
/*     */         
/* 101 */         String empty = "(em branco)";
/* 102 */         infra.put("issuer.cn", certificate.getCertificateIssuerDN().getProperty("CN").orElse("(em branco)"));
/* 103 */         infra.put("issuer.ou", certificate.getCertificateIssuerDN().getProperty("OU").orElse("(em branco)"));
/* 104 */         infra.put("issuer.o", certificate.getCertificateIssuerDN().getProperty("O").orElse("(em branco)"));
/* 105 */         infra.put("issuer.c", certificate.getCertificateIssuerDN().getProperty("C").orElse("(em branco)"));
/*     */         
/* 107 */         infra.put("subject.cn", certificate.getCertificateSubjectDN().getProperty("CN").orElse("(em branco)"));
/* 108 */         infra.put("subject.ou", certificate.getCertificateSubjectDN().getProperty("OU").orElse("(em branco)"));
/* 109 */         infra.put("subject.o", certificate.getCertificateSubjectDN().getProperty("O").orElse("(em branco)"));
/* 110 */         infra.put("subject.c", certificate.getCertificateSubjectDN().getProperty("C").orElse("(em branco)"));
/*     */         
/* 112 */         JSONObject validation = new JSONObject();
/* 113 */         validation.put("before.date", Dates.defaultFormat(certificate.getBeforeDate()));
/* 114 */         validation.put("after.date", Dates.defaultFormat(certificate.getAfterDate()));
/*     */         
/* 116 */         JSONObject electoral = new JSONObject();
/*     */         
/* 118 */         certificate.getCertificatePF().ifPresent(pf -> {
/*     */               pf.getCPF().ifPresent(());
/*     */               
/*     */               pf.getBirthDate().ifPresent(());
/*     */               
/*     */               pf.getNis().ifPresent(());
/*     */               pf.getRg().ifPresent(());
/*     */               pf.getCEI().ifPresent(());
/*     */               pf.getIssuingAgencyRg().ifPresent(());
/*     */               pf.getUfIssuingAgencyRg().ifPresent(());
/*     */               pf.getElectoralDocument().ifPresent(());
/*     */               pf.getSectionElectoralDocument().ifPresent(());
/*     */               pf.getZoneElectoralDocument().ifPresent(());
/*     */               pf.getCityElectoralDocument().ifPresent(());
/*     */               pf.getUFElectoralDocument().ifPresent(());
/*     */             });
/* 134 */         certificate.getCertificatePJ().ifPresent(pj -> {
/*     */               pj.getCNPJ().ifPresent(());
/*     */               
/*     */               pj.getBirthDate().ifPresent(());
/*     */               pj.getNis().ifPresent(());
/*     */               pj.getRg().ifPresent(());
/*     */               pj.getCEI().ifPresent(());
/*     */               pj.getResponsibleName().ifPresent(());
/*     */               pj.getResponsibleCPF().ifPresent(());
/*     */               pj.getBusinessName().ifPresent(());
/*     */               pj.getIssuingAgencyRg().ifPresent(());
/*     */               pj.getUfIssuingAgencyRg().ifPresent(());
/*     */             });
/* 147 */         certJson.put("person", person.toMap());
/* 148 */         certJson.put("validation", validation.toMap());
/* 149 */         certJson.put("infra", infra.toMap());
/* 150 */         if (!electoral.isEmpty())
/* 151 */           certJson.put("electoral", electoral.toMap()); 
/* 152 */         chain.add(certJson);
/*     */       } 
/* 154 */       return (ITaskResponse<IPjeResponse>)PjeJsonTaskResponse.success((new JSONObject()).put("output", chain));
/*     */     } finally {
/*     */       
/* 157 */       token.logout();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeTestTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */