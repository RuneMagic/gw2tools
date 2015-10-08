package com.runemagic.gw2tools.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GW2APIField
{
	/**
	 * The referenced GW2 API field's name.
	 */
	String name();

	/**
	 * The field's type.<br>
	 * If not specified, the property's type is used.
	 */
	GW2APIFieldType sourceType() default GW2APIFieldType.DEFAULT;

	/**
	 * The array item type.
	 */
	GW2APIFieldType itemType() default GW2APIFieldType.DEFAULT;

	/**
	 * The Java class this field should be mapped.
	 */
	Class<?> targetType() default Object.class;

	/**
	 * GW2API factory method for the Object source type instance.
	 */
	String factory() default "";

	/**
	 * The name of the resource that contains this field.<br>
	 * If it's not specified or empty, the parser will try to fetch the
	 * field data from every resource (this may lead to conflicts if 2
	 * or more resources have the same field).
	 */
	String resource() default "";

	/**
	 * True if the field is optional.
	 */
	boolean optional() default false;

}
