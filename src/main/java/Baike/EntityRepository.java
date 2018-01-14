package Baike;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/9
 */

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends Neo4jRepository<Entity, Long> {

    //List<Person> findByName(String name);

    Entity findByName(String name);


}
