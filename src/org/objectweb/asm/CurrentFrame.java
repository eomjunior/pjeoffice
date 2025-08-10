/*    */ package org.objectweb.asm;
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
/*    */ 
/*    */ final class CurrentFrame
/*    */   extends Frame
/*    */ {
/*    */   CurrentFrame(Label owner) {
/* 40 */     super(owner);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void execute(int opcode, int arg, Symbol symbolArg, SymbolTable symbolTable) {
/* 51 */     super.execute(opcode, arg, symbolArg, symbolTable);
/* 52 */     Frame successor = new Frame(null);
/* 53 */     merge(symbolTable, successor, 0);
/* 54 */     copyFrom(successor);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/objectweb/asm/CurrentFrame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */