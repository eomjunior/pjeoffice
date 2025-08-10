/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClientBuilder;
/*    */ import com.github.utils4j.ICanceller;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.util.function.Supplier;
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
/*    */ abstract class PjeClientBuilder
/*    */   implements IPjeClientBuilder
/*    */ {
/*    */   protected final Supplier<ICanceller> canceller;
/*    */   
/*    */   protected PjeClientBuilder(Supplier<ICanceller> canceller) {
/* 42 */     this.canceller = (Supplier<ICanceller>)Args.requireNonNull(canceller, "canceller is null");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */