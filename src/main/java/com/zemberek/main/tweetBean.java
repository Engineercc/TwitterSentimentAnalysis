package com.zemberek.main;

import com.zemberek.model.dbModel;
import com.zemberek.model.tweetDataModel;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import zemberek.morphology.analysis.tr.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

@ManagedBean
@ViewScoped
public class tweetBean implements Serializable{

    private String wildcard;
    private List<tweetDataModel> tweetList;
    private Date startDate;
    private Date endDate;
    private boolean geoLocation;
    private boolean saveDatabase;
    private String oneri1;
    private String oneri2;
    private int spellCheckId = 0;

    public void search(){
        oneri1 = metniDuzelt(wildcard);
        oneri2 = metniDuzelt(wildcard);
        System.out.println("java twitter4j.examples.search.SearchTweets [query]");
        tweetList = new ArrayList<>();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("cKvW9zjMeiU4tENTOptWudK0a");
        cb.setOAuthConsumerSecret("ty4lqZJBOfLXbFvHTjzh9fMw4reGk9Ed39T2fwYMcERAqmmd5c");
        cb.setOAuthAccessToken("254651242-ON6sVyKEF90BbxSoPZjwXKpiPXZVspfxhgjPyIjq");
        cb.setOAuthAccessTokenSecret("yc3RBuvMdpIornxUwVK4x1m4oCyQ6AtDKfZTeSlv9Qc01");
        cb.setTweetModeExtended(true);
        TwitterFactory factory = new TwitterFactory(cb.build());
        Twitter twitter = factory.getInstance();

        try {
            SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
            Query query = new Query(wildcard);
            query.setSince("2017-11-11");
            if(startDate != null){
                query.setSince(sf.format(startDate));
                System.out.println("Baslangic = "+sf.format(startDate));
            }
            if(endDate != null){
                query.setUntil(sf.format(endDate));
                System.out.println("Bitis = "+sf.format(endDate));
            }
            query.setCount(120);
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();

                for (Status tweet : tweets) {

                    if (geoLocation) {
                        if (tweet.getPlace() != null) {
                            insertList(tweet.getCreatedAt(), tweet.getUser().getScreenName(), tweet.getPlace().getName(),
                                    tweet.getPlace().getCountry(), tweet.getPlace().getCountryCode(), tweet.getText());
                        }
                    } else {
                        if (tweet.getPlace() != null) {
                            insertList(tweet.getCreatedAt(), tweet.getUser().getScreenName(), tweet.getPlace().getName(),
                                    tweet.getPlace().getCountry(), tweet.getPlace().getCountryCode(), tweet.getText());
                        } else {
                            insertList(tweet.getCreatedAt(), tweet.getUser().getScreenName(), "",
                                    "", "", tweet.getText());
                        }
                    }

                }
            } while ((query = result.nextQuery()) != null);

        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }
    }

    public void insertList(Date createDate, String user, String city, String country, String countryCode, String text){
        tweetList.add(new tweetDataModel(createDate, user, city,
                country, countryCode, text));
        if(saveDatabase){
            System.out.println("insert = " + text);
            dbModel.insertData(createDate,user,city,country,countryCode,text);
        }
    }

    public void oneri1Search(){
        wildcard = oneri1;
        search();
    }
    public void oneri2Search(){
        wildcard = oneri2;
        search();
    }

    public String metniDuzelt(String str){

        StringBuilder sb = new StringBuilder();

        try {
            TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
            TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);


            for(String s1 : str.split(" ")){
                if(spellChecker.check(s1)){
                    sb.append(s1).append(" ");
                }else {
                    if((spellCheckId ==0)){
                        if(spellChecker.suggestForWord(s1).size() != 0){
                            sb.append(spellChecker.suggestForWord(s1).get(spellCheckId)).append(" ");
                        }else {
                            sb.append(s1).append(" ");
                        }
                    }else if(spellChecker.suggestForWord(s1).size() > 1){
                        sb.append(spellChecker.suggestForWord(s1).get(1)).append(" ");
                    }else {
                        if(spellChecker.suggestForWord(s1).size() != 0){
                            sb.append(spellChecker.suggestForWord(s1).get(0)).append(" ");
                        }else {
                            sb.append(s1).append(" ");
                        }
                    }
                }
            }
            spellCheckId++;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(dataBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }

    public List<tweetDataModel> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<tweetDataModel> tweetList) {
        this.tweetList = tweetList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(boolean geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean isSaveDatabase() {
        return saveDatabase;
    }

    public void setSaveDatabase(boolean saveDatabase) {
        this.saveDatabase = saveDatabase;
    }

    public String getOneri1() {
        return oneri1;
    }

    public void setOneri1(String oneri1) {
        this.oneri1 = oneri1;
    }

    public String getOneri2() {
        return oneri2;
    }

    public void setOneri2(String oneri2) {
        this.oneri2 = oneri2;
    }
}
