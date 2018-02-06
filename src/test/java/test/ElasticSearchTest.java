package test;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


public class ElasticSearchTest {

    public final static String HOST = "127.0.0.1";

    //http请求的端口是9200，客户端是9300
    public final static int PORT = 9300;

    TransportClient client;


    @SuppressWarnings("resource")
    @Test
    public void connectToES() throws Exception {

        //Settings settings = Settings.builder().put("cluster.name", "leo").build();
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(HOST), PORT));

        System.out.println(client.toString());

        //增删改查操作

        //createIndex();
        //createIndexByJson();
        //addIndexValue();
        //getIndex();
        //updateIndex();
        //deleteIndex();

        //设置查询条件
        QueryBuilder queryBuilder1 = QueryBuilders.matchPhraseQuery("sex", "male");
        QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("sex", "male");
        QueryBuilder queryBuilder3 = QueryBuilders.termQuery("sex", "male");
        QueryBuilder queryBuilder4 = QueryBuilders.prefixQuery("sex", "fe");
        QueryBuilder queryBuilder5 = QueryBuilders.wildcardQuery("sex", "*male");
        QueryBuilder queryBuilder6 = QueryBuilders.rangeQuery("age").from("22").to("23");

        BoolQueryBuilder queryBuilder7 = QueryBuilders.boolQuery();
        queryBuilder7.must(queryBuilder1);
        queryBuilder7.filter(queryBuilder6);

        //searchIndex(null, queryBuilder7, "school", "student");

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("aggSex").field("sex.keyword");
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("aggAge").field("age.keyword");
        aggregationBuilder.subAggregation(ageAgg);

        //searchIndex(aggregationBuilder, QueryBuilders.matchAllQuery(), "school", "student");
    }

    /**
     * 创建索引
     * @throws IOException
     */
    public void createIndex() throws IOException{

        IndexResponse response = client.prepareIndex("library", "book", "1")
                .setSource(jsonBuilder().
                        startObject()
                        .field("book_name", "ElasticSearch入门")
                        .field("author", "Kobe")
                        .field("publish_time", "2017-09-09")
                        .endObject())
                .get();
    }

    /**
     * 通过Json创建索引(利用BulkRequestBuilder进行批量操作)
     * @throws IOException
     */
    public void createIndexByJson() throws Exception{

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        String json1 = "{\"name\":\"larry\", \"sex\":\"male\", \"age\":\"23\"}";
        String json2 = "{\"name\":\"euphie\", \"sex\":\"female\", \"age\":\"22\"}";
        List<String> listJson = new ArrayList<>();
        listJson.add(json1);
        listJson.add(json2);

        for(int j=0;j<listJson.size();j++){
            //requestBuilder.setSource(listJson.get(j)).execute().actionGet();
            String id = String.valueOf(j);
            bulkRequest.add(client.prepareIndex("school", "student", id).setSource(listJson.get(j)));
        }

        bulkRequest.execute().actionGet();

    }

    /**
     * 通过Json添加索引内容(利用BulkRequestBuilder进行批量操作)
     * @throws IOException
     */
    public void addIndexValue() throws IOException{

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        String json1 = "{\"name\":\"mandy\", \"sex\":\"female\", \"age\":\"18\"}";

        bulkRequest.add(client.prepareIndex("school", "student", "4").setSource(json1));
        bulkRequest.execute().actionGet();
    }

    /**
     * 获取索引
     */
    public void getIndex(){

        GetResponse getResponse = client.prepareGet("library", "book", "1").execute().actionGet();
        System.out.println(getResponse.getSourceAsString());
    }

    /**
     * 更新索引
     * @throws IOException
     */
    public void updateIndex() throws IOException{

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("library");
        updateRequest.type("book");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder().startObject().field("author", "James").field("publish_time", "2018-01-01").endObject());
        try {
            client.update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        GetResponse getResponse = client.prepareGet("library", "book", "1").execute().actionGet();
        System.out.println(getResponse.getSourceAsString());
    }

    /**
     * 删除索引
     */
    public void deleteIndex(){

        DeleteResponse response = client.prepareDelete("library", "book", "1").get();
    }

    /**
     * 搜索方法
     */
    public void searchIndex(TermsAggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, String index, String type){

        SearchResponse searchResponse = client.prepareSearch(index).setTypes(type)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .execute()
                .actionGet();

        //Search区块
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数 = " + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        if(searchHists.length > 0){

            for(SearchHit hit : searchHists){

                String id = hit.getId();
                String sex = (String)hit.getSource().get("sex");
                String name = (String)hit.getSource().get("name");
                String age = (String)hit.getSource().get("age");

                System.out.println("id = " + id + " name = " + name + " sex = " + sex + " age = " + age);
            }
        }

        //Aggregation区块
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
        System.out.println(searchResponse.toString());
        StringTerms gradeTerms = (StringTerms) aggMap.get("aggSex");
        Iterator<Bucket> gradeBucketIt = gradeTerms.getBuckets().iterator();
        while(gradeBucketIt.hasNext()){

            Bucket gradeBucket = gradeBucketIt.next();
            System.out.println(gradeBucket.getKey() + ": " + gradeBucket.getDocCount());

            //得到所有子聚合
            Map<String, Aggregation> subMap = gradeBucket.getAggregations().asMap();
            StringTerms ageTerms = (StringTerms) subMap.get("aggAge");
            Iterator<Bucket> ageBucketIt = ageTerms.getBuckets().iterator();

            System.out.print("Age : ");
            while(ageBucketIt.hasNext()){

                Bucket ageBucket = ageBucketIt.next();
                System.out.print(" " + ageBucket.getKeyAsString());
            }
            System.out.print("\n\n");
        }
    }


}