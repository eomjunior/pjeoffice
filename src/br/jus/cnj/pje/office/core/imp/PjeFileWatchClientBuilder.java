/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeClient;
/*    */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*    */ import com.github.utils4j.ICanceller;
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
/*    */ class PjeFileWatchClientBuilder
/*    */   extends PjeStdioClientBuilder
/*    */ {
/*    */   PjeFileWatchClientBuilder(Supplier<ICanceller> canceller, Supplier<IPjeJsonCodec> codec) {
/* 40 */     super(canceller, codec);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IPjeClient build() {
/* 47 */     return new PjeFileWatchClient(this.codec, this.canceller);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeFileWatchClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */