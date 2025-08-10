/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.task.ITarefaImpressao;
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskException;
/*     */ import com.github.taskresolver4j.exception.TaskParameterInvalidException;
/*     */ import com.github.utils4j.gui.imp.Dialogs;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.PrintFailException;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PjePrintingTask
/*     */   extends PjeAbstractTask<ITarefaImpressao>
/*     */ {
/*     */   private List<String> conteudo;
/*     */   private Port porta;
/*     */   
/*     */   private enum Port
/*     */   {
/*  60 */     LPT1("LPT1"),
/*  61 */     LPT2("LPT2"),
/*  62 */     LPT3("LPT3"),
/*  63 */     LPT4("LPT4"),
/*  64 */     LPT5("LPT5"),
/*  65 */     COM1("COM1"),
/*  66 */     COM2("COM2"),
/*  67 */     COM3("COM3"),
/*  68 */     COM4("COM4"),
/*  69 */     COM5("COM5");
/*     */     
/*     */     private final String key;
/*     */     
/*     */     Port(String key) {
/*  74 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public final String toString() {
/*  79 */       return this.key;
/*     */     }
/*     */     
/*     */     static Port of(String port) throws TaskParameterInvalidException {
/*  83 */       port = Strings.trim(port).toUpperCase();
/*  84 */       for (Port p : values()) {
/*  85 */         if (port.equals(p.key))
/*  86 */           return p; 
/*  87 */       }  throw new TaskParameterInvalidException("Porta '" + port + "' é inválida!");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PjePrintingTask(Params request, ITarefaImpressao pojo) {
/*  96 */     super(request, pojo);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void validateTaskParams() throws TaskException, InterruptedException {
/* 101 */     if (!Jvms.isWindows())
/* 102 */       throw showFail("Rotina disponível apenas para ambiente Windows"); 
/* 103 */     ITarefaImpressao pojo = getPojoParams();
/* 104 */     this.conteudo = PjeTaskChecker.checkIfNotEmpty(pojo.getConteudo(), "conteudo");
/* 105 */     this.porta = Port.of(PjeTaskChecker.<String>checkIfPresent(pojo.getPorta(), "porta"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onBeforeDoGet() throws TaskException, InterruptedException {
/* 112 */     Dialogs.Choice choice = Dialogs.getBoolean("Confirma a impressão de dados?", "Confirmação", false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     if (choice != Dialogs.Choice.YES) {
/* 118 */       throwCancel(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 124 */     int total = this.conteudo.size();
/*     */     
/* 126 */     if (total == 0) {
/* 127 */       throw showFail("Não foi informado um conteúdo para impressão");
/*     */     }
/* 129 */     AtomicInteger printCount = new AtomicInteger(0);
/*     */     
/* 131 */     IProgressView iProgressView = getProgress();
/*     */     
/* 133 */     ExecutorService service = Executors.newFixedThreadPool(1);
/*     */ 
/*     */     
/*     */     try {
/* 137 */       Future<Optional<Exception>> task = service.submit(() -> {
/*     */             try (PrintWriter printer = new PrintWriter(new FileOutputStream(this.porta.name() + ":"))) {
/*     */               progress.info("Porta desbloqueada", new Object[0]);
/*     */               
/*     */               for (int i = 0; i < total; i++) {
/*     */                 String message = Strings.trim(this.conteudo.get(i), "empty");
/*     */                 
/*     */                 progress.step("Enviando conteúdo [%s]:%s", new Object[] { Integer.valueOf(i), message });
/*     */                 
/*     */                 progress.info("Aguardando resposta da porta '%s' (seja paciente...)", new Object[] { this.porta });
/*     */                 long start = System.currentTimeMillis();
/*     */                 printer.println(message);
/*     */                 printer.flush();
/*     */                 long time = System.currentTimeMillis() - start;
/*     */                 printCount.incrementAndGet();
/*     */                 progress.step("Conteúdo enviado em %s ms", new Object[] { Long.valueOf(time) });
/*     */               } 
/* 154 */             } catch (Exception e) {
/*     */               return Optional.of(e);
/*     */             } 
/*     */             
/*     */             return Optional.empty();
/*     */           });
/*     */       
/* 161 */       iProgressView.begin("Imprimindo", 2 * total);
/*     */       
/*     */       try {
/*     */         Optional<Exception> fail;
/* 165 */         if ((fail = task.get(10L, TimeUnit.SECONDS)).isPresent()) {
/* 166 */           throw (Exception)fail.get();
/*     */         }
/* 168 */       } catch (InterruptedException e) {
/* 169 */         throwCancel();
/*     */       }
/* 171 */       catch (IOException e) {
/* 172 */         throw (TaskException)iProgressView.abort(busyMessage(e));
/*     */       }
/* 174 */       catch (TimeoutException e) {
/* 175 */         if (printCount.get() == 0) {
/* 176 */           throw (TaskException)iProgressView.abort(timeoutMessage(e));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 181 */         waitForConclusion(task);
/*     */       }
/* 183 */       catch (Exception e) {
/* 184 */         throw new PrintFailException(e);
/*     */       } 
/*     */       
/* 187 */       iProgressView.end();
/* 188 */     } catch (PrintFailException e) {
/* 189 */       throw (TaskException)iProgressView.abort(showFail("Não foi possível realizar a impressão", e));
/*     */     } finally {
/*     */       
/* 192 */       service.shutdownNow();
/*     */     } 
/*     */     
/* 195 */     if (printCount.get() != total) {
/* 196 */       throw (TaskException)iProgressView.abort(showFail((total - printCount.get()) + " etiquetas NÃO foram impressas!"));
/*     */     }
/*     */     
/* 199 */     return (ITaskResponse<IPjeResponse>)success();
/*     */   }
/*     */   
/*     */   private void waitForConclusion(Future<Optional<Exception>> task) throws PrintFailException, InterruptedException {
/*     */     try {
/*     */       Optional<Exception> output;
/* 205 */       if ((output = task.get()).isPresent()) {
/* 206 */         throw new PrintFailException((Exception)output.get());
/*     */       }
/* 208 */     } catch (PrintFailException fail) {
/* 209 */       throw fail;
/* 210 */     } catch (InterruptedException fail) {
/* 211 */       throwCancel();
/* 212 */     } catch (Exception fail) {
/* 213 */       throw new PrintFailException(fail);
/*     */     } 
/*     */   }
/*     */   
/*     */   private TaskException timeoutMessage(TimeoutException e) {
/* 218 */     String failMessage = "Há uma demora incomum de mais de 10 segundos para escrita na porta '" + this.porta + "'";
/* 219 */     return showFail(failMessage, "Este problema pode ter as seguintes causas:\n\t1) Impressora desligada;\n\t2) A porta não foi configurada e/ou mapeada corretamente (causa mais provável).\n\t3) Impressora ocupada por um pedido de impressão anterior feito ou por este aplicativo ou por outro. Neste caso o timeout padrão do sistema operacional para liberação da porta é de 5 minutos.\n\t", e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TaskException busyMessage(IOException e) {
/* 227 */     String failMessage = "A porta '" + this.porta + "' não está acessível.";
/* 228 */     return showFail(failMessage, "Se seu desejo for imprimir em portas LPT, o timeout de ocupação definido pelo sistema operacional é de 5 minutos.\nIsto quer dizer que pelos próximo 5 minutos este problema persistirá até que a porta '" + this.porta + "' seja liberada novamente e a critério do sistema operacional.\n\tNota 1: Alternativamente você pode forçar o encerramento deste aplicativo (ou do programa que esteja fazendo uso desta porta) para reset deste timeout e fazer uma nova tentativa\n\tNota 2: Tenha certeza que a porta esteja corretamente configurada/mapeada antes de refazer o teste", e);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjePrintingTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */