/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.IHashedOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.ITarefaAssinadorHash;
/*     */ import com.github.taskresolver4j.ITask;
/*     */ import com.github.taskresolver4j.imp.RequestReader;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
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
/*     */ final class TarefaAssinadorHashReader
/*     */   extends RequestReader<Params, ITarefaAssinadorHash>
/*     */ {
/*  52 */   static final TarefaAssinadorHashReader INSTANCE = new TarefaAssinadorHashReader();
/*     */ 
/*     */   
/*     */   static final class TarefaAssinadorHash
/*     */     implements ITarefaAssinadorHash
/*     */   {
/*     */     private boolean modoTeste;
/*     */     @Deprecated
/*     */     private boolean deslogarKeyStore = true;
/*     */     private String uploadUrl;
/*     */     private String algoritmoAssinatura;
/*  63 */     private List<HashMap<String, String>> arquivos = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public final boolean isModoTeste() {
/*  67 */       return this.modoTeste;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public final boolean isDeslogarKeyStore() {
/*  73 */       return this.deslogarKeyStore;
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getAlgoritmoAssinatura() {
/*  78 */       return Strings.optional(this.algoritmoAssinatura);
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getUploadUrl() {
/*  83 */       return Strings.optional(this.uploadUrl);
/*     */     }
/*     */ 
/*     */     
/*     */     public final List<IHashedOutputDocument> getArquivos() {
/*  88 */       return (this.arquivos == null) ? Collections.<IHashedOutputDocument>emptyList() : (List<IHashedOutputDocument>)this.arquivos
/*     */         
/*  90 */         .stream()
/*  91 */         .map(m -> new TarefaAssinadorHashReader.HashedOutputDocument(m))
/*  92 */         .collect(Collectors.toList());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class HashedOutputDocument
/*     */     extends OutputDocument implements IHashedOutputDocument {
/*  98 */     private Map<String, String> json = new HashMap<>();
/*     */     
/*     */     private HashedOutputDocument(Map<String, String> nameValues) {
/* 101 */       this.json = (Map<String, String>)Args.requireNonNull(nameValues, "nameValues is null");
/*     */     }
/*     */ 
/*     */     
/*     */     public final Optional<String> getId() {
/* 106 */       return Strings.optional(this.json.get("id"));
/*     */     }
/*     */     
/*     */     public final Optional<String> getHash() {
/* 110 */       return Strings.optional(this.json.get("hash"));
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void giveBack(BiConsumer<String, String> consumer) {
/* 115 */       this.json.forEach(consumer);
/*     */     }
/*     */   }
/*     */   
/*     */   private TarefaAssinadorHashReader() {
/* 120 */     super(TarefaAssinadorHash.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITask<?> createTask(Params output, ITarefaAssinadorHash pojo) throws IOException {
/* 125 */     return (ITask<?>)new PjeHashSigningTask(output, pojo);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaAssinadorHashReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */