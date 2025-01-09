package com.heima.item.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.item.domain.po.Item;
import com.heima.item.domain.po.ItemDoc;
import com.heima.item.service.IItemService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * ClassName：DocumentTest
 *
 * @author: Devil
 * @Date: 2025/1/8
 * @Description:
 * @version: 1.0
 */
@SpringBootTest(properties = "spring.profiles.active=local")
public class DocumentTest {
    private RestHighLevelClient client;

    @Autowired
    private IItemService itemService;

    @Test
    void testIndexDoc() throws IOException {
        //0.准备文档数据
        Item item = itemService.getById(100002644680L);
        ItemDoc itemDoc = BeanUtil.copyProperties(item, ItemDoc.class);

        //1.准备Request
        IndexRequest request = new IndexRequest("items").id(item.getId().toString());
        //2.准备请求参数
        request.source(JSONUtil.toJsonStr(itemDoc), XContentType.JSON);
        //3.发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void testGetDoc() throws IOException {
        //1.准备Request
        GetRequest request = new GetRequest("items","100002644680L");
        //2.发送请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //3.解析响应结果
        String string = response.getSourceAsString();
        ItemDoc itemDoc = JSONUtil.toBean(string, ItemDoc.class);
        System.out.println("doc = " + itemDoc);
    }

    @Test
    void testDeleteDoc() throws IOException {
        //1.准备Request
        DeleteRequest request = new DeleteRequest("items","100002644680L");
        //2.发送请求
        client.delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void testUpdateDoc() throws IOException {
        //1.准备Request
        UpdateRequest request = new UpdateRequest("items","100002644680L");
        //2.准备请求参数
        request.doc(
                "price",25600
        );
        //3.发送请求
        client.update(request, RequestOptions.DEFAULT);
    }

    void testBulkDoc() throws IOException{
        int pageNo = 1,pageSize = 500;
        while (true){
            //1.准备数据
            Page<Item> page = itemService.lambdaQuery()
                    .eq(Item::getStatus, 1)
                    .page(Page.of(pageNo, pageSize));
            List<Item> records = page.getRecords();
            if(records == null || records.isEmpty()){
                return;
            }
            //2.准备request
            BulkRequest request = new BulkRequest();
            //3.准备请求参数
            for (Item i :records) {
                request.add(new IndexRequest("items")
                        .id(i.getId().toString())
                        .source(JSONUtil.toJsonStr(BeanUtil.copyProperties(itemService, ItemDoc.class)),XContentType.JSON)
                );
            }
            //4.发送请求
            client.bulk(request,RequestOptions.DEFAULT);
            //5.翻页
            pageNo++;
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
}
