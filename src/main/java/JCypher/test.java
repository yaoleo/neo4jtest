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
import iot.jcypher.query.factories.clause.CREATE;
import iot.jcypher.query.factories.clause.MATCH;
import iot.jcypher.query.factories.clause.RETURN;
import iot.jcypher.query.result.JcError;
import iot.jcypher.query.values.JcNode;
import iot.jcypher.query.values.JcNumber;
import iot.jcypher.query.values.JcRelation;
import iot.jcypher.query.writer.Format;
import iot.jcypher.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/28
 */
public class test {
    private static IDBAccess dbAccess;
    private static void initDBConnection() {
        Properties props = new Properties();

        // properties for remote access and for embedded access
        // (not needed for in memory access)
        // have a look at the DBProperties interface
        // the appropriate database access class will pick the properties it needs
        props.setProperty(DBProperties.SERVER_ROOT_URI, "bolt://localhost:7687");
        props.setProperty(DBProperties.DATABASE_DIR, "C:/NEO4J_DBS/01");

        /** connect to an in memory database (no properties are required) */
        //dbAccess = DBAccessFactory.createDBAccess(DBType.IN_MEMORY, props);

        /** connect to remote database via REST (SERVER_ROOT_URI property is needed) */
        dbAccess = DBAccessFactory.createDBAccess(DBType.REMOTE, props);

        /** connect to an embedded database (DATABASE_DIR property is needed) */
        //dbAccess = DBAccessFactory.createDBAccess(DBType.EMBEDDED, props);
    }

    private static void createDatabaseByQuery() {
        JcNode matrix1 = new JcNode("matrix1");
        JcNode matrix2 = new JcNode("matrix2");
        JcNode matrix3 = new JcNode("matrix3");
        JcNode keanu = new JcNode("keanu");
        JcNode laurence = new JcNode("laurence");
        JcNode carrieanne = new JcNode("carrieanne");

        /**
         * -----------------------------------------------------------------
         * Create the movie database
         */
        String queryTitle = "CREATE MOVIE DATABASE";
        JcQuery query = new JcQuery();
        query.setClauses(new IClause[] {
                CREATE.node(matrix1).label("Movie")
                        .property("title").value("The Matrix")
                        .property("year").value("1999-03-31"),
                CREATE.node(matrix2).label("Movie")
                        .property("title").value("The Matrix Reloaded")
                        .property("year").value("2003-05-07"),
                CREATE.node(matrix3).label("Movie")
                        .property("title").value("The Matrix Revolutions")
                        .property("year").value("2003-10-27"),
                CREATE.node(keanu).label("Actor")
                        .property("name").value("Keanu Reeves")
                        .property("like").value(8.5)
                        .property("numbers").values(1, 2, 3),
                CREATE.node(laurence).label("Actor")
                        .property("name").value("Laurence Fishburne")
                        .property("like").value(7),
                CREATE.node(carrieanne).label("Actor")
                        .property("name").value("Carrie-Anne Moss")
                        .property("like").value(8.3),
                CREATE.node(keanu).relation().out().type("ACTS_IN").property("role").value("Neo").node(matrix1),
                CREATE.node(keanu).relation().out().type("ACTS_IN").property("role").value("Neo").node(matrix2),
                CREATE.node(keanu).relation().out().type("ACTS_IN").property("role").value("Neo").node(matrix3),
                CREATE.node(laurence).relation().out().type("ACTS_IN").property("role").value("Morpheus").node(matrix1),
                CREATE.node(laurence).relation().out().type("ACTS_IN").property("role").value("Morpheus").node(matrix2),
                CREATE.node(laurence).relation().out().type("ACTS_IN").property("role").value("Morpheus").node(matrix3),
                CREATE.node(carrieanne).relation().out().type("ACTS_IN").property("role").value("Trinity").node(matrix1),
                CREATE.node(carrieanne).relation().out().type("ACTS_IN").property("role").value("Trinity").node(matrix2),
                CREATE.node(carrieanne).relation().out().type("ACTS_IN").property("role").value("Trinity").node(matrix3)
        });
        /** map to CYPHER statements and map to JSON, print the mapping results to System.out.
         This will show what normally is created in the background when accessing a Neo4j database*/
        print(query, queryTitle, Format.PRETTY_3);

        /** execute the query against a Neo4j database */
        JcQueryResult result = dbAccess.execute(query);


        /** print the JSON representation of the query result */
        //print(result, queryTitle);
    }
    public static void main(String[] args) {

        /** initialize connection to a Neo4j database */
        initDBConnection();

        /** execute queries against the database */
        createDatabaseByQuery();
//		createMovieDatabaseByGraphModel();
        //createAdditionalNodes();
        //queryNodeCount();
        //queryMovieGraph();
        //querytest();

        /** close the connection to a Neo4j database */
        //closeDBConnection();
    }

    private static void querytest() {
//Nodes are represented by class JcNode
        JcNode n = new JcNode("n"); //"n" is the node's identifier.
//Then we can formulate:
        n.stringProperty("name").length();
//which accesses the node's property "name" as a string
//and calculates its length.
        JcNode a = new JcNode("a");
        JcNode b = new JcNode("b");
        a.stringProperty("name").concat("<->").concat(b.stringProperty("name"));
//concatenates the "name" properties of two nodes (a, b)
//with a string constant.
/*This maps to the Cypher expression:*/ //a.name + '<->' + b.name
        a.numberProperty("amount").plus(b.numberProperty("amount"));
//calculates the sum of the "amount" properties of two nodes (a, b).

//Given that 'r' is a 'Relation', represented by class JcRelation.
        JcRelation r = new JcRelation("r"); //then
        r.startNode(); //answers the start node of the relation.
    }

    private static void queryMovieGraph() {
        String queryTitle = "MOVIE_GRAPH";
        JcNode movie = new JcNode("movie");
        JcNode actor = new JcNode("actor");

        /*******************************/
        JcQuery query = new JcQuery();
        query.setClauses(new IClause[] {
                MATCH.node(actor).label("Actor").relation().out().type("ACTS_IN").node(movie),
                RETURN.value(actor),
                RETURN.value(movie)
        });
        /** map to CYPHER statements and map to JSON, print the mapping results to System.out.
         This will show what normally is created in the background when accessing a Neo4j database*/
        print(query, queryTitle, Format.PRETTY_3);

        /** execute the query against a Neo4j database */
        JcQueryResult result = dbAccess.execute(query);
        if (result.hasErrors())
            printErrors(result);

        /** print the JSON representation of the query result */
        //print(result, queryTitle);

        List<GrNode> actors = result.resultOf(actor);
        List<GrNode> movies = result.resultOf(movie);

        print(actors, true);
        print(movies, true);

        return;
    }

    private static void queryNodeCount() {
        JcQuery query;
        JcQueryResult result;

        String queryTitle = "COUNT NODES";
        JcNode n = new JcNode("n");
        JcNumber nCount = new JcNumber("nCount");

        query = new JcQuery();
        query.setClauses(new IClause[] {
                MATCH.node(n),
                RETURN.count().value(n).AS(nCount)
        });
        /** map to CYPHER statements and map to JSON, print the mapping results to System.out.
         This will show what normally is created in the background when accessing a Neo4j database*/
        print(query, queryTitle, Format.PRETTY_3);

        /** execute the query against a Neo4j database */
        result = dbAccess.execute(query);
        if (result.hasErrors())
            printErrors(result);

        /** print the JSON representation of the query result */
        //print(result, queryTitle);

        List<BigDecimal> nr = result.resultOf(nCount);
        System.out.println(nr.get(0));
        return;
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

    /**
     * print the JSON representation of the query result
     * @param queryResult
     */
    private static void print(JcQueryResult queryResult, String title) {
        System.out.println("RESULT OF QUERY: " + title + " --------------------");
        String resultString = Util.writePretty(queryResult.getJsonResult());
        System.out.println(resultString);
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
     * print errors to System.out
     * @param result
     */
    private static void printErrors(JcQueryResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------General Errors:");
        appendErrorList(result.getGeneralErrors(), sb);
        sb.append("\n---------------DB Errors:");
        appendErrorList(result.getDBErrors(), sb);
        sb.append("\n---------------end Errors:");
        String str = sb.toString();
        System.out.println("");
        System.out.println(str);
    }

    /**
     * print errors to System.out
     */
    private static void printErrors(List<JcError> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------Errors:");
        appendErrorList(errors, sb);
        sb.append("\n---------------end Errors:");
        String str = sb.toString();
        System.out.println("");
        System.out.println(str);
    }

    private static void appendErrorList(List<JcError> errors, StringBuilder sb) {
        int num = errors.size();
        for (int i = 0; i < num; i++) {
            JcError err = errors.get(i);
            sb.append('\n');
            if (i > 0) {
                sb.append("-------------------\n");
            }
            sb.append("codeOrType: ");
            sb.append(err.getCodeOrType());
            sb.append("\nmessage: ");
            sb.append(err.getMessage());
            if (err.getAdditionalInfo() != null) {
                sb.append("\nadditional info: ");
                sb.append(err.getAdditionalInfo());
            }
        }
    }

}
