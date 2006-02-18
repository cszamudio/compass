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

package org.compass.core.config.process;

import java.util.Iterator;

import org.compass.core.config.CompassSettings;
import org.compass.core.converter.ConverterLookup;
import org.compass.core.engine.naming.PropertyNamingStrategy;
import org.compass.core.mapping.CompassMapping;
import org.compass.core.mapping.Mapping;
import org.compass.core.mapping.MappingException;
import org.compass.core.mapping.osem.ClassMapping;
import org.compass.core.mapping.osem.HasRefAliasMapping;

/**
 * @author kimchy
 */
public class ResolveRefAliasProcessor implements MappingProcessor {

    private CompassMapping compassMapping;

    public CompassMapping process(CompassMapping compassMapping, PropertyNamingStrategy namingStrategy,
                                  ConverterLookup converterLookup, CompassSettings settings) throws MappingException {
        this.compassMapping = compassMapping;
        for (Iterator rIt = compassMapping.mappingsIt(); rIt.hasNext();) {
            Mapping mapping = (Mapping) rIt.next();
            if (mapping instanceof ClassMapping) {
                ClassMapping classMapping = (ClassMapping) mapping;
                for (Iterator it = classMapping.mappingsIt(); it.hasNext();) {
                    Mapping innerMapping = (Mapping) it.next();
                    if (innerMapping instanceof HasRefAliasMapping) {
                        processMapping(classMapping, (HasRefAliasMapping) innerMapping);
                    }
                }
            }
        }
        return compassMapping;
    }

    void processMapping(ClassMapping classMapping, HasRefAliasMapping mapping) throws MappingException {
        if (mapping.getRefAlias() != null) {
            ClassMapping refClassMapping = (ClassMapping) compassMapping.getResourceMappingByAlias(mapping.getRefAlias());
            if (refClassMapping == null) {
                throw new MappingException("Failed to resolve ref-alias [" + mapping.getRefAlias() + "] for ["
                        + mapping.getName() + "] in alias [" + classMapping.getAlias() + "]");
            }
            mapping.setRefClassMapping(refClassMapping);
            return;
        }
        Class clazz = mapping.getRefClass();
        if (clazz == null) {
            clazz = mapping.getGetter().getReturnType();
        }
        if (clazz == null) {
            throw new MappingException("This should not happen");
        }
        ClassMapping refClassMapping = compassMapping.getClassMappingByClass(clazz.getName());
        if (refClassMapping == null) {
            throw new MappingException("Tried to resolve ref-alias for property [" + mapping.getName() + "] in alias [" +
                    classMapping.getAlias() + "], but no class mapping was found for [" + clazz.getName() + "]");
        }
        if (compassMapping.hasMultipleClassMapping(clazz.getName())) {
            throw new MappingException("Tried to resolve ref-alias for property [" + mapping.getName() + "] in alias [" +
                    classMapping.getAlias() + "], but there are multiple class mappings for [" + clazz.getName()
                    + "]. Please set the ref-alias explicitly.");
        }
        mapping.setRefAlias(refClassMapping.getAlias());
        mapping.setRefClassMapping(refClassMapping);
    }
}
