/*    */ package org.apache.hc.core5.http2.hpack;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.apache.hc.core5.util.Asserts;
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
/*    */ final class HuffmanNode
/*    */ {
/*    */   private final int symbol;
/*    */   private final int bits;
/*    */   private final HuffmanNode[] children;
/*    */   
/*    */   HuffmanNode() {
/* 45 */     this.symbol = 0;
/* 46 */     this.bits = 8;
/* 47 */     this.children = new HuffmanNode[256];
/*    */   }
/*    */   
/*    */   HuffmanNode(int symbol, int bits) {
/* 51 */     this.symbol = symbol;
/* 52 */     this.bits = bits;
/* 53 */     this.children = null;
/*    */   }
/*    */   
/*    */   public int getBits() {
/* 57 */     return this.bits;
/*    */   }
/*    */   
/*    */   public int getSymbol() {
/* 61 */     return this.symbol;
/*    */   }
/*    */   
/*    */   public boolean hasChild(int index) {
/* 65 */     return (this.children != null && this.children[index] != null);
/*    */   }
/*    */   
/*    */   public HuffmanNode getChild(int index) {
/* 69 */     return (this.children != null) ? this.children[index] : null;
/*    */   }
/*    */   
/*    */   void setChild(int index, HuffmanNode child) {
/* 73 */     Asserts.notNull(this.children, "Children nodes");
/* 74 */     this.children[index] = child;
/*    */   }
/*    */   
/*    */   public boolean isTerminal() {
/* 78 */     return (this.children == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "[symbol=" + this.symbol + ", bits=" + this.bits + ", children=" + 
/*    */ 
/*    */       
/* 86 */       Arrays.toString((Object[])this.children) + ']';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HuffmanNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */