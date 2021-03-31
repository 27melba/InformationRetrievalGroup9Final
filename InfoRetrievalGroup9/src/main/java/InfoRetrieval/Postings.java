package InfoRetrieval;


import model.DictionaryModel;
import model.DocumentModel;
import model.IndexPositionModel;
import model.PostingsModel;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import InfoRetrieval.BinaryTree.Node;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Postings {

    public static double vector_dot(double[] vec1, double[] vec2) {
        double sum = 0;
        for (int i = 0; i < vec1.length && i < vec2.length; i++)
            sum += vec1[i] * vec2[i];
        return sum;
    }

    public static double vector_norm(double[] vec) {
        double sum = 0;
        for (double v : vec)
            sum += v * v;
        return Math.sqrt(sum);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException {

        Generate postingsGenerate=new Generate();
        BooleanQuery bool=new BooleanQuery();
        Scanner s= new Scanner(System.in);

//        https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
        JSONParser jsonParser=new JSONParser();

        try(FileReader fileReader=new FileReader("InfoRetrievalGroup9/src/main/resources/shakespeare-scenes.json")) {

            JSONObject jsonObj= (JSONObject)jsonParser.parse(fileReader);
            JSONArray docArray= (JSONArray) jsonObj.get("corpus");

            System.out.println("Enter your Query");
            String queryString=s.nextLine();

            //get postings List
            List<PostingsModel> p= postingsGenerate.generatePostings(docArray);

            //generating positional Index
            List<DictionaryModel> dicList=postingsGenerate.generatePositionalIndex(p);

            //get the boolean query result
            Set<Long> queryResult=bool.booleanQueryValidation( dicList,queryString.toLowerCase());
            System.out.println("BOOLEAN QUERY RESULTS:");
            System.out.println(queryResult);

            //if the result is empty
            if((queryResult.size()==0) || (queryResult==new HashSet<Long>())){
                System.out.println("sorry! no results found");
            }
            /* sample Boolean Queries:
            weed
            welcome && well && went
            othello || madness && judgement
            abhor && about || abilities
            othello && madness || judgment
            tune && sir || raise ! forbear
            */

            //Cosine Ranking
            System.out.println("\nRANKING USING COSINE SIMLARITY : ") ;
//            spliting query in seperate terms
            String s1[] = queryString.split(" ");

            double scoreQuery[] = new double [s1.length];
            double docScores[][] = new double [docArray.size()][s1.length];
            for (int i =0;i<s1.length;i++) {
//            	Reading dicList and get the posting of terms in query
                for (int j = 0; j < dicList.size(); j++) {
                    if ((dicList.get(j).getTerm().equals(s1[i]))) {
                        DictionaryModel dic = dicList.get(j);
                        LinkedHashSet<IndexPositionModel> sample =dic.getIndexPositionSet();
//                    	store the score of term in scoreQuery
                        scoreQuery[i] =(double) 1/(double)dic.getDocFreq();

                        for (IndexPositionModel jj:sample){
//                        	store the score of term in document

                            int ind= jj.getDocId().intValue();
                            double ans =  (double) (jj.getPositions().size()*100/(double)dic.getDocFreq()) ;
                            docScores[ind-1][i] = ans;
                        }
                        break;
                    }
                }
            }

            BinaryTree bt = new BinaryTree();
            Node root = null ;
            for (int i=0;i<docScores.length;i++) {
                double cosim = vector_dot(scoreQuery, docScores[i]) / (vector_norm(scoreQuery) * vector_norm(docScores[i]));

                if(i == 0) {
                    root =  new Node(cosim,i+1);

                }
                if(!Double.isNaN(cosim)) {
                    bt.insert(root, cosim,i+1);
                }
            }
            bt.traverseInOrder(root,2);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //parsing json and convert it into objects of class DocumentModel
    public static DocumentModel parseDocuments(JSONObject doc)  {

        DocumentModel dModel=new DocumentModel();
        dModel.setPlayId((String) doc.get("playId"));
        dModel.setSceneId((String) doc.get("sceneId"));
        dModel.setSceneNum((Long) doc.get("sceneNum"));
        dModel.setText((String) doc.get("text"));
        return dModel;

    }

}
