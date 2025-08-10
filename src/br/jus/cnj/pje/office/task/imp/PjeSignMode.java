/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import br.jus.cnj.pje.office.task.IPjeSignMode;
/*    */ import br.jus.cnj.pje.office.task.ITarefaAssinador;
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import com.github.taskresolver4j.ITask;
/*    */ import com.github.utils4j.imp.Params;
/*    */ import java.util.Optional;
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
/*    */ public enum PjeSignMode
/*    */   implements IPjeSignMode
/*    */ {
/* 42 */   LOCAL("LOCAL")
/*    */   {
/*    */     public ITask<IPjeResponse> getTask(Params params, ITarefaAssinador pojo) {
/* 45 */       return (ITask<IPjeResponse>)new PjeLocalSigningTask(params, pojo);
/*    */     }
/*    */   },
/* 48 */   DEFINIDO("DEFINIDO")
/*    */   {
/*    */     public ITask<IPjeResponse> getTask(Params params, ITarefaAssinador pojo) {
/* 51 */       return (ITask<IPjeResponse>)new PjePredefinedLocalSigningTask(params, pojo);
/*    */     }
/*    */   },
/* 54 */   REMOTO("REMOTO")
/*    */   {
/*    */     public ITask<IPjeResponse> getTask(Params params, ITarefaAssinador pojo) {
/* 57 */       return (ITask<IPjeResponse>)new PjeRemoteSigningTask(params, pojo);
/*    */     } };
/*    */   private static final PjeSignMode[] VALUES;
/*    */   static {
/* 61 */     VALUES = values();
/*    */   } private String name;
/*    */   @JsonCreator
/*    */   public static PjeSignMode fromString(String key) {
/* 65 */     return get(key).orElse(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   PjeSignMode(String name) {
/* 71 */     this.name = name;
/*    */   }
/*    */   
/*    */   @JsonValue
/*    */   public String getKey() {
/* 76 */     return this.name.toLowerCase();
/*    */   }
/*    */   
/*    */   public static Optional<PjeSignMode> get(String name) {
/* 80 */     for (PjeSignMode a : VALUES) {
/* 81 */       if (a.name.equalsIgnoreCase(name))
/* 82 */         return Optional.of(a); 
/*    */     } 
/* 84 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeSignMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */