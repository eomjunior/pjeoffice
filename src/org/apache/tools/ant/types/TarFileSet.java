/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarFileSet
/*     */   extends ArchiveFileSet
/*     */ {
/*     */   private boolean userNameSet;
/*     */   private boolean groupNameSet;
/*     */   private boolean userIdSet;
/*     */   private boolean groupIdSet;
/*  39 */   private String userName = "";
/*  40 */   private String groupName = "";
/*     */ 
/*     */   
/*     */   private int uid;
/*     */ 
/*     */   
/*     */   private int gid;
/*     */ 
/*     */ 
/*     */   
/*     */   public TarFileSet() {}
/*     */ 
/*     */   
/*     */   protected TarFileSet(FileSet fileset) {
/*  54 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TarFileSet(TarFileSet fileset) {
/*  62 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String userName) {
/*  71 */     checkTarFileSetAttributesAllowed();
/*  72 */     this.userNameSet = true;
/*  73 */     this.userName = userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/*  80 */     if (isReference()) {
/*  81 */       return ((TarFileSet)getRef()).getUserName();
/*     */     }
/*  83 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUserNameBeenSet() {
/*  90 */     return this.userNameSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUid(int uid) {
/*  99 */     checkTarFileSetAttributesAllowed();
/* 100 */     this.userIdSet = true;
/* 101 */     this.uid = uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUid() {
/* 108 */     if (isReference()) {
/* 109 */       return ((TarFileSet)getRef()).getUid();
/*     */     }
/* 111 */     return this.uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUserIdBeenSet() {
/* 118 */     return this.userIdSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroup(String groupName) {
/* 127 */     checkTarFileSetAttributesAllowed();
/* 128 */     this.groupNameSet = true;
/* 129 */     this.groupName = groupName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroup() {
/* 136 */     if (isReference()) {
/* 137 */       return ((TarFileSet)getRef()).getGroup();
/*     */     }
/* 139 */     return this.groupName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasGroupBeenSet() {
/* 146 */     return this.groupNameSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGid(int gid) {
/* 155 */     checkTarFileSetAttributesAllowed();
/* 156 */     this.groupIdSet = true;
/* 157 */     this.gid = gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGid() {
/* 164 */     if (isReference()) {
/* 165 */       return ((TarFileSet)getRef()).getGid();
/*     */     }
/* 167 */     return this.gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasGroupIdBeenSet() {
/* 174 */     return this.groupIdSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveScanner newArchiveScanner() {
/* 183 */     TarScanner zs = new TarScanner();
/* 184 */     zs.setEncoding(getEncoding());
/* 185 */     return zs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) throws BuildException {
/* 198 */     if (this.userNameSet || this.userIdSet || this.groupNameSet || this.groupIdSet) {
/* 199 */       throw tooManyAttributes();
/*     */     }
/* 201 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef(Project p) {
/* 212 */     dieOnCircularReference(p);
/* 213 */     Object o = getRefid().getReferencedObject(p);
/* 214 */     if (o instanceof TarFileSet) {
/* 215 */       return (AbstractFileSet)o;
/*     */     }
/* 217 */     if (o instanceof FileSet) {
/* 218 */       TarFileSet zfs = new TarFileSet((FileSet)o);
/* 219 */       configureFileSet(zfs);
/* 220 */       return zfs;
/*     */     } 
/* 222 */     String msg = getRefid().getRefId() + " doesn't denote a tarfileset or a fileset";
/* 223 */     throw new BuildException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef() {
/* 233 */     return getRef(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void configureFileSet(ArchiveFileSet zfs) {
/* 244 */     super.configureFileSet(zfs);
/* 245 */     if (zfs instanceof TarFileSet) {
/* 246 */       TarFileSet tfs = (TarFileSet)zfs;
/* 247 */       tfs.setUserName(this.userName);
/* 248 */       tfs.setGroup(this.groupName);
/* 249 */       tfs.setUid(this.uid);
/* 250 */       tfs.setGid(this.gid);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 261 */     if (isReference()) {
/* 262 */       return getRef().clone();
/*     */     }
/* 264 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkTarFileSetAttributesAllowed() {
/* 274 */     if (getProject() == null || (
/* 275 */       isReference() && 
/* 276 */       getRefid().getReferencedObject(
/* 277 */         getProject()) instanceof TarFileSet))
/*     */     {
/* 279 */       checkAttributesAllowed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/TarFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */