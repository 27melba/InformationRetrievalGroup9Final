package InfoRetrieval;

import model.DictionaryModel;

import java.util.*;

//https://www.programiz.com/java-programming/operator-precedence
//https://www.geeksforgeeks.org/set-in-java/
public class BooleanQuery {

    public static Set<Long> retrievedResults = new HashSet<Long>();

    public Set<Long> booleanQueryValidation(List<DictionaryModel> d, String q) {

        List<String> splittedQuery = new ArrayList<>();

        String[] strarr = q.split(" "); //take each query term and add to strarr
        splittedQuery = Arrays.asList(strarr); //add strarr to List
        List<DictionaryModel> queryTerms = new ArrayList<DictionaryModel>();

        for (int i = 0; i < splittedQuery.size(); i++) {
            int j;
            for (j = 0; j < d.size(); j++) {
                if ((d.get(j).getTerm().equals(splittedQuery.get(i)))) {
                    queryTerms.add(d.get(j)); //add terms in query to queryTerms List
                    break;
                }
            }
            //adding empty set if the term is not in dictionary
            if ((j == d.size()) && (i % 2 == 0)) {
                DictionaryModel temp = new DictionaryModel();
                temp.setTerm(splittedQuery.get(i));
                temp.setPostSet(new LinkedHashSet<Long>());
                queryTerms.add(temp);
            }

        }

        //if there is no match between query terms and dictionary terms, return empty set
        if (queryTerms.size() == 0) {
            retrievedResults = new HashSet<Long>();
        } else if (queryTerms.size() > 1) {

            String operation = "";
            for (int i = 0; i < splittedQuery.size(); i++) {
                if (i % 2 == 0) {
                    if (i == 0) {
                        retrievedResults = queryTerms.get(i).getPostSet();
                    }
//             if "AND" operation is to be done, take intersection between two postings set of terms
                    if (operation.equals("&&")) {
                        retrievedResults.retainAll(queryTerms.get(i / 2).getPostSet());
                    }
//             if "OR" operation is to be done, take Union between two postings set of terms
                    else if (operation.equals("||")) {
                        retrievedResults.addAll(queryTerms.get(i / 2).getPostSet());
                    }
//             if "NOT" operation is to be done, take NOT of thr postings set of term
                    else if (operation.equals("!")) {
                        retrievedResults.removeAll(queryTerms.get(i / 2).getPostSet());
                    }
                }
//                add the boolean operator to "operation"
                else {
                    operation = splittedQuery.get(i);
                }
            }
        }
        //if only one term in query
        else if (queryTerms.size() == 1) {
            for (int k = 0; k < queryTerms.size(); k++) {
                retrievedResults = queryTerms.get(k).getPostSet();
            }
        }

        return retrievedResults;
    }
}
