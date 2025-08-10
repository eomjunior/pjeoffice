/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.IJsonTranslator;
/*    */ import br.jus.cnj.pje.office.task.ITarefaMedia;
/*    */ import com.github.taskresolver4j.imp.RequestReader;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
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
/*    */ abstract class TarefaMediaReader<T>
/*    */   extends RequestReader<Params, T>
/*    */   implements IJsonTranslator
/*    */ {
/*    */   protected static class TarefaMedia
/*    */     implements ITarefaMedia
/*    */   {
/* 50 */     protected List<String> arquivos = new ArrayList<>();
/*    */ 
/*    */     
/*    */     public final List<String> getArquivos() {
/* 54 */       return (this.arquivos == null) ? Collections.<String>emptyList() : Collections.<String>unmodifiableList(this.arquivos);
/*    */     }
/*    */   }
/*    */   
/*    */   protected TarefaMediaReader(Class<?> clazz) {
/* 59 */     super(clazz);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toJson(Params param) throws Exception {
/* 64 */     return PayloadRequestReader.PAYLOAD.toJson(param
/* 65 */         .of("tarefaId", getTarefaId())
/* 66 */         .of("tarefa", getTarefa(param)));
/*    */   }
/*    */   
/*    */   protected abstract String getTarefaId();
/*    */   
/*    */   protected abstract T getTarefa(Params paramParams);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/TarefaMediaReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */