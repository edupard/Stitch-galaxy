/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.constraints;

/**
 *
 * @author tarasev
 */
import static com.sg.dto.constraints.CanvasSizeRequired.MAX_STITCHES_PER_INCH;
import static com.sg.dto.constraints.CanvasSizeRequired.MIN_STITCHES_PER_INCH;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@NotNull
@DecimalMin(MIN_STITCHES_PER_INCH)
@DecimalMax(MAX_STITCHES_PER_INCH)
@ReportAsSingleViolation
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CanvasSizeRequired {
    
    public static final String MIN_STITCHES_PER_INCH = "1";
    public static final String MAX_STITCHES_PER_INCH = "100";
    
    String message() default "canvas size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}