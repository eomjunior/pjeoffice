/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
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
/*    */ class PjeClientWebBuilder
/*    */   extends PjeClientBuilder
/*    */ {
/*    */   private final Supplier<IPjeWebCodec> codec;
/*    */   
/*    */   public PjeClientWebBuilder(Supplier<ICanceller> canceller, Supplier<IPjeWebCodec> codec) {
/* 43 */     super(canceller);
/* 44 */     this.codec = (Supplier<IPjeWebCodec>)Args.requireNonNull(codec, "codec is null");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final IPjeClient build() {
/* 51 */     return new PjeWebClient(this.codec, this.canceller);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientWebBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */