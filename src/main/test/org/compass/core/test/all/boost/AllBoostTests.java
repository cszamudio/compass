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

package org.compass.core.test.all.boost;

import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.test.AbstractTestCase;

/**
 * This test verifies support for specific boost fields taken into account when used
 * within the all query.
 *
 * @author kimchy
 */
public class AllBoostTests extends AbstractTestCase {

    protected String[] getMappings() {
        return new String[]{"all/boost/mapping.cpm.xml"};
    }

    public void testValue1BoostLevel2() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        A a = new A();
        a.id = 1;
        a.value1 = "test2";
        a.value2 = "test1";
        session.save("anoboost", a);
        session.save("a1", a);

        a = new A();
        a.id = 2;
        a.value1 = "test1";
        a.value2 = "test2";
        session.save("anoboost", a);
        session.save("a1", a);

        CompassHits hits = session.queryBuilder().queryString("test1").toQuery().setAliases("anoboost").hits();
        assertEquals(1, ((A) hits.data(0)).id);
        assertEquals(2, ((A) hits.data(1)).id);
        hits = session.queryBuilder().queryString("test1").toQuery().setAliases("a1").hits();
        assertEquals(2, ((A) hits.data(0)).id);
        assertEquals(1, ((A) hits.data(1)).id);

        tr.commit();
        session.close();
    }
}