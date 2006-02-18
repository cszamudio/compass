/*
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.compass.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kimchy
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchableProperty {

    ManagedId managedId() default ManagedId.AUTO;

    ManagedIdIndex managedIdIndex() default ManagedIdIndex.NA;

    /**
     * The class type of the property. Mainly used for <code>Collection</code> properties, without
     * specific Generic type parameter.
     */
    Class type() default Object.class;

    boolean override() default true;

    /**
     * Converter that will apply to the property mapping. Not the generated
     * meta-data.
     */
    String propertyConverter() default "";

    // Generated MetaData definitions

    String name() default "";

    float boost() default 1.0f;

    Store store() default Store.YES;

    Index index() default Index.TOKENIZED;

    TermVector termVector() default TermVector.NO;

    Reverse reverse() default Reverse.NO;

    String analyzer() default "";

    boolean exceludeFromAll() default false;

    String converter() default "";

    /**
     * The format to apply to the value. Only applies to format-able converters
     * (like dates and numbers).
     */
    String format() default "";
}
