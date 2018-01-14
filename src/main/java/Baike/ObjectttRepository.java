package Baike;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/13
 */
@Repository
public interface ObjectttRepository extends Neo4jRepository<Objecttt, Long> {
    @Nullable
    Objecttt findByName(String name);

    Objecttt findObjectByUri(String uri);

}