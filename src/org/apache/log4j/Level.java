/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
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
/*     */ public class Level
/*     */   extends Priority
/*     */   implements Serializable
/*     */ {
/*     */   public static final int TRACE_INT = 5000;
/*  53 */   public static final Level OFF = new Level(2147483647, "OFF", 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final Level FATAL = new Level(50000, "FATAL", 0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static final Level ERROR = new Level(40000, "ERROR", 3);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final Level WARN = new Level(30000, "WARN", 4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static final Level INFO = new Level(20000, "INFO", 6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Level DEBUG = new Level(10000, "DEBUG", 7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final Level TRACE = new Level(5000, "TRACE", 7);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static final Level ALL = new Level(-2147483648, "ALL", 7);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final long serialVersionUID = 3491141966387921974L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Level(int level, String levelStr, int syslogEquivalent) {
/* 107 */     super(level, levelStr, syslogEquivalent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg) {
/* 115 */     return toLevel(sArg, DEBUG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(int val) {
/* 124 */     return toLevel(val, DEBUG);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(int val, Level defaultLevel) {
/* 132 */     switch (val) {
/*     */       case -2147483648:
/* 134 */         return ALL;
/*     */       case 10000:
/* 136 */         return DEBUG;
/*     */       case 20000:
/* 138 */         return INFO;
/*     */       case 30000:
/* 140 */         return WARN;
/*     */       case 40000:
/* 142 */         return ERROR;
/*     */       case 50000:
/* 144 */         return FATAL;
/*     */       case 2147483647:
/* 146 */         return OFF;
/*     */       case 5000:
/* 148 */         return TRACE;
/*     */     } 
/* 150 */     return defaultLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Level toLevel(String sArg, Level defaultLevel) {
/* 159 */     if (sArg == null) {
/* 160 */       return defaultLevel;
/*     */     }
/* 162 */     String s = sArg.toUpperCase();
/*     */     
/* 164 */     if (s.equals("ALL"))
/* 165 */       return ALL; 
/* 166 */     if (s.equals("DEBUG"))
/* 167 */       return DEBUG; 
/* 168 */     if (s.equals("INFO"))
/* 169 */       return INFO; 
/* 170 */     if (s.equals("WARN"))
/* 171 */       return WARN; 
/* 172 */     if (s.equals("ERROR"))
/* 173 */       return ERROR; 
/* 174 */     if (s.equals("FATAL"))
/* 175 */       return FATAL; 
/* 176 */     if (s.equals("OFF"))
/* 177 */       return OFF; 
/* 178 */     if (s.equals("TRACE")) {
/* 179 */       return TRACE;
/*     */     }
/*     */ 
/*     */     
/* 183 */     if (s.equals("İNFO"))
/* 184 */       return INFO; 
/* 185 */     return defaultLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 196 */     s.defaultReadObject();
/* 197 */     this.level = s.readInt();
/* 198 */     this.syslogEquivalent = s.readInt();
/* 199 */     this.levelStr = s.readUTF();
/* 200 */     if (this.levelStr == null) {
/* 201 */       this.levelStr = "";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 212 */     s.defaultWriteObject();
/* 213 */     s.writeInt(this.level);
/* 214 */     s.writeInt(this.syslogEquivalent);
/* 215 */     s.writeUTF(this.levelStr);
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
/*     */   private Object readResolve() throws ObjectStreamException {
/* 229 */     if (getClass() == Level.class) {
/* 230 */       return toLevel(this.level);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 235 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/Level.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */