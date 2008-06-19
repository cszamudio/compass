package org.compass.core.test.json.simple;

import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.Resource;
import org.compass.core.json.JsonObject;
import org.compass.core.json.RawAliasedJsonObject;
import org.compass.core.test.AbstractTestCase;

/**
 * @author kimchy
 */
public class SimpleJsonTests extends AbstractTestCase {

    protected String[] getMappings() {
        return new String[]{"json/simple/mapping.cpm.xml"};
    }

    public void testDotPath() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("a", "{id : 1, value : \"test\"}");
        session.save(jsonObject);

        assertEquals(1, session.find("a.value:test").length());

        tr.commit();
        session.close();
    }

    public void testSimpleJson() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("a", "{id : 1, value : \"test\"}");
        session.save(jsonObject);

        Resource resource = session.loadResource("a", 1);
        assertEquals("test", resource.getValue("value"));

        tr.commit();
        session.close();
    }

    public void testSimpleJsonWithContent() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("b", "{id : 1, value : \"test\"}");
        session.save(jsonObject);

        Resource resource = session.loadResource("b", 1);
        assertEquals("test", resource.getValue("value"));

        JsonObject obj = (JsonObject) session.load("b", 1);
        assertNotNull(obj);
        assertEquals(true, obj.keys().hasNext());

        // make another round, now without using RAW
        session.save("b", obj);
        obj = (JsonObject) session.load("b", 1);
        assertNotNull(obj);
        assertEquals(true, obj.keys().hasNext());

        tr.commit();
        session.close();
    }

    public void testSimpleJsonWithValueConverter() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("c", "{id : 1, int : 2, float: 1.2}");
        session.save(jsonObject);

        Resource resource = session.loadResource("c", 1);
        assertEquals("0002", resource.getValue("int"));
        assertEquals(new Integer(2), resource.getObject("int"));
        assertEquals("0001.20", resource.getValue("float"));
        assertEquals(new Float(1.2), resource.getObject("float"));
        assertEquals(true, resource.getProperty("float").isCompressed());

        assertEquals(1, session.find("c.int:2").length());

        tr.commit();
        session.close();
    }

    public void testNullValue() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("d", "{id : 1, value : null}");
        session.save(jsonObject);

        Resource resource = session.loadResource("d", 1);
        assertEquals("kablam", resource.getValue("value"));

        tr.commit();
        session.close();
    }

    public void testDoubleMapping() {
        CompassSession session = openSession();
        CompassTransaction tr = session.beginTransaction();

        RawAliasedJsonObject jsonObject = new RawAliasedJsonObject("e", "{id : 1, value : 2}");
        session.save(jsonObject);

        Resource resource = session.loadResource("e", 1);
        assertEquals(2, resource.getValues("value").length);

        tr.commit();
        session.close();
    }
}
