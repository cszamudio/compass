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

package org.compass.core.mapping.osem;

/**
 * @author kimchy
 */
public interface HasRefAliasMapping extends ObjectMapping {

    String getRefAlias();

    void setRefAlias(String refAlias);

    ClassMapping getRefClassMapping();

    void setRefClassMapping(ClassMapping refClassMapping);

    Class getRefClass();

    void setRefClass(Class refClass);
}
