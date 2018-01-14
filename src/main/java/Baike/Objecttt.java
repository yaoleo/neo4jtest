package Baike;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/13
 */

@NodeEntity
public class Objecttt {
    private Long id;
    private  String name;
    private  String uri;

    @Properties
    private Map<String, String> properties = new HashMap<>();

    public Objecttt(String name) {this.name = name;}

    public String toString() {
        return this.name +"uri:"+ this.uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(String property, String subject){
        this.properties.put(property,subject);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    //@Relationship

}
