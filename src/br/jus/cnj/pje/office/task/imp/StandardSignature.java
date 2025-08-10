/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeToken;
/*     */ import br.jus.cnj.pje.office.signer4j.IPjeXmlSignerBuilder;
/*     */ import br.jus.cnj.pje.office.task.ICosignChecker;
/*     */ import br.jus.cnj.pje.office.task.IStandardSignature;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonValue;
/*     */ import com.github.signer4j.IByteProcessor;
/*     */ import com.github.signer4j.IByteProcessorBuilder;
/*     */ import com.github.signer4j.ICMSSigner;
/*     */ import com.github.signer4j.ICMSSignerBuilder;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignatureType;
/*     */ import com.github.signer4j.imp.SignatureAlgorithm;
/*     */ import com.github.signer4j.imp.SignatureType;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Objects;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum StandardSignature
/*     */   implements IStandardSignature
/*     */ {
/*  56 */   XADES(".xml", "application/xml", IConstants.UTF_8)
/*     */   {
/*     */     public IStandardSignature check(ITarefaAssinador params) throws TaskException {
/*  59 */       PjeTaskChecker.checkIfNull(params, "params is null");
/*     */       
/*  61 */       Optional<ISignatureType> st = params.getTipoAssinatura();
/*  62 */       PjeTaskChecker.throwIf((st.isPresent() && SignatureType.DETACHED.equals(st.get())), "Assinatura %s em XADES ainda não suportada pelo assinador. Use ATTACHED", new Object[] { SignatureType.DETACHED });
/*     */ 
/*     */       
/*  65 */       Optional<ISignatureAlgorithm> sa = params.getAlgoritmoHash();
/*  66 */       PjeTaskChecker.throwIf((sa.isPresent() && !SignatureAlgorithm.SHA256withRSA.equals(sa.get())), "Algorítimo %s em XADES ainda não é suportado pelo assinador. Use SHA256withRSA", new Object[] { sa
/*  67 */             .get() });
/*     */       
/*  69 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IPjeXmlSignerBuilder processorBuilder(IPjeToken token, ITarefaAssinador params) {
/*  74 */       Args.requireNonNull(token, "token is null");
/*     */       
/*  76 */       return token.xmlSignerBuilder();
/*     */     }
/*     */ 
/*     */     
/*     */     public ICosignChecker cosignChecker() {
/*  81 */       return CosignChecker.XML;
/*     */     }
/*     */   },
/*     */   
/*  85 */   CADES(".p7s", "application/pkcs7-signature")
/*     */   {
/*     */     public IStandardSignature check(ITarefaAssinador params) throws TaskException {
/*  88 */       PjeTaskChecker.checkIfNull(params, "params is null");
/*  89 */       PjeTaskChecker.checkIfPresent(params.getAlgoritmoHash(), "algoritmoHash");
/*  90 */       PjeTaskChecker.checkIfPresent(params.getTipoAssinatura(), "tipoAssinatura");
/*  91 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ICMSSignerBuilder processorBuilder(IPjeToken token, ITarefaAssinador params) {
/*  96 */       Args.requireNonNull(token, "token is null");
/*  97 */       Args.requireNonNull(params, "param is null");
/*  98 */       return ((ICMSSignerBuilder)((ICMSSignerBuilder)token.cmsSignerBuilder()
/*  99 */         .usingSignatureAlgorithm(params.getAlgoritmoHash().get()))
/* 100 */         .usingSignatureType(params.getTipoAssinatura().get()))
/* 101 */         .usingConfig((p, o) -> ((ICMSSigner)p).usingAttributes(((Boolean)o).booleanValue()));
/*     */     }
/*     */ 
/*     */     
/*     */     public ICosignChecker cosignChecker() {
/* 106 */       return CosignChecker.CMS;
/*     */     }
/*     */   },
/*     */   
/* 110 */   ENVELOPED(XADES.extension, XADES.mimeType, XADES.charset)
/*     */   {
/*     */     
/*     */     public IByteProcessorBuilder<?, ?> processorBuilder(IPjeToken token, ITarefaAssinador params)
/*     */     {
/* 115 */       return XADES.processorBuilder(token, params);
/*     */     }
/*     */ 
/*     */     
/*     */     public IStandardSignature check(ITarefaAssinador params) throws TaskException {
/* 120 */       return XADES.check(params);
/*     */     }
/*     */ 
/*     */     
/*     */     public ICosignChecker cosignChecker() {
/* 125 */       return XADES.cosignChecker();
/*     */     }
/*     */   };
/*     */ 
/*     */   
/*     */   private final String extension;
/*     */   
/*     */   private final String mimeType;
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */   
/*     */   StandardSignature(String extension, String mimeType, Charset charset) {
/* 138 */     this.extension = (String)Args.requireNonNull(extension, "extension is null");
/* 139 */     this.mimeType = (String)Args.requireNonNull(mimeType, "mimeType is null");
/* 140 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   @JsonCreator
/*     */   public static StandardSignature fromString(String key) {
/* 145 */     return (key == null) ? CADES : valueOf(key.toUpperCase());
/*     */   }
/*     */   
/*     */   @JsonValue
/*     */   public String getKey() {
/* 150 */     return name().toLowerCase();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getExtension() {
/* 155 */     return this.extension;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMineType() {
/* 160 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCharset() {
/* 165 */     return Objects.toString(this.charset, null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/StandardSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */