/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.IInputDocument64;
/*     */ import br.jus.cnj.pje.office.task.ISignedOutputDocument64;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinadorBase64;
/*     */ import com.github.taskresolver4j.ITask;
/*     */ import com.github.taskresolver4j.imp.RequestReader;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TarefaAssinadorBase64Reader
/*     */   extends RequestReader<Params, ITarefaAssinadorBase64>
/*     */ {
/*  51 */   static final TarefaAssinadorBase64Reader INSTANCE = new TarefaAssinadorBase64Reader();
/*     */ 
/*     */   
/*     */   static final class InputDocument64
/*     */     extends Document
/*     */     implements IInputDocument64
/*     */   {
/*     */     private String hashDoc;
/*     */     
/*     */     private String conteudoBase64;
/*     */     
/*     */     public final Optional<String> getHashDoc() {
/*  63 */       return Strings.optional(this.hashDoc);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getConteudoBase64() {
/*  68 */       return Strings.optional(this.conteudoBase64);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class SignedOutputDocument
/*     */     extends OutputDocument
/*     */     implements ISignedOutputDocument64 {
/*     */     private String hashDoc;
/*     */     private String assinaturaBase64;
/*     */     
/*     */     public SignedOutputDocument(String hash, String signature64) {
/*  79 */       this.hashDoc = (String)Args.requireNonNull(hash, "hashDoc is null");
/*  80 */       this.assinaturaBase64 = (String)Args.requireNonNull(signature64, "assinaturaBase64 is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getHashDoc() {
/*  85 */       return Strings.optional(this.hashDoc);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getAssinaturaBase64() {
/*  90 */       return Strings.optional(this.assinaturaBase64);
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void giveBack(BiConsumer<String, String> consumer) {
/*  95 */       consumer.accept("hashDoc", this.hashDoc);
/*  96 */       consumer.accept("assinaturaBase64", this.assinaturaBase64);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TarefaAssinadorBase64
/*     */     implements ITarefaAssinadorBase64
/*     */   {
/*     */     private String uploadUrl;
/*     */     
/*     */     private String algoritmoAssinatura;
/*     */     
/*     */     @Deprecated
/*     */     private boolean deslogarKeyStore = true;
/* 110 */     private List<TarefaAssinadorBase64Reader.InputDocument64> arquivos = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Optional<String> getAlgoritmoAssinatura() {
/* 116 */       return Strings.optional(this.algoritmoAssinatura);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getUploadUrl() {
/* 121 */       return Strings.optional(this.uploadUrl);
/*     */     }
/*     */ 
/*     */     
/*     */     public final List<IInputDocument64> getArquivos() {
/* 126 */       return (this.arquivos == null) ? Collections.<IInputDocument64>emptyList() : Collections.<IInputDocument64>unmodifiableList((List)this.arquivos);
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public final boolean isDeslogarKeyStore() {
/* 132 */       return this.deslogarKeyStore;
/*     */     }
/*     */   }
/*     */   
/*     */   private TarefaAssinadorBase64Reader() {
/* 137 */     super(TarefaAssinadorBase64.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITask<?> createTask(Params params, ITarefaAssinadorBase64 pojo) throws IOException {
/* 142 */     return (ITask<?>)new PjeBase64SignerTask(params, pojo);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaAssinadorBase64Reader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */