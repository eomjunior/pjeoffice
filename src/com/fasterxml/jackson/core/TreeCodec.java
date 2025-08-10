/*    */ package com.fasterxml.jackson.core;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public abstract class TreeCodec
/*    */ {
/*    */   public abstract <T extends TreeNode> T readTree(JsonParser paramJsonParser) throws IOException, JsonProcessingException;
/*    */   
/*    */   public abstract void writeTree(JsonGenerator paramJsonGenerator, TreeNode paramTreeNode) throws IOException, JsonProcessingException;
/*    */   
/*    */   public TreeNode missingNode() {
/* 23 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TreeNode nullNode() {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public abstract TreeNode createArrayNode();
/*    */   
/*    */   public abstract TreeNode createObjectNode();
/*    */   
/*    */   public abstract JsonParser treeAsTokens(TreeNode paramTreeNode);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/TreeCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */