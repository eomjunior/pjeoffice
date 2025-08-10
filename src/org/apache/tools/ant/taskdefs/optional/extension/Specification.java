/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.util.DeweyDecimal;
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
/*     */ public final class Specification
/*     */ {
/*     */   private static final String MISSING = "Missing ";
/*  51 */   public static final Attributes.Name SPECIFICATION_TITLE = Attributes.Name.SPECIFICATION_TITLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final Attributes.Name SPECIFICATION_VERSION = Attributes.Name.SPECIFICATION_VERSION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static final Attributes.Name SPECIFICATION_VENDOR = Attributes.Name.SPECIFICATION_VENDOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final Attributes.Name IMPLEMENTATION_TITLE = Attributes.Name.IMPLEMENTATION_TITLE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final Attributes.Name IMPLEMENTATION_VENDOR = Attributes.Name.IMPLEMENTATION_VENDOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final Compatibility COMPATIBLE = new Compatibility("COMPATIBLE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static final Compatibility REQUIRE_SPECIFICATION_UPGRADE = new Compatibility("REQUIRE_SPECIFICATION_UPGRADE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public static final Compatibility REQUIRE_VENDOR_SWITCH = new Compatibility("REQUIRE_VENDOR_SWITCH");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final Compatibility REQUIRE_IMPLEMENTATION_CHANGE = new Compatibility("REQUIRE_IMPLEMENTATION_CHANGE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final Compatibility INCOMPATIBLE = new Compatibility("INCOMPATIBLE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String specificationTitle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeweyDecimal specificationVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String specificationVendor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationTitle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationVendor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String[] sections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Specification[] getSpecifications(Manifest manifest) throws ParseException {
/* 171 */     if (null == manifest) {
/* 172 */       return new Specification[0];
/*     */     }
/* 174 */     List<Specification> results = new ArrayList<>();
/*     */     
/* 176 */     for (Map.Entry<String, Attributes> e : manifest.getEntries().entrySet()) {
/*     */       
/* 178 */       Objects.requireNonNull(results); Optional.<Specification>ofNullable(getSpecification(e.getKey(), e.getValue())).ifPresent(results::add);
/*     */     } 
/* 180 */     return removeDuplicates(results)
/* 181 */       .<Specification>toArray(new Specification[0]);
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
/*     */   
/*     */   public Specification(String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor) {
/* 202 */     this(specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Specification(String specificationTitle, String specificationVersion, String specificationVendor, String implementationTitle, String implementationVersion, String implementationVendor, String[] sections) {
/* 227 */     this.specificationTitle = specificationTitle;
/* 228 */     this.specificationVendor = specificationVendor;
/*     */     
/* 230 */     if (null != specificationVersion) {
/*     */       try {
/* 232 */         this.specificationVersion = new DeweyDecimal(specificationVersion);
/*     */       }
/* 234 */       catch (NumberFormatException nfe) {
/* 235 */         throw new IllegalArgumentException("Bad specification version format '" + specificationVersion + "' in '" + specificationTitle + "'. (Reason: " + nfe + ")");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     this.implementationTitle = implementationTitle;
/* 243 */     this.implementationVendor = implementationVendor;
/* 244 */     this.implementationVersion = implementationVersion;
/*     */     
/* 246 */     if (null == this.specificationTitle) {
/* 247 */       throw new NullPointerException("specificationTitle");
/*     */     }
/* 249 */     this.sections = (sections == null) ? null : (String[])sections.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecificationTitle() {
/* 258 */     return this.specificationTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecificationVendor() {
/* 267 */     return this.specificationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationTitle() {
/* 276 */     return this.implementationTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeweyDecimal getSpecificationVersion() {
/* 285 */     return this.specificationVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationVendor() {
/* 294 */     return this.implementationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationVersion() {
/* 303 */     return this.implementationVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSections() {
/* 314 */     return (this.sections == null) ? null : (String[])this.sections.clone();
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
/*     */   public Compatibility getCompatibilityWith(Specification other) {
/* 328 */     if (!this.specificationTitle.equals(other.getSpecificationTitle())) {
/* 329 */       return INCOMPATIBLE;
/*     */     }
/*     */ 
/*     */     
/* 333 */     DeweyDecimal otherSpecificationVersion = other.getSpecificationVersion();
/* 334 */     if (null != this.specificationVersion && (null == otherSpecificationVersion || 
/* 335 */       !isCompatible(this.specificationVersion, otherSpecificationVersion))) {
/* 336 */       return REQUIRE_SPECIFICATION_UPGRADE;
/*     */     }
/*     */ 
/*     */     
/* 340 */     if (null != this.implementationVendor && 
/* 341 */       !this.implementationVendor.equals(other.getImplementationVendor())) {
/* 342 */       return REQUIRE_VENDOR_SWITCH;
/*     */     }
/*     */ 
/*     */     
/* 346 */     if (null != this.implementationVersion && 
/* 347 */       !this.implementationVersion.equals(other.getImplementationVersion())) {
/* 348 */       return REQUIRE_IMPLEMENTATION_CHANGE;
/*     */     }
/*     */ 
/*     */     
/* 352 */     return COMPATIBLE;
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
/*     */   public boolean isCompatibleWith(Specification other) {
/* 364 */     return (COMPATIBLE == getCompatibilityWith(other));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 374 */     String format = "%s: %s%n";
/*     */     
/* 376 */     StringBuilder sb = new StringBuilder(String.format("%s: %s%n", new Object[] { SPECIFICATION_TITLE, this.specificationTitle }));
/*     */ 
/*     */     
/* 379 */     if (null != this.specificationVersion) {
/* 380 */       sb.append(String.format("%s: %s%n", new Object[] { SPECIFICATION_VERSION, this.specificationVersion }));
/*     */     }
/*     */     
/* 383 */     if (null != this.specificationVendor) {
/* 384 */       sb.append(String.format("%s: %s%n", new Object[] { SPECIFICATION_VENDOR, this.specificationVendor }));
/*     */     }
/*     */     
/* 387 */     if (null != this.implementationTitle) {
/* 388 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_TITLE, this.implementationTitle }));
/*     */     }
/*     */     
/* 391 */     if (null != this.implementationVersion) {
/* 392 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_VERSION, this.implementationVersion }));
/*     */     }
/*     */     
/* 395 */     if (null != this.implementationVendor) {
/* 396 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_VENDOR, this.implementationVendor }));
/*     */     }
/*     */     
/* 399 */     return sb.toString();
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
/*     */   private boolean isCompatible(DeweyDecimal first, DeweyDecimal second) {
/* 411 */     return first.isGreaterThanOrEqual(second);
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
/*     */   private static List<Specification> removeDuplicates(List<Specification> list) {
/* 425 */     List<Specification> results = new ArrayList<>();
/* 426 */     List<String> sections = new ArrayList<>();
/* 427 */     while (!list.isEmpty()) {
/* 428 */       Specification specification = list.remove(0);
/*     */       
/* 430 */       for (Iterator<Specification> iterator = list.iterator(); iterator.hasNext(); ) {
/* 431 */         Specification other = iterator.next();
/* 432 */         if (isEqual(specification, other)) {
/* 433 */           Optional.<String[]>ofNullable(other.getSections())
/* 434 */             .ifPresent(s -> Collections.addAll(sections, s));
/* 435 */           iterator.remove();
/*     */         } 
/*     */       } 
/* 438 */       results.add(mergeInSections(specification, sections));
/*     */       
/* 440 */       sections.clear();
/*     */     } 
/* 442 */     return results;
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
/*     */   private static boolean isEqual(Specification specification, Specification other) {
/* 455 */     return (specification
/* 456 */       .getSpecificationTitle().equals(other.getSpecificationTitle()) && specification
/* 457 */       .getSpecificationVersion().isEqual(other.getSpecificationVersion()) && specification
/* 458 */       .getSpecificationVendor().equals(other.getSpecificationVendor()) && specification
/* 459 */       .getImplementationTitle().equals(other.getImplementationTitle()) && specification
/* 460 */       .getImplementationVersion().equals(other.getImplementationVersion()) && specification
/* 461 */       .getImplementationVendor().equals(other.getImplementationVendor()));
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
/*     */   private static Specification mergeInSections(Specification specification, List<String> sectionsToAdd) {
/* 474 */     if (sectionsToAdd.isEmpty()) {
/* 475 */       return specification;
/*     */     }
/* 477 */     Stream<String> sections = Stream.concat(Optional.<String[]>ofNullable(specification.getSections())
/* 478 */         .map(Stream::of).orElse(Stream.empty()), sectionsToAdd.stream());
/*     */     
/* 480 */     return new Specification(specification.getSpecificationTitle(), specification
/* 481 */         .getSpecificationVersion().toString(), specification
/* 482 */         .getSpecificationVendor(), specification
/* 483 */         .getImplementationTitle(), specification
/* 484 */         .getImplementationVersion(), specification
/* 485 */         .getImplementationVendor(), sections.<String>toArray(x$0 -> new String[x$0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getTrimmedString(String value) {
/* 495 */     return (value == null) ? null : value.trim();
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
/*     */   private static Specification getSpecification(String section, Attributes attributes) throws ParseException {
/* 510 */     String name = getTrimmedString(attributes.getValue(SPECIFICATION_TITLE));
/* 511 */     if (null == name) {
/* 512 */       return null;
/*     */     }
/*     */     
/* 515 */     String specVendor = getTrimmedString(attributes.getValue(SPECIFICATION_VENDOR));
/* 516 */     if (null == specVendor) {
/* 517 */       throw new ParseException("Missing " + SPECIFICATION_VENDOR, 0);
/*     */     }
/*     */     
/* 520 */     String specVersion = getTrimmedString(attributes.getValue(SPECIFICATION_VERSION));
/* 521 */     if (null == specVersion) {
/* 522 */       throw new ParseException("Missing " + SPECIFICATION_VERSION, 0);
/*     */     }
/*     */     
/* 525 */     String impTitle = getTrimmedString(attributes.getValue(IMPLEMENTATION_TITLE));
/* 526 */     if (null == impTitle) {
/* 527 */       throw new ParseException("Missing " + IMPLEMENTATION_TITLE, 0);
/*     */     }
/*     */     
/* 530 */     String impVersion = getTrimmedString(attributes.getValue(IMPLEMENTATION_VERSION));
/* 531 */     if (null == impVersion) {
/* 532 */       throw new ParseException("Missing " + IMPLEMENTATION_VERSION, 0);
/*     */     }
/*     */     
/* 535 */     String impVendor = getTrimmedString(attributes.getValue(IMPLEMENTATION_VENDOR));
/* 536 */     if (null == impVendor) {
/* 537 */       throw new ParseException("Missing " + IMPLEMENTATION_VENDOR, 0);
/*     */     }
/*     */     
/* 540 */     return new Specification(name, specVersion, specVendor, impTitle, impVersion, impVendor, new String[] { section });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/Specification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */