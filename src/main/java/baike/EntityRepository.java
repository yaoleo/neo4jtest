package baike;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/9
 */

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends Neo4jRepository<Entity, Long> {

    //List<Entity> findByName(String name);
    @Nullable
    Entity findByName(String name);


}
