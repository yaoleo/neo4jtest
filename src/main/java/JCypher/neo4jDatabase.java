package JCypher;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;
import iot.jcypher.graph.GrLabel;
import iot.jcypher.graph.GrNode;
import iot.jcypher.graph.GrProperty;
import iot.jcypher.query.JcQuery;
import iot.jcypher.query.JcQueryResult;
import iot.jcypher.query.api.IClause;
import iot.jcypher.query.factories.clause.*;
import iot.jcypher.query.values.JcNode;
import iot.jcypher.query.writer.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.io.IOTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/28
 */
public class neo4jDatabase {
    private final static Logger log = LoggerFactory.getLogger(neo4jDatabase.class);
    private static IDBAccess dbAccess;

    private static void initDBConnection() {
        Properties props = new Properties();
        props.setProperty(DBProperties.SERVER_ROOT_URI, "bolt://localhost:7687");

        dbAccess = DBAccessFactory.createDBAccess(DBType.REMOTE, props);

    }

    private static void createDatabaseByQuery() throws IOException {
        ArrayList<String> triples = IOTool.readLinesFromFile("C:\\Users\\yaoleo\\IdeaProjects\\neo4jtest\\src\\main\\resources\\NT.nt");
        //log.info(String.valueOf(triples.size()));
        int count = 0;
        JcNode matrix1 = new JcNode("matrix1");
        JcQuery query = new JcQuery();
        JcNode object = new JcNode("orgin");

        //判断是否存过列表
        ArrayList<String> ss = new ArrayList<>();
        for(String triple :triples) {
            String objecturi = triple.split(" ")[0];
            String propertyuri = triple.split(" ")[1];
            String subjectname = triple.split(" ")[2].split("\"")[1];

            String[] propertylist = propertyuri.split("/");
            String propertyname = propertylist[propertylist.length - 1];
            propertyname = propertyname.substring(0, propertyname.length() - 1);

            String[] objectlist = objecturi.split("/");
            String objectname = objectlist[objectlist.length - 1];
            objectname = objectname.substring(0, objectname.length() - 1);
            JcNode n = new JcNode("n");
            if (ss.contains(objectname)){
                //todo
                String queryTitle = "CREATE  NODES";
                query.setClauses(new IClause[] {
                        MATCH.node(n),
                        WHERE.valueOf(n.property("name")).EQUALS(objectname),
                        DO.SET(n.property(propertyname)).to(subjectname),
                });
                JcQueryResult result = dbAccess.execute(query);
                print(query, queryTitle, Format.PRETTY_3);
                log.info(result.toString());
                if (result.hasErrors())
                    log.info(result.toString());
            }else{
                ss.add(objectname);
                String queryTitle = "CREATE  NODES";
                query.setClauses(new IClause[] {
                        CREATE.node(object).label("ObjectNew")
                                .property("name").value(objectname)
                                .property(propertyname).value(subjectname)
                });
                JcQueryResult result = dbAccess.execute(query);
                print(query, queryTitle, Format.PRETTY_3);
                log.info(result.toString());
                if (result.hasErrors())
                    log.info(result.toString());
            }



        }

    }

    private static void queryTriples(String subject, String object){

    }

    /**
     * 输入主语，谓语返回宾语
     * 参数：主语，谓语
     * return
     */
    private static void queryObject(String subjectname, String propertyname){
        String queryTitle = "CREATE  NODES";
        JcQuery query = new JcQuery();
        JcNode n = new JcNode("n");
        query.setClauses(new IClause[] {
                MATCH.node(n),
                WHERE.valueOf(n.property("name")).EQUALS(subjectname),
                RETURN.value(n)
        });
        JcQueryResult result = dbAccess.execute(query);
        //print(query, queryTitle, Format.PRETTY_3);
        System.out.println(result);
        List<GrNode> ns = result.resultOf(n);
        print(ns,true);
        //if (result.hasErrors())
        //    log.info(result.toString());
    }

    private static void print(List<GrNode> nodes, boolean distinct) {
        List<Long> ids = new ArrayList<Long>();
        StringBuilder sb = new StringBuilder();
        boolean firstNode = true;
        for (GrNode node : nodes) {
            if (!ids.contains(node.getId()) || !distinct) {
                ids.add(node.getId());
                if (!firstNode)
                    sb.append("\n");
                else
                    firstNode = false;
                sb.append("---NODE---:\n");
                sb.append('[');
                sb.append(node.getId());
                sb.append(']');
                for (GrLabel label : node.getLabels()) {
                    sb.append(", ");
                    sb.append(label.getName());
                }
                sb.append("\n");
                boolean first = true;
                for (GrProperty prop : node.getProperties()) {
                    if (!first)
                        sb.append(", ");
                    else
                        first = false;
                    sb.append(prop.getName());
                    sb.append(" = ");
                    sb.append(prop.getValue());
                }
            }
        }
        System.out.println(sb.toString());
    }

    /**
     * map to CYPHER statements and map to JSON, print the mapping results to System.out
     * @param query
     * @param title
     * @param format
     */
    private static void print(JcQuery query, String title, Format format) {
        System.out.println("QUERY: " + title + " --------------------");
        // map to Cypher
        String cypher = iot.jcypher.util.Util.toCypher(query, format);
        System.out.println("CYPHER --------------------");
        System.out.println(cypher);

        // map to JSON
        String json = iot.jcypher.util.Util.toJSON(query, format);
        System.out.println("");
        System.out.println("JSON   --------------------");
        System.out.println(json);

        System.out.println("");
    }
    public static void main(String[] args) throws IOException {

        /** initialize connection to a Neo4j database */
        initDBConnection();

        /** execute queries against the database */
        //createDatabaseByQuery();
//		createMovieDatabaseByGraphModel();
        //createAdditionalNodes();
        queryObject("可蒙犬","科");
        //queryMovieGraph();

        /** close the connection to a Neo4j database */
        //closeDBConnection();
    }
}
