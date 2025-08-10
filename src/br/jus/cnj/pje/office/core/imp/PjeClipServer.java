/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import br.jus.cnj.pje.office.task.imp.PjeTaskReader;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.utils4j.imp.Ids;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.awt.datatransfer.Transferable;
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
/*     */ class PjeClipServer
/*     */   extends PjeURIServer
/*     */ {
/*  51 */   private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/*     */   
/*  53 */   private String lastUri = Ids.next();
/*     */   
/*     */   public PjeClipServer(IBootable boot) {
/*  56 */     super(boot, "clip://global-messaging");
/*     */   }
/*     */   
/*     */   protected boolean isLast(String uri) {
/*  60 */     return this.lastUri.equals(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearBuffer() {
/*  65 */     this.clipboard.setContents(new StringSelection(""), null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeResponse createResponse() throws Exception {
/*  70 */     return new PjeClipResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeRequest createRequest(String input) throws Exception {
/*  75 */     return new PjeClipRequest(input);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> getDefaultDiscardResponse(IPjeRequest request, IPjeResponse response, TaskExecutorDiscartingException e) {
/*  80 */     return PjeClientProtocol.CLIP.fail().apply(e);
/*     */   }
/*     */   
/*     */   protected String getUri() throws InterruptedException, Exception {
/*     */     Optional<String> uri;
/*     */     while (true) {
/*  86 */       Thread.sleep(1000L);
/*  87 */       Optional<Transferable> content = Optional.ofNullable(this.clipboard.getContents(this));
/*  88 */       if (!content.isPresent()) {
/*     */         continue;
/*     */       }
/*  91 */       Transferable t = content.get();
/*  92 */       if (!t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
/*     */         continue;
/*     */       }
/*  95 */       String stringContent = Strings.trim((String)t.getTransferData(DataFlavor.stringFlavor));
/*     */       
/*  97 */       uri = nextUri(stringContent);
/*     */       
/*  99 */       if (!uri.isPresent())
/*     */         continue; 
/*     */       break;
/*     */     } 
/* 103 */     return getServerEndpoint(uri.get());
/*     */   }
/*     */   
/*     */   private Optional<String> nextUri(String content) {
/*     */     Optional<String> uri;
/* 108 */     if (content.isEmpty() || isLast(content)) {
/* 109 */       return Optional.empty();
/*     */     }
/*     */     
/* 112 */     this.lastUri = content;
/*     */     
/* 114 */     if (!this.lastUri.startsWith("https://jsoncompare.org")) {
/* 115 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     Params params = Params.create().of("servidor", getServerEndpoint()).of("url", content).of("enviarPara", "C:\\Users\\Leonardo\\Documents\\TEMP\\baixado.mp4");
/*     */ 
/*     */     
/*     */     try {
/* 125 */       uri = Optional.ofNullable(PjeTaskReader.UTIL_DOWNLOADER.toUri(params));
/* 126 */     } catch (Exception e) {
/* 127 */       uri = Optional.empty();
/*     */     } 
/*     */     
/* 130 */     return uri;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClipServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */