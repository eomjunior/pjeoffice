/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.BuildListener;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.SubBuildListener;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.LogLevel;
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
/*     */ public class Recorder
/*     */   extends Task
/*     */   implements SubBuildListener
/*     */ {
/*  55 */   private String filename = null;
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Boolean append = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private Boolean start = null;
/*     */   
/*  66 */   private int loglevel = -1;
/*     */   
/*     */   private boolean emacsMode = false;
/*     */   
/*  70 */   private static Map<String, RecorderEntry> recorderEntries = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  81 */     getProject().addBuildListener((BuildListener)this);
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
/*     */   public void setName(String fname) {
/*  94 */     this.filename = fname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAction(ActionChoices action) {
/* 104 */     if (action.getValue().equalsIgnoreCase("start")) {
/* 105 */       this.start = Boolean.TRUE;
/*     */     } else {
/* 107 */       this.start = Boolean.FALSE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(boolean append) {
/* 117 */     this.append = append ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmacsMode(boolean emacsMode) {
/* 126 */     this.emacsMode = emacsMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoglevel(VerbosityLevelChoices level) {
/* 136 */     this.loglevel = level.getLevel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 147 */     if (this.filename == null) {
/* 148 */       throw new BuildException("No filename specified");
/*     */     }
/*     */     
/* 151 */     getProject().log("setting a recorder for name " + this.filename, 4);
/*     */ 
/*     */ 
/*     */     
/* 155 */     RecorderEntry recorder = getRecorder(this.filename, getProject());
/*     */     
/* 157 */     recorder.setMessageOutputLevel(this.loglevel);
/* 158 */     recorder.setEmacsMode(this.emacsMode);
/* 159 */     if (this.start != null) {
/* 160 */       if (this.start.booleanValue()) {
/* 161 */         recorder.reopenFile();
/* 162 */         recorder.setRecordState(this.start);
/*     */       } else {
/* 164 */         recorder.setRecordState(this.start);
/* 165 */         recorder.closeFile();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ActionChoices
/*     */     extends EnumeratedAttribute
/*     */   {
/* 178 */     private static final String[] VALUES = new String[] { "start", "stop" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 185 */       return VALUES;
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
/*     */   public static class VerbosityLevelChoices
/*     */     extends LogLevel {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RecorderEntry getRecorder(String name, Project proj) throws BuildException {
/* 208 */     RecorderEntry entry = recorderEntries.get(name);
/*     */     
/* 210 */     if (entry == null) {
/*     */       
/* 212 */       entry = new RecorderEntry(name);
/*     */       
/* 214 */       if (this.append == null) {
/* 215 */         entry.openFile(false);
/*     */       } else {
/* 217 */         entry.openFile(this.append.booleanValue());
/*     */       } 
/* 219 */       entry.setProject(proj);
/* 220 */       recorderEntries.put(name, entry);
/*     */     } 
/*     */     
/* 223 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subBuildStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetFinished(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskFinished(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageLogged(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildFinished(BuildEvent event) {
/* 288 */     cleanup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subBuildFinished(BuildEvent event) {
/* 298 */     if (event.getProject() == getProject()) {
/* 299 */       cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanup() {
/* 309 */     recorderEntries.entrySet().removeIf(e -> (((RecorderEntry)e.getValue()).getProject() == getProject()));
/* 310 */     getProject().removeBuildListener((BuildListener)this);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Recorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */