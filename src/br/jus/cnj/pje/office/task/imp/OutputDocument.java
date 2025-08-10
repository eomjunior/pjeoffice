/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.IOutputDocument;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(value = {"paramsEnvio"}, allowGetters = false, allowSetters = true)
/*    */ public abstract class OutputDocument
/*    */   extends Document
/*    */   implements IOutputDocument
/*    */ {
/*    */   private transient List<String> paramsEnvio;
/*    */   @JsonIgnore
/*    */   private transient boolean setup = false;
/*    */   
/*    */   public void dispose() {}
/*    */   
/*    */   public final List<String> getParamsEnvio() {
/* 34 */     if (!this.setup) {
/* 35 */       if (this.paramsEnvio == null)
/* 36 */         this.paramsEnvio = new ArrayList<>(); 
/* 37 */       giveBack((n, v) -> this.paramsEnvio.add(n + "=" + v));
/* 38 */       this.setup = true;
/*    */     } 
/* 40 */     return Collections.unmodifiableList(this.paramsEnvio);
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getParameter(String paramName) {
/* 45 */     Args.requireText(paramName, "paramName is null/empty");
/* 46 */     return getParamsEnvio().stream()
/* 47 */       .map(Strings::trim)
/* 48 */       .filter(s -> (s.startsWith(paramName) && s.contains("=")))
/* 49 */       .map(s -> s.substring(s.indexOf('=') + 1).trim())
/* 50 */       .filter(Strings::hasText)
/* 51 */       .findFirst();
/*    */   }
/*    */   
/*    */   protected void giveBack(BiConsumer<String, String> consumer) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/OutputDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */