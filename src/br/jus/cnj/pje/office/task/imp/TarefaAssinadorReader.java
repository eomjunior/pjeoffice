/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.IJsonTranslator;
/*     */ import br.jus.cnj.pje.office.task.IPjeSignMode;
/*     */ import br.jus.cnj.pje.office.task.IStandardSignature;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*     */ import br.jus.cnj.pje.office.task.IURLOutputDocument;
/*     */ import com.github.signer4j.ISignatureAlgorithm;
/*     */ import com.github.signer4j.ISignatureType;
/*     */ import com.github.signer4j.imp.SignatureAlgorithm;
/*     */ import com.github.signer4j.imp.SignatureType;
/*     */ import com.github.taskresolver4j.ITask;
/*     */ import com.github.taskresolver4j.imp.RequestReader;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TarefaAssinadorReader
/*     */   extends RequestReader<Params, ITarefaAssinador>
/*     */   implements IJsonTranslator
/*     */ {
/*  67 */   static final TarefaAssinadorReader INSTANCE = new TarefaAssinadorReader();
/*     */ 
/*     */   
/*     */   static final class TarefaAssinador
/*     */     implements ITarefaAssinador
/*     */   {
/*     */     private PjeSignMode modo;
/*     */     
/*     */     private String enviarPara;
/*     */     @Deprecated
/*     */     private boolean deslogarKeyStore = true;
/*  78 */     private List<TarefaAssinadorReader.URLOutputDocument> arquivos = new ArrayList<>();
/*     */     
/*  80 */     private SignatureType tipoAssinatura = SignatureType.ATTACHED;
/*     */     
/*  82 */     private SignatureAlgorithm algoritmoHash = SignatureAlgorithm.SHA256withRSA;
/*     */     
/*  84 */     private StandardSignature padraoAssinatura = StandardSignature.CADES;
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public final boolean isDeslogarKeyStore() {
/*  89 */       return this.deslogarKeyStore;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<IPjeSignMode> getModo() {
/*  94 */       return Optional.ofNullable(this.modo);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getEnviarPara() {
/*  99 */       return Strings.optional(this.enviarPara);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<ISignatureAlgorithm> getAlgoritmoHash() {
/* 104 */       return (Optional)Optional.ofNullable(this.algoritmoHash);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<IStandardSignature> getPadraoAssinatura() {
/* 109 */       return Optional.ofNullable(this.padraoAssinatura);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<ISignatureType> getTipoAssinatura() {
/* 114 */       return (Optional)Optional.ofNullable(this.tipoAssinatura);
/*     */     }
/*     */ 
/*     */     
/*     */     public final List<IURLOutputDocument> getArquivos() {
/* 119 */       return (this.arquivos == null) ? Collections.<IURLOutputDocument>emptyList() : Collections.<IURLOutputDocument>unmodifiableList((List)this.arquivos);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class URLOutputDocument extends OutputDocument implements IURLOutputDocument { private String url;
/*     */     
/*     */     public static IURLOutputDocument newInstance(File file) {
/* 126 */       return new URLOutputDocument(file, () -> file.getName() + ".p7s");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private String nome;
/*     */ 
/*     */     
/*     */     private boolean terAtributosAssinados = true;
/*     */ 
/*     */ 
/*     */     
/*     */     private URLOutputDocument(File file) {
/* 139 */       this(file, file::getName);
/*     */     }
/*     */     
/*     */     private URLOutputDocument(File file, Supplier<String> nome) {
/* 143 */       Args.requireNonNull(file, "file is null");
/* 144 */       Args.requireNonNull(nome, "nome is null");
/* 145 */       this.nome = nome.get();
/* 146 */       this.url = file.getAbsolutePath();
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getUrl() {
/* 151 */       return Strings.optional(this.url);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getNome() {
/* 156 */       return Strings.optional(this.nome);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isTerAtributosAssinados() {
/* 161 */       return this.terAtributosAssinados;
/*     */     }
/*     */     private URLOutputDocument() {} }
/*     */   
/*     */   private TarefaAssinadorReader() {
/* 166 */     super(TarefaAssinador.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITask<?> createTask(Params output, ITarefaAssinador pojo) throws IOException {
/* 171 */     IPjeSignMode mode = (IPjeSignMode)pojo.getModo().orElseThrow(() -> new IOException("Parameter 'modoAssinatura' (local/remoto) not found!"));
/*     */ 
/*     */     
/* 174 */     return mode.getTask(output, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toJson(Params input) throws Exception {
/* 179 */     TarefaAssinador ta = new TarefaAssinador();
/* 180 */     ta.modo = PjeSignMode.fromString((String)input.orElse("modo", PjeSignMode.DEFINIDO.getKey()));
/* 181 */     ta.padraoAssinatura = StandardSignature.fromString((String)input.orElse("padraoAssinatura", StandardSignature.CADES.getKey()));
/* 182 */     ta.tipoAssinatura = SignatureType.fromString((String)input.orElse("tipoAssinatura", SignatureType.ATTACHED.getKey()));
/* 183 */     ta.algoritmoHash = SignatureAlgorithm.fromString((String)input.orElse("algoritmoHash", SignatureAlgorithm.SHA256withRSA.getKey()));
/* 184 */     ta.enviarPara = (String)input.getValue("enviarPara");
/* 185 */     ta.arquivos = (List)((List)input.orElse("arquivos", Collections.emptyList())).stream()
/* 186 */       .map(p -> new File(p))
/* 187 */       .map(f -> new URLOutputDocument(f))
/* 188 */       .collect(Collectors.toList());
/* 189 */     input.of("tarefaId", PjeTaskReader.CNJ_ASSINADOR.getId())
/* 190 */       .of("tarefa", ta);
/* 191 */     return PayloadRequestReader.PAYLOAD.toJson(input);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaAssinadorReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */