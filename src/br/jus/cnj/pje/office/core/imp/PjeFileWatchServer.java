/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.shell.ShellExtension;
/*     */ import br.jus.cnj.pje.office.task.imp.PjeTaskReader;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.utils4j.IFilePacker;
/*     */ import com.github.utils4j.imp.FilePacker;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.IExecutable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
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
/*     */ class PjeFileWatchServer
/*     */   extends PjeURIServer
/*     */ {
/*     */   private final IFilePacker<IOException> packer;
/*  63 */   private final Map<PjeTaskReader, List<Properties>> blockPerTask = new HashMap<>();
/*     */   
/*     */   public PjeFileWatchServer(IBootable boot) {
/*  66 */     super(boot, "filewatch://watch-service");
/*  67 */     this.packer = (IFilePacker<IOException>)new FilePacker(ShellExtension.HOME_WATCHING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStart() throws IOException {
/*  72 */     this.packer.start();
/*  73 */     super.doStart();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStop(boolean kill) throws IOException {
/*  78 */     this.packer.stop();
/*  79 */     super.doStop(kill);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearBuffer() {
/*  84 */     this.blockPerTask.clear();
/*  85 */     this.packer.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> getDefaultDiscardResponse(IPjeRequest request, IPjeResponse response, TaskExecutorDiscartingException e) {
/*  90 */     return PjeClientProtocol.FILEWATCH.fail().apply(e);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeResponse createResponse() throws Exception {
/*  95 */     return new PjeFileWatchResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeRequest createRequest(String uri) throws Exception {
/* 100 */     return new PjeFileWatchRequest(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispatch(List<File> files) {
/*     */     try {
/* 106 */       this.packer.offer(files);
/* 107 */     } catch (InterruptedException e) {
/* 108 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Optional<String> nextUri() {
/*     */     while (true) {
/* 114 */       Optional<PjeTaskReader> tr = this.blockPerTask.keySet().stream().findFirst();
/* 115 */       if (!tr.isPresent()) {
/* 116 */         return Optional.empty();
/*     */       }
/* 118 */       PjeTaskReader r = tr.get();
/* 119 */       List<Properties> block = this.blockPerTask.get(r);
/*     */       
/*     */       try {
/* 122 */         Params params = Params.create();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 127 */         List<String> arquivos = (List<String>)block.stream().peek(p -> p.forEach(())).map(p -> p.getProperty("arquivo")).collect(Collectors.toList());
/*     */         
/* 129 */         params.of("servidor", getServerEndpoint())
/* 130 */           .of("arquivos", arquivos);
/*     */         
/* 132 */         return Optional.of(getServerEndpoint(r.toUri(params)));
/* 133 */       } catch (Exception e) {
/* 134 */         LOGGER.warn("URI mal formada em fileWatchServer.nextUri() - reader: " + r.getId() + ". Tarefa ignorada/escapada", e);
/*     */       } finally {
/* 136 */         this.blockPerTask.remove(r);
/* 137 */         block.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getUri() throws InterruptedException, Exception {
/*     */     try {
/* 145 */       return nextUri().orElseGet(() -> {
/*     */             Optional<String> uri = Optional.empty();
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
/*     */             do {
/*     */               List<File> block = (List<File>)Throwables.runtime(this.packer::filesPackage);
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
/*     */               block.stream().filter(File::exists).forEach(());
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
/*     */               block.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               uri = (Optional<String>)Throwables.runtime(this::nextUri);
/*     */             } while (!uri.isPresent());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             return uri.get();
/*     */           });
/* 203 */     } catch (RuntimeException e) {
/* 204 */       Throwable cause = e.getCause();
/* 205 */       throw Exception.class.isInstance(cause) ? (Exception)cause : e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeFileWatchServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */