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

package org.compass.core.converter.extended;

import java.sql.Timestamp;
import java.util.Date;

import org.compass.core.converter.ConversionException;
import org.compass.core.converter.basic.DateConverter;
import org.compass.core.mapping.ResourcePropertyMapping;

/**
 * @author kimchy
 */
public class SqlTimestampConverter extends DateConverter {

    /**
     * Sql Timestamp has no default format, it uses the {@link java.sql.Time#toString()}.
     */
    protected String doGetDefaultFormat() {
        return null;
    }

    public String toString(Object o, ResourcePropertyMapping resourcePropertyMapping) throws ConversionException {
        if (hasFormatter) {
            return super.toString(o, resourcePropertyMapping);
        }
        return o.toString();
    }

    public Object fromString(String str, ResourcePropertyMapping resourcePropertyMapping) {
        if (hasFormatter) {
            Date date = (Date) super.fromString(str, resourcePropertyMapping);
            return new Timestamp(date.getTime());
        }
        return Timestamp.valueOf(str);
    }
}
