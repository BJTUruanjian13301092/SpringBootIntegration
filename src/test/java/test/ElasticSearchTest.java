package test;

import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.validate.query.*;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.junit.Test;

import javax.management.Query;
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


    /**
     * 利用TransportClient连接集群，程序主入口
     * @throws Exception
     */
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
        //deleteIndexID();
        //deleteIndex();

        //设置查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        QueryBuilder queryBuilder1 = QueryBuilders.matchPhraseQuery("sex", "male");
        QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("sex", "male");
        QueryBuilder queryBuilder3 = QueryBuilders.termQuery("sex", "male");
        QueryBuilder queryBuilder4 = QueryBuilders.prefixQuery("sex", "fe");
        QueryBuilder queryBuilder5 = QueryBuilders.wildcardQuery("sex", "*male");
        QueryBuilder queryBuilder6 = QueryBuilders.rangeQuery("age").from("22").to("23");

        BoolQueryBuilder queryBuilder7 = QueryBuilders.boolQuery();
        queryBuilder7.must(queryBuilder1);
        queryBuilder7.filter(queryBuilder6);

        QueryBuilder queryBuilder8 = QueryBuilders.fuzzyQuery("sex", "fe-male");

        String[] fields = {"name"};
        String[] texts = {"larry"};
        QueryBuilder queryBuilder9 = QueryBuilders.moreLikeThisQuery(fields, texts, null).minTermFreq(1).maxQueryTerms(12).minDocFreq(1);


        //验证查询语句的正确性
        ValidateQueryResponse validateQueryResponse = new ValidateQueryRequestBuilder(client, ValidateQueryAction.INSTANCE)
                .setQuery(queryBuilder9).setExplain(true).get();

        System.out.println("查询结果反馈:");
        System.out.println("--------------------------------------------------------");
        for (QueryExplanation qe : validateQueryResponse.getQueryExplanation()) {
            System.out.println(String.format("索引:%s", qe.getIndex()));
            System.out.println(String.format("解释:%s", qe.getExplanation()));
            System.out.println(String.format("错误信息：%s", qe.getError()));
        }

        //设置聚合条件
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("aggSex").field("sex.keyword");
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("aggAge").field("age.keyword");
        aggregationBuilder.subAggregation(ageAgg);

        //设置排序条件
        SortBuilder sortBuilder = SortBuilders.fieldSort("age.keyword").order(SortOrder.ASC).unmappedType("long");

        searchIndex(sortBuilder, aggregationBuilder, queryBuilder9, "school", "student");
    }

    /**
     * 创建索引
     * @throws IOException
     */
    public void createIndex() throws IOException{
    int[] a = {100, 200};
        for(int i=0;i<10;i++){

            IndexResponse response = client.prepareIndex("library", "book", String.valueOf(i))
                    .setSource(jsonBuilder().
                            startObject()
                            .field("book_name", "ElasticSearch入门")
                            .field("author", "Kobe")
                            .field("publish_time", "2017-09-09")
                            .field("describe", "This book is not a good one so do not buy it")
                            .field("price", a)
                            .endObject())
                    .get();
        }
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

//        //Get一个
//        GetResponse getResponse = client.prepareGet("school", "student", "0").execute().actionGet();
//        System.out.println(getResponse.getSourceAsString());

        //Get多个
        MultiGetResponse multiGetResponse = client.prepareMultiGet()
                .add("school", "student", "0")
                .add("school", "student", "1").execute().actionGet();
        MultiGetItemResponse[] itemResponses = multiGetResponse.getResponses();
        for(MultiGetItemResponse item : itemResponses){

            System.out.println(item.getResponse().getSourceAsString());
        }

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
     * 删除索引中的一个ID
     */
    public void deleteIndexID(){

        //DeleteResponse response = client.prepareDelete("library", "book", "1").get();

        //批量删除,通过add添加
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.add(client.prepareDelete("library", "book", "1"));
        bulkRequest.execute().actionGet();
    }

    /**
     * 删除索引
     */
    public void deleteIndex(){

        //删除索引
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete("library").execute().actionGet();

        //判断索引是否存在
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest("library");

        IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();

        System.out.println(inExistsResponse.toString());
    }

    /**
     * 搜索方法
     */
    public void searchIndex(SortBuilder sortBuilder, TermsAggregationBuilder aggregationBuilder, QueryBuilder queryBuilder, String index, String type){

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .addSort(sortBuilder)
                .setFrom(0)                 //分页技术，设置起始位置
                .setSize(10000);            //设置（每一页）最大的显示数量，size默认是10

        System.out.println("SearchRequest is: ");
        System.out.println(searchRequest.toString());
        SearchResponse searchResponse = searchRequest.execute().actionGet();

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
                float score = hit.getScore();

                System.out.println("id = " + id + " name = " + name + " sex = " + sex + " age = " + age + " score = " + score);
            }
        }

        System.out.println("--------------------------------------------------------");

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