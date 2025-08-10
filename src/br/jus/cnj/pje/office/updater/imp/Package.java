/*    */ package br.jus.cnj.pje.office.updater.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.PjeVersion;
/*    */ import br.jus.cnj.pje.office.updater.IPackage;
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Containers;
/*    */ import com.github.utils4j.imp.Directory;
/*    */ import com.github.utils4j.imp.Environment;
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Package
/*    */   implements IPackage
/*    */ {
/*    */   private final File patcherJar;
/*    */   private final IProgress progress;
/*    */   
/*    */   Package(File patcherJar, IProgress progress) throws IOException {
/* 33 */     this.patcherJar = (File)Args.requireNonNull(patcherJar, "updater is null");
/* 34 */     this.progress = (IProgress)Args.requireNonNull(progress, "progress is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply() throws Exception {
/* 39 */     this.progress.begin("Aplicando pacote de atualização...");
/*    */     
/*    */     try {
/* 42 */       Path pjeofficeHome = (Path)Environment.requirePathFrom("pjeoffice_home").orElseThrow(() -> new Exception("Não foi encontrada a variável de ambiente pjeoffice_home"));
/*    */       
/* 44 */       Path bin = pjeofficeHome.resolve("jre").resolve("bin");
/*    */       
/* 46 */       File javaw = bin.resolve("javaw.exe").toFile();
/*    */       
/* 48 */       if (!javaw.exists()) {
/* 49 */         javaw = bin.resolve("java").toFile();
/* 50 */         if (!javaw.exists()) {
/* 51 */           throw new Exception("A instalação atual do PJeOffice Pro encontra-se corrompida. Não foi encontrado: " + Directory.stringPath(javaw));
/*    */         }
/*    */       } 
/*    */       
/* 55 */       File pjeOfficeDir = pjeofficeHome.toFile();
/*    */       
/*    */       try {
/* 58 */         String version = PjeVersion.CURRENT.toString();
/* 59 */         String pjeOfficeDirectoryPath = Directory.stringPath(pjeOfficeDir);
/* 60 */         String patcherJarPath = Directory.stringPath(this.patcherJar);
/* 61 */         File patcherDirectory = this.patcherJar.getParentFile();
/* 62 */         String patcherDirectoryPath = Directory.stringPath(patcherDirectory);
/* 63 */         String looksAndFeels = Environment.valueFrom("pjeoffice_looksandfeels").orElse("Metal");
/*    */         
/* 65 */         List<String> params = Containers.arrayList((Object[])new String[] { 
/* 66 */               Directory.stringPath(javaw), 
/* 67 */               Jvms.env("pjeoffice_home", pjeOfficeDirectoryPath), 
/* 68 */               Jvms.env("pjeoffice_version", version), 
/* 69 */               Jvms.env("pjeoffice_patcher_home", patcherDirectoryPath), 
/* 70 */               Jvms.env("pjeoffice_patcher_jar", patcherJarPath), 
/* 71 */               Jvms.env("pjeoffice_looksandfeels", looksAndFeels), "-jar", patcherJarPath, pjeOfficeDirectoryPath, version, patcherDirectoryPath, patcherJarPath, looksAndFeels });
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 76 */         (new ProcessBuilder(params)).directory(patcherDirectory).start();
/* 77 */       } catch (IOException e) {
/* 78 */         throw new Exception("Não foi possível iniciar execução do atualizador!", e);
/*    */       } 
/*    */     } finally {
/* 81 */       Throwables.quietly(this.progress::end);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/imp/Package.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */