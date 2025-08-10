/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDriverVisitor;
/*    */ import com.github.signer4j.IPathCollectionStrategy;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
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
/*    */ abstract class PreloadedStrategy
/*    */   implements IPathCollectionStrategy
/*    */ {
/* 43 */   private final List<String> searchedPaths = new ArrayList<>(60);
/*    */   
/* 45 */   private final Set<DriverSetup> libraries = new HashSet<>();
/*    */   
/*    */   protected final boolean load(String library) {
/* 48 */     this.searchedPaths.add(library);
/* 49 */     Optional<DriverSetup> ds = DriverSetup.create(Paths.get(library, new String[0]));
/* 50 */     if (ds.isPresent()) {
/* 51 */       return this.libraries.add(ds.get());
/*    */     }
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final List<String> queriedPaths() {
/* 58 */     return Collections.unmodifiableList(this.searchedPaths);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void lookup(IDriverVisitor visitor) {
/* 63 */     this.libraries.forEach(visitor::visit);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PreloadedStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */