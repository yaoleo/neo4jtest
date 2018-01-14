package baike;
/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/9
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import utils.io.IOTool;

import java.util.*;

@SpringBootApplication
@EnableNeo4jRepositories
@Configuration
public class Application {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner demo(EntityRepository entityRepository) {
        return args -> {

            entityRepository.deleteAll();

            ArrayList<String> triples = IOTool.readLinesFromFile("C:\\Users\\yaoleo\\IdeaProjects\\neo4jtest\\src\\main\\resources\\NT_triplets.nt");
            log.info(String.valueOf(triples.size()));
            int count = 0;
            for(String triple :triples){
                String objecturi = triple.split(" ")[0];
                String propertyuri = triple.split(" ")[1];
                String subjectname  = triple.split(" ")[2];

                String[] propertylist = propertyuri.split("/");
                String propertyname = propertylist[propertylist.length-1];
                propertyname = propertyname.substring(0,propertyname.length()-1);

                String[] objectlist = objecturi.split("/");
                String objectname = objectlist[objectlist.length-1];
                objectname = objectname.substring(0,objectname.length()-1);

                Entity ss = new Entity(objectname);

                //List<Entity> issaved = personRepository.findByName(objectname);
                Entity issaved = entityRepository.findByName(objectname);
                if(null == issaved ){
                    count ++;
                    ss.setProperties(propertyname,subjectname);
                    entityRepository.save(ss);
                }else {
                    Map<String, String> savedproperties = issaved.getProperties();
                    //System.out.println(savedproperties);
                    entityRepository.delete(issaved);
                    ss.setProperties(savedproperties);
                    //personRepository.save(ss);
                    //Map<String, String> property = new HashMap<>();
                    //property.put(propertyname,subjectname);
                    ss.setProperties(propertyname,subjectname);
                    //System.out.println("sss"+ss.getProperties());
                    entityRepository.save(ss);
                }

                //System.out.println(issaved);

            }
            System.out.println(count);
            /*properties.put("saaa","ddd");
            roy.setProperties(properties);
            personRepository.save(greg);
            Entity issaved = personRepository.findByName("Greg");
            personRepository.save(roy);
            Entity issaved2 = personRepository.findByName("Greg");
            System.out.println(issaved);
            log.info("Before linking up with Neo4j...");

            //team.stream().forEach(person -> log.info("\t" + person.toString()));

            personRepository.save(greg);
            personRepository.save(roy);
            personRepository.save(craig);*/

        };
    }
/*
    @Bean
    CommandLineRunner SaveBaikeObject(ObjectttRepository objectttRepository){
        return args ->{
            objectttRepository.deleteAll();

            ArrayList<String> triples = IOTool.readLinesFromFile("C:\\Users\\yaoleo\\IdeaProjects\\neo4jtest\\src\\main\\resources\\NT.nt");
            log.info(String.valueOf(triples.size()));
            int count = 0;
            for(String triple :triples){
                String objecturi = triple.split(" ")[0];
                String propertyuri = triple.split(" ")[1];
                String subjectname  = triple.split(" ")[2];

                String[] propertylist = propertyuri.split("/");
                String propertyname = propertylist[propertylist.length-1];
                propertyname = propertyname.substring(0,propertyname.length()-1);

                String[] objectlist = objecturi.split("/");
                String objectname = objectlist[objectlist.length-1];
                objectname = objectname.substring(0,objectname.length()-1);


                Objecttt objecttt = new Objecttt(objectname);
                objecttt.setUri(objecturi);
                objectttRepository.save(objecttt);
                Objecttt issaved = objectttRepository.findByName(objectname);
                //Map<String, String> property = new HashMap<>();
                //objecttt.setProperties(propertyname,subjectname);
                //objectRepository.save(objecttt);
                //List<Objecttt> empty = [];
                System.out.println(issaved);
               /*if(null == issaved || issaved.size() ==0){
                    count ++;
                    //objecttt.setProperties(propertyname,subjectname);
                    objectRepository.save(objecttt);
                }else {
                    //Map<String, String> property = new HashMap<>();
                    //property.put(propertyname,subjectname);
                    //issaved.setProperties(propertyname,subjectname);
                    //objectRepository.save(issaved);
                }
            }
            System.out.println("TOTAL NODE:"+count);
        };
    }*/

}