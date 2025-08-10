/*    */ package br.jus.cnj.pje.office.shell;
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
/*    */ class Strings
/*    */ {
/* 31 */   private static final char[] WINDOWS_SEARCH_CHAR_BUGS = new char[] { '¸', '~' };
/*    */ 
/*    */ 
/*    */   
/* 35 */   private static final char[] WINDOWS_REPLACED_CHAR_BUGS = new char[] { '̧', '̃' };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String trim(String text) {
/* 42 */     return (text == null) ? "" : text.trim();
/*    */   }
/*    */   
/*    */   public static String at(String[] args, int index) {
/* 46 */     return (args == null || index >= args.length) ? "" : trim(args[index]);
/*    */   }
/*    */   
/*    */   public static String at(String[] args, int index, String defaultIfNothing) {
/* 50 */     return (args == null || index >= args.length) ? trim(defaultIfNothing) : trim(args[index]);
/*    */   }
/*    */   
/*    */   public static String sanitize(String path) {
/* 54 */     for (int i = 0; i < WINDOWS_SEARCH_CHAR_BUGS.length; ) {
/* 55 */       int idx = path.indexOf(WINDOWS_SEARCH_CHAR_BUGS[i]);
/* 56 */       if (idx >= 0) {
/* 57 */         path = path.substring(0, idx) + WINDOWS_REPLACED_CHAR_BUGS[i] + path.substring(idx + 1); continue;
/*    */       } 
/* 59 */       i++;
/*    */     } 
/*    */     
/* 62 */     return path;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/shell/Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */