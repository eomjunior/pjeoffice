/*     */ package org.apache.tools.ant.taskdefs.optional.j2ee;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class JonasHotDeploymentTool
/*     */   extends GenericHotDeploymentTool
/*     */   implements HotDeploymentTool
/*     */ {
/*     */   protected static final String DEFAULT_ORB = "RMI";
/*     */   private static final String JONAS_DEPLOY_CLASS_NAME = "org.objectweb.jonas.adm.JonasAdmin";
/*  56 */   private static final String[] VALID_ACTIONS = new String[] { "delete", "deploy", "list", "undeploy", "update" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File jonasroot;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private String orb = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String davidHost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int davidPort;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDavidhost(String inValue) {
/*  86 */     this.davidHost = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDavidport(int inValue) {
/*  96 */     this.davidPort = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJonasroot(File inValue) {
/* 106 */     this.jonasroot = inValue;
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
/*     */   
/*     */   public void setOrb(String inValue) {
/* 120 */     this.orb = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 130 */     Path aClassPath = super.getClasspath();
/*     */     
/* 132 */     if (aClassPath == null) {
/* 133 */       aClassPath = new Path(getTask().getProject());
/*     */     }
/* 135 */     if (this.orb != null) {
/* 136 */       String aOrbJar = (new File(this.jonasroot, "lib/" + this.orb + "_jonas.jar")).toString();
/* 137 */       String aConfigDir = (new File(this.jonasroot, "config/")).toString();
/* 138 */       Path aJOnASOrbPath = new Path(aClassPath.getProject(), aOrbJar + File.pathSeparator + aConfigDir);
/*     */       
/* 140 */       aClassPath.append(aJOnASOrbPath);
/*     */     } 
/* 142 */     return aClassPath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateAttributes() throws BuildException {
/* 162 */     Java java = getJava();
/*     */     
/* 164 */     String action = getTask().getAction();
/* 165 */     if (action == null) {
/* 166 */       throw new BuildException("The \"action\" attribute must be set");
/*     */     }
/*     */     
/* 169 */     if (!isActionValid()) {
/* 170 */       throw new BuildException("Invalid action \"%s\" passed", new Object[] { action });
/*     */     }
/*     */     
/* 173 */     if (getClassName() == null) {
/* 174 */       setClassName("org.objectweb.jonas.adm.JonasAdmin");
/*     */     }
/*     */     
/* 177 */     if (this.jonasroot == null || this.jonasroot.isDirectory()) {
/* 178 */       java.createJvmarg().setValue("-Dinstall.root=" + this.jonasroot);
/* 179 */       java.createJvmarg().setValue("-Djava.security.policy=" + this.jonasroot + "/config/java.policy");
/*     */ 
/*     */       
/* 182 */       if ("DAVID".equals(this.orb)) {
/* 183 */         java.createJvmarg().setValue("-Dorg.omg.CORBA.ORBClass=org.objectweb.david.libs.binding.orbs.iiop.IIOPORB");
/*     */         
/* 185 */         java.createJvmarg().setValue("-Dorg.omg.CORBA.ORBSingletonClass=org.objectweb.david.libs.binding.orbs.ORBSingletonClass");
/*     */         
/* 187 */         java.createJvmarg().setValue("-Djavax.rmi.CORBA.StubClass=org.objectweb.david.libs.stub_factories.rmi.StubDelegate");
/*     */         
/* 189 */         java.createJvmarg().setValue("-Djavax.rmi.CORBA.PortableRemoteObjectClass=org.objectweb.david.libs.binding.rmi.ORBPortableRemoteObjectDelegate");
/*     */         
/* 191 */         java.createJvmarg().setValue("-Djavax.rmi.CORBA.UtilClass=org.objectweb.david.libs.helpers.RMIUtilDelegate");
/*     */         
/* 193 */         java.createJvmarg().setValue("-Ddavid.CosNaming.default_method=0");
/* 194 */         java.createJvmarg().setValue("-Ddavid.rmi.ValueHandlerClass=com.sun.corba.se.internal.io.ValueHandlerImpl");
/*     */         
/* 196 */         if (this.davidHost != null) {
/* 197 */           java.createJvmarg().setValue("-Ddavid.CosNaming.default_host=" + this.davidHost);
/*     */         }
/*     */         
/* 200 */         if (this.davidPort != 0) {
/* 201 */           java.createJvmarg().setValue("-Ddavid.CosNaming.default_port=" + this.davidPort);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 207 */     if (getServer() != null) {
/* 208 */       java.createArg().setLine("-n " + getServer());
/*     */     }
/*     */     
/* 211 */     if ("deploy".equals(action) || "update"
/* 212 */       .equals(action) || "redeploy"
/* 213 */       .equals(action)) {
/* 214 */       java.createArg().setLine("-a " + getTask().getSource());
/* 215 */     } else if (action.equals("delete") || action.equals("undeploy")) {
/* 216 */       java.createArg().setLine("-r " + getTask().getSource());
/* 217 */     } else if (action.equals("list")) {
/* 218 */       java.createArg().setValue("-l");
/*     */     } 
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
/*     */   
/*     */   protected boolean isActionValid() {
/* 233 */     String action = getTask().getAction();
/*     */     
/* 235 */     for (String validAction : VALID_ACTIONS) {
/* 236 */       if (action.equals(validAction)) {
/* 237 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 241 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/j2ee/JonasHotDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */