package com.zemberek.main;

import com.zemberek.model.dbModel;
import com.zemberek.model.tweetDataModel;
import com.zemberek.util.clientProvider;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.primefaces.extensions.component.gchart.model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@ManagedBean
@ViewScoped
public class dataBean implements Serializable {

    private List<tweetDataModel> dataList;
    private GChartModel mapChartModel = null;
    private GChartType cityType = GChartType.PIE;
    private GChartModel cityModel = null;
    private String docCount;
    private String took;
    private String diskUsage;
    private String wildcard = "";
    private boolean searchButton = false;


    @PostConstruct
    public void init(){
        dataList = new ArrayList<>();
        fullTextSearch();
    }

    public void fullTextSearch(){
        dataList.clear();
        Map<String, Object> colorAxis = new HashMap<>();
        colorAxis.put("colors", new String[] { "white", "orange" });
        clientProvider.instance().getClient()
                .admin().indices().prepareRefresh().execute().actionGet();

        mapChartModel = new GChartModelBuilder().setChartType(GChartType.GEO)
                .addColumns("Country", "Popularity")
                .addOption("colorAxis", colorAxis)
                .build();

        cityModel = new GChartModelBuilder().setChartType(getCityType())
                .addColumns("Topping", "Slices").build();

        SearchRequestBuilder searchRequestBuilder = clientProvider.instance().getClient().prepareSearch(dbModel.indexName);
        searchRequestBuilder.setTypes(dbModel.typeName);
        searchRequestBuilder.setSearchType(SearchType.DEFAULT);

        searchRequestBuilder.setFrom(0).setSize(100000).setExplain(true).addSort("date", SortOrder.DESC);

        if(searchButton){
            QueryBuilder queryBuilder = QueryBuilders.queryString("*" + wildcard + "*");
            searchRequestBuilder.setQuery(queryBuilder);
        }

        SearchResponse response = searchRequestBuilder.execute().actionGet();

        IndicesStatsResponse indicesStatsResponse = clientProvider.instance().getClient()
                .admin()
                .indices()
                .prepareStats(dbModel.indexName)
                .all()
                .execute().actionGet();

        diskUsage = indicesStatsResponse.getTotal().getStore().getSize().toString();

        if (response != null) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            for (SearchHit hit : response.getHits()) {
                try {
                    dataList.add(new tweetDataModel(sf.parse(hit.getSource().get("date").toString()),hit.getSource().get("user").toString(),hit.getSource().get("city").toString(),
                            hit.getSource().get("country").toString(),hit.getSource().get("countryCode").toString(),hit.getSource().get("text").toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        docCount = Integer.toString(dataList.size());
        took = response.getTook().toString();

        Map<String, Long> counting = dataList.stream().collect(
                Collectors.groupingBy(tweetDataModel::getCountry, Collectors.counting()));

        chartData(counting, mapChartModel);

        Map<String, Long> countingCity = dataList.stream().collect(
                Collectors.groupingBy(tweetDataModel::getCity, Collectors.counting()));

        chartData(countingCity,cityModel);


        searchButton = false;

    }

    public void chartData(Map<String, Long> map, GChartModel model){
        for(Map.Entry e : map.entrySet()){
            if(!e.getKey().toString().equals("")) {
                Collection<Object> o = new ArrayList<>();
                o.add(e.getValue());
                GChartModelRow rows = new DefaultGChartModelRow(e.getKey().toString(), o);
                model.getRows().add(rows);
            }
        }
    }

    public void search(){
        searchButton = true;
        fullTextSearch();
    }


    public List<tweetDataModel> getDataList() {
        return dataList;
    }

    public void setDataList(List<tweetDataModel> dataList) {
        this.dataList = dataList;
    }

    public GChartModel getMapChartModel() {
        return mapChartModel;
    }

    public GChartType getCityType() {
        return cityType;
    }

    public GChartModel getCityModel() {
        return cityModel;
    }

    public void setCityModel(GChartModel cityModel) {
        this.cityModel = cityModel;
    }

    public String getDocCount() {
        return docCount;
    }

    public void setDocCount(String docCount) {
        this.docCount = docCount;
    }

    public String getTook() {
        return took;
    }

    public void setTook(String took) {
        this.took = took;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }
}
