/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.function.Functions;
/*     */ import java.awt.Window;
/*     */ import java.io.File;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Dialogs
/*     */ {
/*     */   static {
/*  64 */     UIManager.put("OptionPane.cancelButtonText", "Cancelar");
/*  65 */     UIManager.put("OptionPane.noButtonText", "Não");
/*  66 */     UIManager.put("OptionPane.okButtonText", "OK");
/*  67 */     UIManager.put("OptionPane.yesButtonText", "Sim");
/*     */   }
/*     */   
/*     */   public enum Choice {
/*  71 */     YES,
/*  72 */     NO,
/*  73 */     CANCEL;
/*     */   }
/*     */   
/*     */   private static <T> T invoke(Function<JFrame, T> function) {
/*  77 */     JFrame top = new JFrame("");
/*  78 */     top.setType(Window.Type.UTILITY);
/*  79 */     top.setAlwaysOnTop(true);
/*     */     try {
/*  81 */       return function.apply(top);
/*     */     } finally {
/*  83 */       top.dispose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> T consume(Consumer<JFrame> consumer) {
/*  88 */     return invoke(Functions.toFunction(consumer));
/*     */   }
/*     */   
/*     */   public static String input(String message, Object defaultValue) {
/*  92 */     return invoke(f -> JOptionPane.showInputDialog(f, message, defaultValue));
/*     */   }
/*     */   
/*     */   public static void warning(String message) {
/*  96 */     consume(f -> JOptionPane.showMessageDialog(f, message, "Informação", 2));
/*     */   }
/*     */   
/*     */   public static void info(String message) {
/* 100 */     consume(f -> JOptionPane.showMessageDialog(f, message, "Informação", 1));
/*     */   }
/*     */   
/*     */   public static void error(String message) {
/* 104 */     consume(f -> JOptionPane.showMessageDialog(f, message, "Erro", 0));
/*     */   }
/*     */   
/*     */   public static Choice yesNo(String message, String title, boolean cancelOption) {
/* 108 */     int options = cancelOption ? 1 : 0;
/* 109 */     int answer = ((Integer)invoke(f -> Integer.valueOf(JOptionPane.showConfirmDialog(f, message, title, options)))).intValue();
/* 110 */     switch (answer) {
/*     */       case 0:
/* 112 */         return Choice.YES;
/*     */       case 1:
/* 114 */         return Choice.NO;
/*     */       case 2:
/* 116 */         return Choice.CANCEL;
/*     */     } 
/* 118 */     return Choice.CANCEL;
/*     */   }
/*     */   
/*     */   public static String fileDialog(String extensionDescription, String extension, boolean openFile) {
/* 122 */     JFileChooser chooser = new JFileChooser();
/* 123 */     FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionDescription + " (*." + extension + ")", new String[] { extension });
/* 124 */     chooser.setFileFilter(filter);
/* 125 */     String fileName = null;
/*     */     while (true) {
/* 127 */       int result = openFile ? chooser.showOpenDialog(null) : chooser.showSaveDialog(null);
/* 128 */       if (result != 0) {
/* 129 */         return null;
/*     */       }
/* 131 */       fileName = chooser.getSelectedFile().getAbsolutePath();
/* 132 */       File f = new File(fileName);
/* 133 */       if (f.exists()) {
/* 134 */         if (!openFile) {
/* 135 */           String shortFileName = chooser.getSelectedFile().getName();
/* 136 */           Choice replaceFile = getBoolean(shortFileName + " já existe. Gosaria de substituí-lo?", "Escolha um arquivo", false);
/* 137 */           if (replaceFile == Choice.YES) {
/*     */             break;
/*     */           }
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 144 */       if (openFile) {
/* 145 */         error("Arquivo não existe. Por favor tente novamente!");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 151 */     return fileName;
/*     */   }
/*     */   
/*     */   public static <T> Optional<T> getOption(String message, T[] options) {
/* 155 */     return getOption(message, options, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Optional<T> getOption(String message, T[] options, T defaultOption) {
/* 160 */     return invoke(f -> Optional.ofNullable(JOptionPane.showInputDialog(f, message, "Opções", 3, null, options, defaultOption)));
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
/*     */   public static Integer getInteger(String message, Integer defaultValue) {
/* 174 */     return getInteger(message, defaultValue, null, null);
/*     */   }
/*     */   public static Integer getInteger(String message, Integer defaultValue, Integer min, Integer max) {
/*     */     Integer input;
/*     */     while (true) {
/* 179 */       String textInput = input(message, defaultValue);
/* 180 */       if (textInput == null) {
/* 181 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 185 */         input = Integer.valueOf(Integer.parseInt(textInput));
/* 186 */       } catch (NumberFormatException ex) {
/* 187 */         if (textInput.equals("")) {
/* 188 */           error("Informe um número"); continue;
/*     */         } 
/* 190 */         error("Valor inválido. Digite um número!");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 195 */       if (min != null && input.intValue() < min.intValue()) {
/* 196 */         error("O número deve ser maior ou igual a " + min);
/*     */         
/*     */         continue;
/*     */       } 
/* 200 */       if (max != null && input.intValue() > max.intValue()) {
/* 201 */         error("O número deve ser menor ou igual a " + max); continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 205 */     return input;
/*     */   }
/*     */   
/*     */   public static Double getDouble(String message, Double defaultValue, Double min, Double max) {
/*     */     Double input;
/*     */     while (true) {
/* 211 */       String textInput = input(message, defaultValue);
/* 212 */       if (textInput == null) {
/* 213 */         return null;
/*     */       }
/*     */       
/*     */       try {
/* 217 */         input = Double.valueOf(Double.parseDouble(textInput));
/* 218 */       } catch (NumberFormatException ex) {
/* 219 */         if (textInput.equals("")) {
/* 220 */           error("Informe um número"); continue;
/*     */         } 
/* 222 */         error("Valor inválido. Digite um número");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 227 */       if (min != null && input.doubleValue() < min.doubleValue()) {
/* 228 */         error("O número deve ser maior ou igual a " + min);
/*     */         
/*     */         continue;
/*     */       } 
/* 232 */       if (max != null && input.doubleValue() > max.doubleValue()) {
/* 233 */         error("O número deve ser menor ou igual a " + max); continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 237 */     return input;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String message, String defaultValue) {
/* 243 */     return getString(message, defaultValue, false);
/*     */   }
/*     */   public static String getString(String message, String defaultValue, boolean required) {
/*     */     String textInput;
/*     */     while (true) {
/* 248 */       textInput = input(message, defaultValue);
/* 249 */       if (textInput == null) {
/* 250 */         return null;
/*     */       }
/* 252 */       if (required && textInput.equals("")) {
/* 253 */         error("Digite a informação."); continue;
/*     */       }  break;
/*     */     } 
/* 256 */     return textInput;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Choice getBoolean(String message, String title) {
/* 261 */     return getBoolean(message, title, true);
/*     */   }
/*     */   
/*     */   public static Choice getBoolean(String message, String title, boolean cancelOption) {
/* 265 */     return yesNo(message, title, cancelOption);
/*     */   }
/*     */   
/*     */   public static boolean isValidEmailAddress(String email) {
/* 269 */     String[] tokens = email.split("@");
/* 270 */     if (tokens.length != 2) {
/* 271 */       return false;
/*     */     }
/*     */     
/* 274 */     for (int token = 0; token < 2; token++) {
/* 275 */       int tokenLength = tokens[token].length();
/* 276 */       if (tokens[token].charAt(0) == '.' || tokens[token].charAt(tokenLength - 1) == '.') {
/* 277 */         return false;
/*     */       }
/* 279 */       String validCharacters = (token == 0) ? "abcdefghijklmnopqrstuvwxyz0123456789!#$%&'*+-/=?^_`{|}~." : "abcdefghijklmnopqrstuvwxyz0123456789-.";
/*     */ 
/*     */       
/* 282 */       tokens[token].toLowerCase();
/* 283 */       for (int charNum = 0; charNum < tokenLength; charNum++) {
/* 284 */         if (validCharacters.indexOf(tokens[token].charAt(charNum)) == -1) {
/* 285 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     return true;
/*     */   }
/*     */   
/*     */   public static String getEmailAddress(String message, String defaultValue) {
/* 294 */     return getEmailAddress(message, defaultValue, false);
/*     */   }
/*     */   public static String getEmailAddress(String message, String defaultValue, boolean required) {
/*     */     String textInput;
/*     */     while (true) {
/* 299 */       textInput = getString(message, defaultValue, required);
/* 300 */       if (textInput == null) {
/* 301 */         return null;
/*     */       }
/* 303 */       if (textInput.equals("") && !required) {
/* 304 */         return "";
/*     */       }
/* 306 */       if (!isValidEmailAddress(textInput)) {
/* 307 */         error("Please enter a valid email address."); continue;
/*     */       }  break;
/*     */     } 
/* 310 */     return textInput;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isValidTelephoneNumber(String telephoneNumber) {
/* 315 */     int stringLength = telephoneNumber.length();
/* 316 */     for (int charNum = 0; charNum < stringLength; charNum++) {
/* 317 */       char character = telephoneNumber.charAt(charNum);
/* 318 */       if (!Character.isDigit(character))
/*     */       {
/* 320 */         if (character != ' ')
/*     */         {
/*     */           
/* 323 */           return false; }  } 
/*     */     } 
/* 325 */     return true;
/*     */   }
/*     */   
/*     */   public static String getTelephoneNumber(String message, String defaultValue) {
/* 329 */     return getTelephoneNumber(message, defaultValue, false);
/*     */   }
/*     */   public static String getTelephoneNumber(String message, String defaultValue, boolean required) {
/*     */     String textInput;
/*     */     while (true) {
/* 334 */       textInput = getString(message, defaultValue, required);
/* 335 */       if (textInput == null) {
/* 336 */         return null;
/*     */       }
/* 338 */       if (textInput.equals("") && !required) {
/* 339 */         return "";
/*     */       }
/* 341 */       if (!isValidTelephoneNumber(textInput)) {
/* 342 */         error("Informe um número de telefone válido"); continue;
/*     */       }  break;
/*     */     } 
/* 345 */     return textInput;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 350 */     SwingUtilities.invokeLater(() -> {
/*     */           Choice c = yesNo("LEONARDO OLIVEIRA", "titulo", false);
/*     */           System.out.println(c.toString());
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/Dialogs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */