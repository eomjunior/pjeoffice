/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import com.github.signer4j.IHashAlgorithm;
/*    */ import com.github.signer4j.ISignatureAlgorithm;
/*    */ import com.github.signer4j.imp.HashAlgorithm;
/*    */ import com.github.signer4j.imp.SignatureAlgorithm;
/*    */ import com.github.taskresolver4j.exception.TaskParameterInvalidException;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class PjeTaskChecker
/*    */ {
/*    */   public static void throwIf(Supplier<Boolean> predicate, String message, Object... extra) throws TaskParameterInvalidException {
/* 47 */     throwIf((predicate == null || ((Boolean)predicate.get()).booleanValue()), message, extra);
/*    */   }
/*    */   
/*    */   public static void throwIf(boolean condition, String message, Object... extra) throws TaskParameterInvalidException {
/* 51 */     if (condition) {
/* 52 */       throw new TaskParameterInvalidException(String.format(message, extra));
/*    */     }
/*    */   }
/*    */   
/*    */   public static void throwIf(boolean condition, String paramName) throws TaskParameterInvalidException {
/* 57 */     if (condition) {
/* 58 */       throw new TaskParameterInvalidException("Parâmetro '" + paramName + "' não informado!");
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T> T checkIfPresent(Optional<T> optional, String paramName) throws TaskParameterInvalidException {
/* 63 */     throwIf(!((Optional)checkIfNull(optional, paramName)).isPresent(), paramName);
/* 64 */     return optional.get();
/*    */   }
/*    */   
/*    */   public static <T> T checkIfNull(T object, String paramName) throws TaskParameterInvalidException {
/* 68 */     throwIf((object == null), paramName);
/* 69 */     return object;
/*    */   }
/*    */   
/*    */   public static ISignatureAlgorithm checkIfSupportedSig(Optional<String> algorithm, String paramName) throws TaskParameterInvalidException {
/* 73 */     return checkIfSupportedSig(checkIfPresent(algorithm, paramName), paramName);
/*    */   }
/*    */   
/*    */   public static IHashAlgorithm checkIfSupportedHash(Optional<String> algorithm, String paramName) throws TaskParameterInvalidException {
/* 77 */     return checkIfSupportedHash(checkIfPresent(algorithm, paramName), paramName);
/*    */   }
/*    */   
/*    */   public static ISignatureAlgorithm checkIfSupportedSig(String algorithm, String paramName) throws TaskParameterInvalidException {
/* 81 */     throwIf(!SignatureAlgorithm.isSupported(algorithm), "Algoritmo '%s' não é suportado", new Object[] { algorithm });
/* 82 */     return SignatureAlgorithm.from(algorithm).get();
/*    */   }
/*    */   
/*    */   private static IHashAlgorithm checkIfSupportedHash(String algorithm, String paramName) throws TaskParameterInvalidException {
/* 86 */     throwIf(!HashAlgorithm.isSupported(algorithm), "Algoritmo '%s' não é suportado", new Object[] { algorithm });
/* 87 */     return HashAlgorithm.get(algorithm).get();
/*    */   }
/*    */   
/*    */   public static <T> List<T> checkIfNotEmpty(List<T> content, String paramName) throws TaskParameterInvalidException {
/* 91 */     throwIf((content == null || content.isEmpty()), paramName);
/* 92 */     return content;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeTaskChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */