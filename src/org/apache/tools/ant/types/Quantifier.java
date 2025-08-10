/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class Quantifier
/*     */   extends EnumeratedAttribute
/*     */ {
/*     */   private static final String[] VALUES;
/*     */   
/*     */   static {
/*  47 */     VALUES = (String[])Stream.<Predicate>of(Predicate.values()).map(Predicate::getNames).flatMap(Collection::stream).toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */   
/*  50 */   public static final Quantifier ALL = new Quantifier(Predicate.ALL);
/*     */ 
/*     */   
/*  53 */   public static final Quantifier ANY = new Quantifier(Predicate.ANY);
/*     */ 
/*     */   
/*  56 */   public static final Quantifier ONE = new Quantifier(Predicate.ONE);
/*     */ 
/*     */   
/*  59 */   public static final Quantifier MAJORITY = new Quantifier(Predicate.MAJORITY);
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Quantifier NONE = new Quantifier(Predicate.NONE);
/*     */   
/*     */   private enum Predicate {
/*  66 */     ALL("all", new String[] { "each", "every" })
/*     */     {
/*     */       boolean eval(int t, int f) {
/*  69 */         return (f == 0);
/*     */       }
/*     */     },
/*     */     
/*  73 */     ANY("any", new String[] { "some" })
/*     */     {
/*     */       boolean eval(int t, int f) {
/*  76 */         return (t > 0);
/*     */       }
/*     */     },
/*     */     
/*  80 */     ONE("one", new String[0])
/*     */     {
/*     */       boolean eval(int t, int f) {
/*  83 */         return (t == 1);
/*     */       }
/*     */     },
/*     */     
/*  87 */     MAJORITY("majority", new String[] { "most" })
/*     */     {
/*     */       boolean eval(int t, int f) {
/*  90 */         return (t > f);
/*     */       }
/*     */     },
/*     */     
/*  94 */     NONE("none", new String[0])
/*     */     {
/*     */       boolean eval(int t, int f) {
/*  97 */         return (t == 0);
/*     */       }
/*     */     };
/*     */     
/*     */     static Predicate get(String name) {
/* 102 */       return (Predicate)Stream.<Predicate>of(values()).filter(p -> p.names.contains(name))
/* 103 */         .findFirst()
/* 104 */         .orElseThrow(() -> new IllegalArgumentException(name));
/*     */     }
/*     */     
/*     */     final Set<String> names;
/*     */     
/*     */     Predicate(String primaryName, String... additionalNames) {
/* 110 */       Set<String> names = new LinkedHashSet<>();
/* 111 */       names.add(primaryName);
/* 112 */       Collections.addAll(names, additionalNames);
/* 113 */       this.names = Collections.unmodifiableSet(names);
/*     */     }
/*     */     
/*     */     Set<String> getNames() {
/* 117 */       return this.names;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean eval(int param1Int1, int param1Int2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Quantifier() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Quantifier(String value) {
/* 134 */     setValue(value);
/*     */   }
/*     */   
/*     */   private Quantifier(Predicate impl) {
/* 138 */     setValue(impl.getNames().iterator().next());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getValues() {
/* 147 */     return VALUES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(boolean[] b) {
/* 156 */     int t = 0;
/* 157 */     for (boolean bn : b) {
/* 158 */       if (bn) {
/* 159 */         t++;
/*     */       }
/*     */     } 
/* 162 */     return evaluate(t, b.length - t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(int t, int f) {
/* 172 */     int index = getIndex();
/* 173 */     if (index == -1) {
/* 174 */       throw new BuildException("Quantifier value not set.");
/*     */     }
/* 176 */     return Predicate.get(VALUES[index]).eval(t, f);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Quantifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */