/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
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
/*    */ class PjeStdioClientBuilder
/*    */   extends PjeClientBuilder
/*    */ {
/*    */   protected final Supplier<IPjeJsonCodec> codec;
/*    */   
/*    */   PjeStdioClientBuilder(Supplier<ICanceller> canceller, Supplier<IPjeJsonCodec> codec) {
/* 43 */     super(canceller);
/* 44 */     this.codec = (Supplier<IPjeJsonCodec>)Args.requireNonNull(codec, "codec is null");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IPjeClient build() {
/* 51 */     return new PjeStdioClient(this.codec, this.canceller);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeStdioClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */