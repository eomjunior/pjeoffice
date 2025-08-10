/*     */ package br.jus.cnj.pje.office.updater.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.config.imp.Global;
/*     */ import br.jus.cnj.pje.office.core.imp.sec.PjeSecurity;
/*     */ import br.jus.cnj.pje.office.updater.IPackage;
/*     */ import br.jus.cnj.pje.office.updater.IStatusChecking;
/*     */ import br.jus.cnj.pje.office.updater.IVersionChecker;
/*     */ import com.github.progress4j.IProgress;
/*     */ import com.github.progress4j.IProgressView;
/*     */ import com.github.progress4j.imp.ProgressStatus;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.gui.imp.Silencer;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.DefaultHttpClientCreator;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import com.github.utils4j.imp.Downloader;
/*     */ import com.github.utils4j.imp.Randomizer;
/*     */ import com.github.utils4j.imp.Streams;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.zeroturnaround.zip.ZipUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HttpVersionChecker
/*     */   implements IVersionChecker
/*     */ {
/*     */   private final String rootUri;
/*     */   
/*     */   public HttpVersionChecker(String rootUri) {
/*  39 */     this.rootUri = Args.requireText(rootUri, "rootUri is empty");
/*     */   }
/*     */ 
/*     */   
/*     */   public IStatusChecking check(IProgressView progress, boolean silent) {
/*  44 */     Args.requireNonNull(progress, "progress is null");
/*     */     try {
/*  46 */       progress.begin("Verificando se há novas atualizações...");
/*     */       try {
/*  48 */         ProgressStatus progressStatus = new ProgressStatus((IProgress)progress, "Baixando 'update.properties'...", true);
/*     */         
/*  50 */         try (CloseableHttpClient client = DefaultHttpClientCreator.create(Global.CONFIG.http_client_user_agent())) {
/*  51 */           Downloader downloader = new Downloader(this.rootUri, client);
/*  52 */           Disposable ticket = downloader.newRequest().subscribe(req -> progress.cancelCode(req::abort));
/*  53 */           progress.info("Consulta em '%s'", new Object[] { this.rootUri });
/*     */           try {
/*  55 */             downloader.download("update.properties?&nocache=" + Randomizer.nocache(), (IDownloadStatus)progressStatus);
/*     */           } finally {
/*  57 */             ticket.dispose();
/*     */           } 
/*  59 */         } catch (Exception e) {
/*  60 */           Silencer.failAs(silent, "Não foi possível confirmar a existência de nova versão!", e);
/*  61 */           return StatusChecking.undefined();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  89 */         Throwables.quietly(progress::end);
/*     */       } 
/*  91 */     } catch (InterruptedException e) {
/*  92 */       Silencer.failAs(silent, "Operação cancelada!", e);
/*  93 */       return StatusChecking.undefined();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IPackage getPackage(IProgressView progress, IStatusChecking checking) throws Exception {
/*  99 */     Args.requireNonNull(progress, "progress is null");
/* 100 */     Args.requireNonNull(checking, "checking is null");
/*     */     
/* 102 */     if (!checking.isAcceptable()) {
/* 103 */       throw new Exception("A propriedade 'app.patcher.hash' não foi definida em 'update.properties' do servidor de atualização ou a aplicação já se encontra atualizada.");
/*     */     }
/*     */ 
/*     */     
/* 107 */     ProgressStatus progressStatus = new ProgressStatus((IProgress)progress, "Baixando patcher de atualização...", true);
/*     */     
/* 109 */     try (CloseableHttpClient client = DefaultHttpClientCreator.create(Global.CONFIG.http_client_user_agent())) {
/* 110 */       Downloader downloader = new Downloader(this.rootUri, client);
/* 111 */       Disposable ticket = downloader.newRequest().subscribe(req -> progress.cancelCode(req::abort));
/*     */       try {
/* 113 */         downloader.download("patcher.zip?&nocache=" + Randomizer.nocache(), (IDownloadStatus)progressStatus);
/*     */       } finally {
/* 115 */         ticket.dispose();
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     File patcherZip = (File)progressStatus.getDownloadedFile().orElseThrow(() -> new Exception("Arquivo 'patcher.zip' ausente ou vazio no servidor de atualização."));
/*     */     
/*     */     try {
/*     */       String patcherZipHash, patcherZipHashDecripted;
/*     */       File patcherTempDir;
/*     */       try {
/* 125 */         patcherZipHash = Streams.sha1(patcherZip);
/* 126 */       } catch (IOException e) {
/* 127 */         throw new Exception("Não foi possível calcular o hash do arquivo 'patcher.zip' baixado.", e);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 132 */         patcherZipHashDecripted = PjeSecurity.decrypt(checking.getPatcherHash());
/* 133 */       } catch (Exception e) {
/* 134 */         throw new Exception("Não foi possível descriptografar o hash definido em 'app.patcher.hash' de 'update.properties' do servidor de atualização. Há uma violação de segurança!", e);
/*     */       } 
/*     */ 
/*     */       
/* 138 */       if (!patcherZipHashDecripted.equals(patcherZipHash)) {
/* 139 */         throw new Exception("O hash definido em 'app.patcher.hash' é diferente do hash do arquivo baixado 'patcher.zip'. Ou houve falha na transferência de dados ou o hash foi calculado sobre um binário 'patcher.zip' inválido no processo de build");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 146 */         patcherTempDir = Files.createTempDirectory("patcher", (FileAttribute<?>[])new FileAttribute[0]).toFile();
/* 147 */       } catch (IOException e) {
/* 148 */         throw new Exception("Não foi possível criar pasta temporária para descompactação do pacote 'patcher.zip'.", e);
/*     */       } 
/*     */ 
/*     */       
/* 152 */       progress.begin("Descompactando pacote patcher.zip...");
/*     */       try {
/* 154 */         ZipUtil.unpack(patcherZip, patcherTempDir);
/* 155 */       } catch (Exception e) {
/* 156 */         Directory.deleteQuietly(patcherTempDir);
/* 157 */         throw new Exception("Não foi possível descompactar o pacote de atualização baixado 'patcher.zip'.", e);
/*     */       } finally {
/*     */         
/* 160 */         Throwables.quietly(progress::end);
/*     */       } 
/*     */       
/* 163 */       File patcherJar = new File(patcherTempDir, "patcher.jar");
/* 164 */       if (!patcherJar.exists()) {
/* 165 */         Directory.deleteQuietly(patcherTempDir);
/* 166 */         throw new Exception("Não foi encontrado no pacote de atualização 'patcher.zip' uma referência ao atualizador 'patcher.jar'");
/*     */       } 
/*     */ 
/*     */       
/* 170 */       return new Package(patcherJar, (IProgress)progress);
/*     */     } finally {
/*     */       
/* 173 */       Directory.deleteQuietly(patcherZip);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/imp/HttpVersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */