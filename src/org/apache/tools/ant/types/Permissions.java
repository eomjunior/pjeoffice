/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.SocketPermission;
/*     */ import java.security.UnresolvedPermission;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.PropertyPermission;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ExitException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Permissions
/*     */ {
/*  49 */   private final List<Permission> grantedPermissions = new LinkedList<>();
/*  50 */   private final List<Permission> revokedPermissions = new LinkedList<>();
/*  51 */   private java.security.Permissions granted = null;
/*  52 */   private SecurityManager origSm = null;
/*     */   
/*     */   private boolean active = false;
/*     */   
/*     */   private final boolean delegateToOldSM;
/*  57 */   private static final Class<?>[] PARAMS = new Class[] { String.class, String.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permissions() {
/*  64 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permissions(boolean delegateToOldSM) {
/*  74 */     this.delegateToOldSM = delegateToOldSM;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredGrant(Permission perm) {
/*  82 */     this.grantedPermissions.add(perm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredRevoke(Permission perm) {
/*  90 */     this.revokedPermissions.add(perm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setSecurityManager() throws BuildException {
/* 101 */     this.origSm = System.getSecurityManager();
/* 102 */     init();
/* 103 */     System.setSecurityManager(new MySM());
/* 104 */     this.active = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() throws BuildException {
/* 111 */     this.granted = new java.security.Permissions();
/* 112 */     for (Permission p : this.revokedPermissions) {
/* 113 */       if (p.getClassName() == null) {
/* 114 */         throw new BuildException("Revoked permission " + p + " does not contain a class.");
/*     */       }
/*     */     } 
/* 117 */     for (Permission p : this.grantedPermissions) {
/* 118 */       if (p.getClassName() == null) {
/* 119 */         throw new BuildException("Granted permission " + p + " does not contain a class.");
/*     */       }
/*     */       
/* 122 */       java.security.Permission perm = createPermission(p);
/* 123 */       this.granted.add(perm);
/*     */     } 
/*     */ 
/*     */     
/* 127 */     this.granted.add(new SocketPermission("localhost:1024-", "listen"));
/* 128 */     this.granted.add(new PropertyPermission("java.version", "read"));
/* 129 */     this.granted.add(new PropertyPermission("java.vendor", "read"));
/* 130 */     this.granted.add(new PropertyPermission("java.vendor.url", "read"));
/* 131 */     this.granted.add(new PropertyPermission("java.class.version", "read"));
/* 132 */     this.granted.add(new PropertyPermission("os.name", "read"));
/* 133 */     this.granted.add(new PropertyPermission("os.version", "read"));
/* 134 */     this.granted.add(new PropertyPermission("os.arch", "read"));
/* 135 */     this.granted.add(new PropertyPermission("file.encoding", "read"));
/* 136 */     this.granted.add(new PropertyPermission("file.separator", "read"));
/* 137 */     this.granted.add(new PropertyPermission("path.separator", "read"));
/* 138 */     this.granted.add(new PropertyPermission("line.separator", "read"));
/* 139 */     this.granted.add(new PropertyPermission("java.specification.version", "read"));
/* 140 */     this.granted.add(new PropertyPermission("java.specification.vendor", "read"));
/* 141 */     this.granted.add(new PropertyPermission("java.specification.name", "read"));
/* 142 */     this.granted.add(new PropertyPermission("java.vm.specification.version", "read"));
/* 143 */     this.granted.add(new PropertyPermission("java.vm.specification.vendor", "read"));
/* 144 */     this.granted.add(new PropertyPermission("java.vm.specification.name", "read"));
/* 145 */     this.granted.add(new PropertyPermission("java.vm.version", "read"));
/* 146 */     this.granted.add(new PropertyPermission("java.vm.vendor", "read"));
/* 147 */     this.granted.add(new PropertyPermission("java.vm.name", "read"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private java.security.Permission createPermission(Permission permission) {
/*     */     try {
/* 156 */       Class<? extends java.security.Permission> clazz = Class.forName(permission.getClassName()).asSubclass(java.security.Permission.class);
/* 157 */       String name = permission.getName();
/* 158 */       String actions = permission.getActions();
/* 159 */       Constructor<? extends java.security.Permission> ctr = clazz.getConstructor(PARAMS);
/* 160 */       return ctr.newInstance(new Object[] { name, actions });
/* 161 */     } catch (Exception e) {
/*     */       
/* 163 */       return new UnresolvedPermission(permission.getClassName(), permission
/* 164 */           .getName(), permission.getActions(), null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void restoreSecurityManager() {
/* 172 */     this.active = false;
/* 173 */     System.setSecurityManager(this.origSm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MySM
/*     */     extends SecurityManager
/*     */   {
/*     */     private MySM() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkExit(int status) {
/* 192 */       java.security.Permission perm = new RuntimePermission("exitVM", null);
/*     */       try {
/* 194 */         checkPermission(perm);
/* 195 */       } catch (SecurityException e) {
/* 196 */         throw new ExitException(e.getMessage(), status);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkPermission(java.security.Permission perm) {
/* 208 */       if (Permissions.this.active) {
/* 209 */         if (Permissions.this.delegateToOldSM && !perm.getName().equals("exitVM")) {
/* 210 */           boolean permOK = Permissions.this.granted.implies(perm);
/* 211 */           checkRevoked(perm);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 216 */           if (!permOK && Permissions.this.origSm != null) {
/* 217 */             Permissions.this.origSm.checkPermission(perm);
/*     */           }
/*     */         } else {
/* 220 */           if (!Permissions.this.granted.implies(perm)) {
/* 221 */             throw new SecurityException("Permission " + perm + " was not granted.");
/*     */           }
/* 223 */           checkRevoked(perm);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkRevoked(java.security.Permission perm) {
/* 233 */       for (Permissions.Permission revoked : Permissions.this.revokedPermissions) {
/* 234 */         if (revoked.matches(perm)) {
/* 235 */           throw new SecurityException("Permission " + perm + " was revoked.");
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Permission
/*     */   {
/*     */     private String className;
/*     */     
/*     */     private String name;
/*     */     
/*     */     private String actionString;
/*     */     
/*     */     private Set<String> actions;
/*     */     
/*     */     public void setClass(String aClass) {
/* 253 */       this.className = aClass.trim();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getClassName() {
/* 261 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String aName) {
/* 269 */       this.name = aName.trim();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 277 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setActions(String actions) {
/* 285 */       this.actionString = actions;
/* 286 */       if (!actions.isEmpty()) {
/* 287 */         this.actions = parseActions(actions);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getActions() {
/* 296 */       return this.actionString;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean matches(java.security.Permission perm) {
/* 304 */       if (!this.className.equals(perm.getClass().getName())) {
/* 305 */         return false;
/*     */       }
/* 307 */       if (this.name != null) {
/* 308 */         if (this.name.endsWith("*")) {
/* 309 */           if (!perm.getName().startsWith(this.name.substring(0, this.name.length() - 1))) {
/* 310 */             return false;
/*     */           }
/* 312 */         } else if (!this.name.equals(perm.getName())) {
/* 313 */           return false;
/*     */         } 
/*     */       }
/* 316 */       if (this.actions != null) {
/* 317 */         Set<String> as = parseActions(perm.getActions());
/* 318 */         int size = as.size();
/* 319 */         as.removeAll(this.actions);
/*     */         
/* 321 */         return (as.size() != size);
/*     */       } 
/* 323 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Set<String> parseActions(String actions) {
/* 331 */       Set<String> result = new HashSet<>();
/* 332 */       StringTokenizer tk = new StringTokenizer(actions, ",");
/* 333 */       while (tk.hasMoreTokens()) {
/* 334 */         String item = tk.nextToken().trim();
/* 335 */         if (!item.isEmpty()) {
/* 336 */           result.add(item);
/*     */         }
/*     */       } 
/* 339 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 348 */       return "Permission: " + this.className + " (\"" + this.name + "\", \"" + this.actions + "\")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Permissions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */