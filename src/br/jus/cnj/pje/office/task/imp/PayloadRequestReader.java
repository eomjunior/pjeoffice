/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.imp.PjeTaskRequest;
/*     */ import br.jus.cnj.pje.office.task.IJsonTranslator;
/*     */ import br.jus.cnj.pje.office.task.IPayload;
/*     */ import com.github.taskresolver4j.ITask;
/*     */ import com.github.taskresolver4j.imp.DefaultTaskRequest;
/*     */ import com.github.taskresolver4j.imp.RequestReader;
/*     */ import com.github.utils4j.imp.Objects;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.beans.Transient;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public final class PayloadRequestReader
/*     */   extends RequestReader<PjeTaskRequest, IPayload>
/*     */   implements IJsonTranslator
/*     */ {
/*  49 */   public static final PayloadRequestReader PAYLOAD = new PayloadRequestReader();
/*     */   
/*     */   public static final Class<?> payloadClass() {
/*  52 */     return Payload.class;
/*     */   }
/*     */   
/*     */   static final class Payload
/*     */     implements IPayload {
/*     */     private String aplicacao;
/*     */     private String servidor;
/*     */     private String sessao;
/*     */     private String codigoSeguranca;
/*     */     private String tarefaId;
/*     */     private String tarefa;
/*     */     
/*     */     public Optional<String> getServidor() {
/*  65 */       return Strings.optional(this.servidor);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> getAplicacao() {
/*  70 */       return Strings.optional(this.aplicacao);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> getSessao() {
/*  75 */       return Strings.optional(this.sessao);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> getCodigoSeguranca() {
/*  80 */       return Strings.optional(this.codigoSeguranca, "bypass");
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> getTarefaId() {
/*  85 */       return Strings.optional(this.tarefaId);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<String> getTarefa() {
/*  90 */       return Strings.optional(this.tarefa);
/*     */     }
/*     */ 
/*     */     
/*     */     @Transient
/*     */     public Optional<String> getOrigin() {
/*  96 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     @Transient
/*     */     public boolean isFromPreflightableRequest() {
/* 102 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private PayloadRequestReader() {
/* 107 */     super(Payload.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ITask<?> createTask(PjeTaskRequest request, IPayload payload) throws IOException {
/* 113 */     payload.getAplicacao().orElseThrow(() -> new IOException("Server did not send 'aplicacao' parameter"));
/*     */ 
/*     */ 
/*     */     
/* 117 */     String taskId = (String)payload.getTarefaId().orElseThrow(() -> new IOException("Server did not send the 'tarefaId' parameter!"));
/*     */ 
/*     */ 
/*     */     
/* 121 */     String taskJson = (String)payload.getTarefa().orElseThrow(() -> new IOException("Server did not send the 'tarefa' parameter!"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     request.of(IPayload.PJE_PAYLOAD_REQUEST_PARAM, payload);
/*     */ 
/*     */ 
/*     */     
/* 130 */     ITask<?> task = (ITask)PjeTaskReader.from(taskId).read(taskJson, (Params)request).get(DefaultTaskRequest.PARAM_TASK).orElseThrow(() -> new IOException("Unabled to create instance of 'idTarefa': " + taskId));
/*     */ 
/*     */ 
/*     */     
/* 134 */     return task;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toJson(Params input) throws Exception {
/* 139 */     Payload payload = new Payload();
/* 140 */     payload.aplicacao = (String)input.orElse("aplicacao", "PjeOffice");
/* 141 */     payload.servidor = (String)input.orElse("servidor", "");
/* 142 */     payload.sessao = (String)input.orElse("sessao", "");
/* 143 */     payload.codigoSeguranca = (String)input.orElse("codigoSeguranca", "");
/* 144 */     payload.tarefaId = (String)input.orElseThrow("tarefaId", () -> new IllegalArgumentException("'tarefaId' não informado."));
/*     */ 
/*     */     
/* 147 */     payload.tarefa = Objects.toJson(input.orElseThrow("tarefa", () -> new IllegalArgumentException("'tarefa' não informado.")));
/*     */ 
/*     */     
/* 150 */     return Objects.toJson(payload);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PayloadRequestReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */