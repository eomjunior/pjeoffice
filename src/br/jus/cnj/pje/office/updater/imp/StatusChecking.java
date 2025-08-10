/*    */ package br.jus.cnj.pje.office.updater.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.updater.IStatusChecking;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class StatusChecking
/*    */   implements IStatusChecking
/*    */ {
/*    */   private final VersionStatus status;
/*    */   private final Optional<String> patcherHash;
/*    */   
/*    */   static IStatusChecking undefined() {
/* 18 */     return new StatusChecking(VersionStatus.UNDEFINED);
/*    */   }
/*    */   
/*    */   static IStatusChecking updated() {
/* 22 */     return new StatusChecking(VersionStatus.UPDATED);
/*    */   }
/*    */   
/*    */   static IStatusChecking outdated(String patcherHash) {
/* 26 */     return new StatusChecking(VersionStatus.OUTDATED, patcherHash);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private StatusChecking(VersionStatus status) {
/* 34 */     this(status, Strings.empty());
/*    */   }
/*    */   
/*    */   private StatusChecking(VersionStatus status, String patcherHash) {
/* 38 */     this.status = (VersionStatus)Args.requireNonNull(status, "status is null");
/* 39 */     this.patcherHash = Strings.optional(patcherHash);
/*    */   }
/*    */ 
/*    */   
/*    */   public final VersionStatus getStatus() {
/* 44 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getPatcherHash() {
/* 49 */     return this.patcherHash.orElse(Strings.empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isAcceptable() {
/* 54 */     return (VersionStatus.OUTDATED.equals(this.status) && this.patcherHash.isPresent());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/updater/imp/StatusChecking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */