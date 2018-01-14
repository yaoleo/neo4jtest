package Baike;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/9
 */

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NodeEntity
public class Entity {

    private Long id;
    private String name;

    @Properties
    private Map<String, String> properties = new HashMap<>();

    public void setProperties(Map<String, String> properties){
        this.properties = properties;
    }

    @Relationship(type = "FRIEND", direction = "OUTGOING")
    private Set<Entity> friends;

    public Entity() {}
    public Entity(String name) { this.name = name; }

    public void knows(Entity friend) { friends.add(friend); }


    public void setProperties(String propertyname, String subjectname) {
        this.properties.put(propertyname,subjectname);
    }

    public Map<String,String> getProperties() {
        return  this.properties;
    }
}
