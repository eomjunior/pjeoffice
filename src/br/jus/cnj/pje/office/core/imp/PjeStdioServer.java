/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.IBootable;
/*     */ import br.jus.cnj.pje.office.core.IPjeRequest;
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.taskresolver4j.exception.TaskExecutorDiscartingException;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.InterruptibleInputStream;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ class PjeStdioServer
/*     */   extends PjeURIServer
/*     */ {
/*     */   private static final int MAX_BODY_SIZE = 8421376;
/*  49 */   private final InputStream stdin = (InputStream)new InterruptibleInputStream(System.in);
/*     */   
/*     */   public PjeStdioServer(IBootable boot) {
/*  52 */     super(boot, "stdio://native-messaging");
/*     */   }
/*     */   
/*     */   private void skip() throws IOException {
/*     */     int skip;
/*  57 */     while ((skip = this.stdin.available()) > 0) {
/*  58 */       this.stdin.skip(skip);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void clearBuffer() {
/*  63 */     Throwables.quietly(this::skip);
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeResponse createResponse() throws Exception {
/*  68 */     return new PjeStdioResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   protected IPjeRequest createRequest(String uri) throws Exception {
/*  73 */     return new PjeStdioRequest(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ITaskResponse<IPjeResponse> getDefaultDiscardResponse(IPjeRequest request, IPjeResponse response, TaskExecutorDiscartingException e) {
/*  78 */     return PjeClientProtocol.STDIO.fail().apply(e);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getUri() throws InterruptedException, Exception {
/*  83 */     byte[] header = new byte[4];
/*     */     
/*     */     int read;
/*  86 */     if ((read = this.stdin.read(header)) < 0) {
/*  87 */       throw new IOException("Leitura negativa " + read);
/*     */     }
/*  89 */     int size = toInt(header);
/*  90 */     if (size <= 0 || size > 8421376) {
/*  91 */       throw new IOException("Header de tamanho inválido: " + size);
/*     */     }
/*  93 */     byte[] bodyUrl = new byte[size];
/*  94 */     if ((read = this.stdin.read(bodyUrl)) != size) {
/*  95 */       throw new IOException("Body de tamanho inválido: " + size + ". Esperado = " + read);
/*     */     }
/*  97 */     return new String(bodyUrl, IConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int toInt(byte[] bytes) {
/* 102 */     return bytes[3] << 24 & 0xFF000000 | bytes[2] << 16 & 0xFF0000 | bytes[1] << 8 & 0xFF00 | bytes[0] << 0 & 0xFF;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStdioServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */