/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeSocketCodec;
/*     */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*     */ import br.jus.cnj.pje.office.task.IPjeEndpoint;
/*     */ import br.jus.cnj.pje.office.task.ISSOPayload;
/*     */ import br.jus.cnj.pje.office.task.ISignableURLDocument;
/*     */ import com.github.signer4j.ISignedData;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.IContentType;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Pair;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.Functions;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
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
/*     */ public abstract class PjeClient<T>
/*     */   implements IPjeClient
/*     */ {
/*     */   private final Supplier<ICanceller> canceller;
/*     */   private final Supplier<? extends IPjeSocketCodec<T>> socket;
/*     */   
/*     */   protected PjeClient(Supplier<? extends IPjeSocketCodec<T>> socket, Supplier<ICanceller> canceller) {
/*  65 */     this.socket = (Supplier<? extends IPjeSocketCodec<T>>)Args.requireNonNull(socket, "socket is null");
/*  66 */     this.canceller = (Supplier<ICanceller>)Args.requireNonNull(canceller, "canceller is null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void download(IPjeEndpoint endpoint, IDownloadStatus status) throws PjeClientException, InterruptedException {
/*  76 */     Args.requireNonNull(endpoint, "endpoint is null");
/*  77 */     Args.requireNonNull(status, "status is null");
/*  78 */     get(() -> createGet(endpoint), status);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, ISSOPayload payload) throws PjeClientException, InterruptedException {
/*  84 */     Args.requireNonNull(endpoint, "endpoint is null");
/*  85 */     Args.requireNonNull(payload, "payload is null");
/*  86 */     return post(() -> createOutput(endpoint, payload), IResultChecker.NOTHING);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, ISignedData signedData) throws PjeClientException, InterruptedException {
/*  92 */     Args.requireNonNull(endpoint, "endpoint is null");
/*  93 */     Args.requireNonNull(signedData, "signedData is null");
/*  94 */     return post(() -> createOutput(endpoint, signedData), getIfError());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, String certificateChain64) throws PjeClientException, InterruptedException {
/* 100 */     Args.requireNonNull(endpoint, "endpoint is null");
/* 101 */     Args.requireNonNull(certificateChain64, "certificateChain64 is null");
/* 102 */     return post(() -> createOutput(endpoint, certificateChain64), getIfNotSuccess());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, Object pojo) throws PjeClientException, InterruptedException {
/* 108 */     Args.requireNonNull(endpoint, "endpoint is null");
/* 109 */     Args.requireNonNull(pojo, "pojo is null");
/* 110 */     return post(() -> createOutput(endpoint, pojo), getIfNotSuccess());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, ISignableURLDocument document, IContentType contentType) throws PjeClientException, InterruptedException {
/* 116 */     Args.requireNonNull(endpoint, "endpoint is null");
/* 117 */     Args.requireNonNull(document, "document is null");
/* 118 */     Args.requireNonNull(contentType, "contentType is null");
/* 119 */     return post(() -> createOutput(endpoint, document, contentType), getIfError());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final PjeTaskResponse send(IPjeEndpoint endpoint, ISignedData signedData, IOutputDocument document) throws PjeClientException, InterruptedException {
/* 125 */     Args.requireNonNull(endpoint, "endpoint is null");
/* 126 */     Args.requireNonNull(signedData, "signedData is null");
/* 127 */     Args.requireNonNull(document, "document is null");
/* 128 */     return post(() -> createOutput(endpoint, signedData, document), getIfNotSuccess());
/*     */   }
/*     */ 
/*     */   
/*     */   private void get(IProvider<HttpGet> provider, IDownloadStatus status) throws PjeClientException, InterruptedException {
/* 133 */     submit("DOWNLOAD", Functions.nullProvider(() -> ((IPjeSocketCodec)this.socket.get()).get(provider, status)));
/*     */   }
/*     */ 
/*     */   
/*     */   private PjeTaskResponse post(IProvider<T> provider, IResultChecker checkResults) throws PjeClientException, InterruptedException {
/* 138 */     return submit("UPLOAD", () -> (PjeTaskResponse)((IPjeSocketCodec)this.socket.get()).post(provider, checkResults));
/*     */   }
/*     */ 
/*     */   
/*     */   private PjeTaskResponse submit(String method, IProvider<PjeTaskResponse> socket) throws PjeClientException, InterruptedException {
/*     */     try {
/* 144 */       return (PjeTaskResponse)socket.get();
/* 145 */     } catch (InterruptedException e) {
/* 146 */       throw e;
/* 147 */     } catch (PjeClientException e) {
/* 148 */       checkInterrupt(method, e);
/* 149 */       throw e;
/* 150 */     } catch (ConnectionRequestTimeoutException e) {
/* 151 */       throw new PjeClientConnectionTimeoutException("Pool de conexões em timeout", e);
/* 152 */     } catch (Exception e) {
/* 153 */       checkInterrupt(method, e);
/* 154 */       throw new PjeClientException("Falha no " + method, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void checkInterrupt(String method, Exception e) throws InterruptedException {
/* 159 */     if (Thread.currentThread().isInterrupted() || "Request aborted".equals(e.getMessage())) {
/* 160 */       throw new InterruptedException(method + " cancelado! \n\tcause: " + Throwables.rootMessage(e));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <H extends org.apache.hc.client5.http.classic.methods.HttpUriRequestBase> H createOutput(H request, IPjeEndpoint endpoint) throws InterruptedException {
/* 166 */     request.setHeader("Cookie", endpoint.getSession());
/* 167 */     request.setHeader("versao", PjeVersion.CURRENT);
/*     */     
/* 169 */     request.setHeader("Accept-Encoding", "gzip,deflate");
/* 170 */     ((ICanceller)this.canceller.get()).cancelCode(request::abort);
/* 171 */     return request;
/*     */   }
/*     */   
/*     */   private HttpGet createGet(IPjeEndpoint endpoint) throws InterruptedException {
/* 175 */     return createOutput(new HttpGet(endpoint.getPath()), endpoint);
/*     */   }
/*     */   
/*     */   protected static Stream<Pair<String, String>> giveBack(IOutputDocument document) {
/* 179 */     return document.getParamsEnvio().stream().map(Strings::trim).filter(Strings::hasText).map(param -> {
/*     */           int idx = param.indexOf('=');
/*     */           return Pair.of(((idx < 0) ? param : param.substring(0, idx)).trim(), ((idx < 0) ? "" : param.substring(idx + 1)).trim());
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract IResultChecker getIfError();
/*     */   
/*     */   protected abstract IResultChecker getIfNotSuccess();
/*     */   
/*     */   protected abstract T createOutput(IPjeEndpoint paramIPjeEndpoint, Object paramObject) throws Exception;
/*     */   
/*     */   protected abstract T createOutput(IPjeEndpoint paramIPjeEndpoint, ISignedData paramISignedData) throws Exception;
/*     */   
/*     */   protected abstract T createOutput(IPjeEndpoint paramIPjeEndpoint, String paramString) throws Exception;
/*     */   
/*     */   protected abstract T createOutput(IPjeEndpoint paramIPjeEndpoint, ISignedData paramISignedData, IOutputDocument paramIOutputDocument) throws Exception;
/*     */   
/*     */   protected abstract T createOutput(IPjeEndpoint paramIPjeEndpoint, ISignableURLDocument paramISignableURLDocument, IContentType paramIContentType) throws Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */