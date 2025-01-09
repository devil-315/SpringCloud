package com.heima.item.es;

import cn.hutool.json.JSONUtil;
import com.heima.item.domain.po.ItemDoc;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ClassName：SearchTest
 *
 * @author: Devil
 * @Date: 2025/1/9
 * @Description:
 * @version: 1.0
 */
@SpringBootTest(properties = "spring.profiles.active=local")
public class SearchTest {
    private RestHighLevelClient client;

    @Test
    void testMatchAll() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.配置request参数
        request.source()
                .query(QueryBuilders.matchAllQuery());
        //3.发送请求
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        //4.解决结果
        handleResponse(search);
    }

    @Test
    void testSearch() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.配置request参数
        request.source()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("name","脱脂牛奶"))
                        .filter(QueryBuilders.termQuery("brand","德亚"))
                        .filter(QueryBuilders.rangeQuery("price").lt(30000)));
        //3.发送请求
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        //4.解决结果
        handleResponse(search);
    }

    @Test
    void testSortAndPage() throws IOException {
        //0.前端传递的分页参数
        int pageNo = 1,pageSize = 5;
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1query条件
        request.source()
                .query(QueryBuilders.matchAllQuery());
        //2.2分页
        request.source().from((pageNo - 1) * pageSize).size(pageSize);
        //2.3排序
        request.source()
                .sort("sold", SortOrder.DESC)
                .sort("price",SortOrder.ASC);

        //3.发送请求
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        //4.解决结果
        handleResponse(search);
    }

    @Test
    void testHighlight() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1query条件
        request.source().query(QueryBuilders.matchQuery("name","脱脂牛奶"));
        //2.2高亮条件
        request.source().highlighter(new HighlightBuilder()
                .field("name"));
        //3.发送请求
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        //4.解决结果
        handleResponse(search);
    }

    @Test
    void testAgg() throws IOException {
        //1.创建request对象
        SearchRequest request = new SearchRequest("items");
        //2.组织DSL参数
        //2.1分页
        request.source().size(0);
        //2.2聚合条件
        String name = "brandAgg";
        request.source().aggregation(
                AggregationBuilders.terms(name).field("brand").size(10)
        );
        //3.发送请求
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        // 5.解析聚合结果
        Aggregations aggregations = search.getAggregations();

        // 5.1.获取品牌聚合
        Terms brandTerms = aggregations.get(name);

        // 5.2.获取聚合中的桶
        List<? extends Terms.Bucket> buckets = brandTerms.getBuckets();
        // 5.3.遍历桶内数据
        for (Terms.Bucket bucket: buckets) {
            // 5.4.获取桶内key
            System.out.println("brand: " + bucket.getKeyAsString());
            System.out.println("count: " + bucket.getKeyAsNumber());
        }

    }

    @BeforeEach
    void setUp() {
        client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.33.164:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        if(client != null){
            client.close();
        }
    }

    private static void handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 3.得到_source，也就是原始json文档
            String source = hit.getSourceAsString();
            // 4.反序列化
            ItemDoc item = JSONUtil.toBean(source, ItemDoc.class);
            //5.获取高亮结果
            Map<String, HighlightField> hfs = hit.getHighlightFields();
            if (hfs != null && !hfs.isEmpty()){
                // 5.1.有高亮结果，获取name的高亮结果
                HighlightField hf = hfs.get("name");
                // 5.2.获取第一个高亮结果片段，就是商品名称的高亮值
                String s = hf.getFragments()[0].string();
                item.setName(s);
            }
            System.out.println(item);
        }
    }
}
