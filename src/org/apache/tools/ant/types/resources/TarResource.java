/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.tar.TarEntry;
/*     */ import org.apache.tools.tar.TarInputStream;
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
/*     */ public class TarResource
/*     */   extends ArchiveResource
/*     */ {
/*  39 */   private String userName = "";
/*  40 */   private String groupName = "";
/*     */   private long uid;
/*     */   private long gid;
/*  43 */   private byte linkFlag = 48;
/*  44 */   private String linkName = "";
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
/*     */   public TarResource(File a, TarEntry e) {
/*  59 */     super(a, true);
/*  60 */     setEntry(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarResource(Resource a, TarEntry e) {
/*  70 */     super(a, true);
/*  71 */     setEntry(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  82 */     if (isReference()) {
/*  83 */       return getRef().getInputStream();
/*     */     }
/*  85 */     Resource archive = getArchive();
/*  86 */     TarInputStream i = new TarInputStream(archive.getInputStream());
/*     */     TarEntry te;
/*  88 */     while ((te = i.getNextEntry()) != null) {
/*  89 */       if (te.getName().equals(getName())) {
/*  90 */         return (InputStream)i;
/*     */       }
/*     */     } 
/*     */     
/*  94 */     FileUtils.close((InputStream)i);
/*  95 */     throw new BuildException("no entry " + getName() + " in " + 
/*  96 */         getArchive());
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 109 */     if (isReference()) {
/* 110 */       return getRef().getOutputStream();
/*     */     }
/* 112 */     throw new UnsupportedOperationException("Use the tar task for tar output.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 120 */     if (isReference()) {
/* 121 */       return getRef().getUserName();
/*     */     }
/* 123 */     checkEntry();
/* 124 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroup() {
/* 131 */     if (isReference()) {
/* 132 */       return getRef().getGroup();
/*     */     }
/* 134 */     checkEntry();
/* 135 */     return this.groupName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLongUid() {
/* 143 */     if (isReference()) {
/* 144 */       return getRef().getLongUid();
/*     */     }
/* 146 */     checkEntry();
/* 147 */     return this.uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getUid() {
/* 155 */     return (int)getLongUid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLongGid() {
/* 163 */     if (isReference()) {
/* 164 */       return getRef().getLongGid();
/*     */     }
/* 166 */     checkEntry();
/* 167 */     return this.gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getGid() {
/* 175 */     return (int)getLongGid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLinkName() {
/* 183 */     return this.linkName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getLinkFlag() {
/* 191 */     return this.linkFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fetchEntry() {
/* 199 */     Resource archive = getArchive(); 
/* 200 */     try { TarInputStream i = new TarInputStream(archive.getInputStream()); 
/* 201 */       try { TarEntry te = null;
/* 202 */         while ((te = i.getNextEntry()) != null)
/* 203 */         { if (te.getName().equals(getName()))
/* 204 */           { setEntry(te);
/*     */ 
/*     */ 
/*     */             
/* 208 */             i.close(); return; }  }  i.close(); } catch (Throwable throwable) { try { i.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 209 */     { log(e.getMessage(), 4);
/* 210 */       throw new BuildException(e); }
/*     */     
/* 212 */     setEntry((TarEntry)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TarResource getRef() {
/* 217 */     return (TarResource)getCheckedRef(TarResource.class);
/*     */   }
/*     */   
/*     */   private void setEntry(TarEntry e) {
/* 221 */     if (e == null) {
/* 222 */       setExists(false);
/*     */       return;
/*     */     } 
/* 225 */     setName(e.getName());
/* 226 */     setExists(true);
/* 227 */     setLastModified(e.getModTime().getTime());
/* 228 */     setDirectory(e.isDirectory());
/* 229 */     setSize(e.getSize());
/* 230 */     setMode(e.getMode());
/* 231 */     this.userName = e.getUserName();
/* 232 */     this.groupName = e.getGroupName();
/* 233 */     this.uid = e.getLongUserId();
/* 234 */     this.gid = e.getLongGroupId();
/* 235 */     this.linkName = e.getLinkName();
/* 236 */     this.linkFlag = e.getLinkFlag();
/*     */   }
/*     */   
/*     */   public TarResource() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/TarResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */