package InfoRetrieval;

import model.DictionaryModel;
import model.DocumentModel;
import model.IndexPositionModel;
import model.PostingsModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Generate {

    Postings postings=new Postings();
    Long p= Long.valueOf(0);

    //generate Postings list
    public List<PostingsModel> generatePostings(JSONArray doc) throws IOException {

        DocumentModel dModel= new DocumentModel();
        List<DocumentModel> docList=new ArrayList<DocumentModel>();
        List<String> stringInPostingsList=new ArrayList<>();
        PostingsModel postingsModel=new PostingsModel();
        List<PostingsModel> postingsList=new ArrayList<>();

        for(int i=0;i<doc.size();i++){
            DocumentModel d=postings.parseDocuments((JSONObject) doc.get(i));
            docList.add(d);
        }

        for (int i=0;i<docList.size();i++){

            //take each terms
            String[] s=docList.get(i).getText().split("\\s+");
            p++;
            for ( int j = 0; j<s.length;j++) {

                postingsModel = new PostingsModel();
                postingsModel.setPosition((long)j+1);
                postingsModel.setDocID(p);
                postingsModel.setTerm(s[j]);
                postingsList.add(postingsModel);
            }
        }
        //sort the terms alphabetically
        Collections.sort(postingsList, new Comparator<PostingsModel>() {
            @Override
            public int compare(PostingsModel o1, PostingsModel o2) {
                return o1.getTerm().compareTo(o2.getTerm());
            }
        });

        return postingsList;
    }

    //Generate Positional Index
    public List<DictionaryModel> generatePositionalIndex(List<PostingsModel> pModel) throws IOException {

        FileWriter fw=new FileWriter("InfoRetrievalGroup9/src/main/resources/positionalIndex.txt");
        DictionaryModel dModel=new DictionaryModel();
        IndexPositionModel iModel=new IndexPositionModel();
        Set<Long> postingsSet = new LinkedHashSet<>() ;
        Set<Long> positionSet = new LinkedHashSet<>() ;
        Set<IndexPositionModel> indexPositionSet = new LinkedHashSet<>() ;
        List<DictionaryModel> dicList=new ArrayList<>();

        for (int i=0;i<pModel.size();i++){

            postingsSet.add(pModel.get(i).getDocID());
            positionSet.add(pModel.get(i).getPosition());

            if(i != pModel.size()-1) {

                //loops until next term occurs or next document occurs
                if((!pModel.get(i).getDocID().equals( pModel.get(i + 1).getDocID()))
                || (!pModel.get(i).getTerm().equals( pModel.get(i + 1).getTerm()))){

                    iModel.setPositions((LinkedHashSet<Long>) positionSet);
                    iModel.setDocId(pModel.get(i).getDocID());

                    indexPositionSet.add(iModel);
                    positionSet=new LinkedHashSet<>();
                    iModel=new IndexPositionModel();
                }
                //loops until next term occurs
                if (!pModel.get(i).getTerm().equals( pModel.get(i + 1).getTerm())) {

                    dModel.setTerm(pModel.get(i).getTerm());
                    dModel.setPostSet((LinkedHashSet<Long>) postingsSet);
                    dModel.setIndexPositionSet((LinkedHashSet<IndexPositionModel>) indexPositionSet);
                    dModel.setDocFreq((long) postingsSet.size());
                    dicList.add(dModel);

                    //for next term, create new indexPositionSet, dModel, postingsSet
                    indexPositionSet = new LinkedHashSet<>() ;
                    dModel = new DictionaryModel();
                    postingsSet = new LinkedHashSet<>();

                }
            }
            //for last term
            else{
                iModel.setPositions((LinkedHashSet<Long>) positionSet);
                iModel.setDocId(pModel.get(i).getDocID());
                indexPositionSet.add(iModel);

                dModel.setTerm(pModel.get(i).getTerm());
                dModel.setPostSet((LinkedHashSet<Long>)postingsSet);
                dModel.setIndexPositionSet((LinkedHashSet<IndexPositionModel>) indexPositionSet);
                dModel.setDocFreq((long) postingsSet.size());
                dicList.add(dModel);

            }
        }

        //write the positional index to the file
        fw.write("Term->docFreq->DocId&Positions\n");
        for (DictionaryModel i:dicList){
            LinkedHashSet<IndexPositionModel> sample =i.getIndexPositionSet();
            fw.write(i.getTerm()+"->"+
                    i.getDocFreq()+"\n");
            for (IndexPositionModel j:i.getIndexPositionSet()){
                    fw.write(j.getDocId()+"->"+
                                j.getPositions()+"\n");
            }
        }
        fw.close();
        return dicList;
  }
}

