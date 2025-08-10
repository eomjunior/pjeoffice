/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.taskresolver4j.ITaskResponse;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.imp.Containers;
/*    */ import com.github.utils4j.imp.Directory;
/*    */ import com.github.utils4j.imp.Environment;
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class PjeBySliceVideoSplitterTask
/*    */   extends PjeAbstractMediaTask<ITarefaMedia>
/*    */ {
/*    */   protected PjeBySliceVideoSplitterTask(Params request, ITarefaMedia pojo) {
/* 54 */     super(request, pojo);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ITaskResponse<IPjeResponse> doGet() throws TaskException, InterruptedException {
/* 60 */     Path pjeofficeHome = (Path)Environment.requirePathFrom("pjeoffice_home").orElseThrow(() -> showFail("Não foi encontrada a variável de ambiente pjeoffice_home"));
/*    */     
/* 62 */     Path bin = pjeofficeHome.resolve("jre").resolve("bin");
/* 63 */     File javaw = bin.resolve("javaw.exe").toFile();
/* 64 */     if (!javaw.exists()) {
/* 65 */       javaw = bin.resolve("java").toFile();
/* 66 */       if (!javaw.exists()) {
/* 67 */         throw showFail("A instalação do PJeOffice Pro encontra-se corrompida.", "Não foi encontrado: " + 
/* 68 */             Directory.stringPath(javaw));
/*    */       }
/*    */     } 
/*    */     
/* 72 */     File cutplayer = pjeofficeHome.resolve("cutplayer4jfx.jar").toFile();
/* 73 */     if (!cutplayer.exists()) {
/* 74 */       throw showFail("A instalação do PJeOffice Pro encontra-se corrompida.", "Não foi encontrado: " + 
/* 75 */           Directory.stringPath(cutplayer));
/*    */     }
/*    */     
/* 78 */     File fileHome = pjeofficeHome.toFile();
/*    */     
/* 80 */     List<String> params = Containers.arrayList((Object[])new String[] {
/* 81 */           Directory.stringPath(javaw), 
/* 82 */           Jvms.env("pjeoffice_home", fileHome), 
/* 83 */           Jvms.env("ffmpeg_home", fileHome), 
/* 84 */           Jvms.env("cutplayer4j_looksandfeels", Environment.valueFrom("cutplayer4j_looksandfeels").orElse(Strings.empty())), "-jar", 
/*    */           
/* 86 */           Directory.stringPath(cutplayer)
/*    */         });
/* 88 */     params.addAll(this.arquivos);
/*    */     
/*    */     try {
/* 91 */       (new ProcessBuilder(params)).directory(fileHome).start();
/* 92 */     } catch (IOException e) {
/* 93 */       throw showFail("Não foi possível iniciar o player de cortes", e);
/*    */     } 
/*    */     
/* 96 */     return (ITaskResponse<IPjeResponse>)success();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeBySliceVideoSplitterTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */