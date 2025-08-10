/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.taskresolver4j.exception.TaskException;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.io.File;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
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
/*    */ abstract class PjeAbstractMediaTask<T extends ITarefaMedia>
/*    */   extends PjeAbstractTask<T>
/*    */ {
/*    */   protected List<String> arquivos;
/*    */   
/*    */   protected PjeAbstractMediaTask(Params request, T pojo) {
/* 49 */     super(request, pojo, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void validateTaskParams() throws TaskException, InterruptedException {
/* 54 */     List<String> files = ((ITarefaMedia)getPojoParams()).getArquivos();
/* 55 */     if (files.isEmpty())
/*    */     {
/*    */       
/* 58 */       files = (List<String>)Stream.<File>of(selectFilesFromDialog("Selecione os arquivos")).map(File::getAbsolutePath).collect(Collectors.toList());
/*    */     }
/* 60 */     this
/*    */ 
/*    */       
/* 63 */       .arquivos = (List<String>)files.stream().map(s -> (String)Throwables.call((), s)).collect(Collectors.toList());
/* 64 */     doValidateTaskParams();
/*    */   }
/*    */   
/*    */   protected void doValidateTaskParams() throws TaskException, InterruptedException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeAbstractMediaTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */