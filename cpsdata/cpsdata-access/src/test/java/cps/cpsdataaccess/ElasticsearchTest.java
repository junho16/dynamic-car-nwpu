package cps.cpsdataaccess;

import com.alibaba.fastjson.JSONObject;
import cps.data.api.service.MessageLogService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPSDataAccessApplication.class)
public class ElasticsearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private MessageLogService messageLogService;

    @Test
    public void testSearch() {

        SearchRequest searchRequest = new SearchRequest("cpsruntimeservice-logs");

        // 构建查询参数 boolQueryBuilder可以进行多参数添加
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // termQuery:精确查找  rangeQuery:范围匹配  wildcardQuery:模糊匹配 （取出logger属性值为device_attributes_logger的数据）
        boolQueryBuilder.must(QueryBuilders.termQuery("logger", "device_attributes_logger"));

        // 查询参数
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // query查询
        searchSourceBuilder.query(boolQueryBuilder);
        // from-size分页
        searchSourceBuilder.from(0).size(10);
        // sort排序
        searchSourceBuilder.sort("@timestamp", SortOrder.DESC);
        searchRequest.source(searchSourceBuilder);
        SearchHit[] hits = new SearchHit[0];
        try {
            hits = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT).getHits().getHits();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                System.out.println("sourceAsString==>" + sourceAsString);
                JSONObject messageJsonObject = JSONObject.parseObject(sourceAsString);
                System.out.println("logger==>" + messageJsonObject.getString("logger"));
                System.out.println("message==>" + messageJsonObject.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSaveDeviceAttributeMsgLog() {

        // 属性日志存储
        JSONObject attributeJSON = new JSONObject();
        attributeJSON.put("uuid", "1269");
        JSONObject attributes = new JSONObject();
        attributes.put("currentTemperature", "28");
        attributeJSON.put("attributes", attributes);
        messageLogService.saveDeviceMsgLog(attributeJSON.toJSONString());

    }

    @Test
    public void testSaveDeviceAffairMsgLog() {

        // 事件日志存储
        JSONObject affairJSON = new JSONObject();
        affairJSON.put("uuid", "1269");
        JSONObject affairs = new JSONObject();
        JSONObject val = new JSONObject();
        val.put("Type", 1);
        affairs.put("tempAlarm", val);
        affairJSON.put("affairs", affairs);
        messageLogService.saveDeviceMsgLog(affairJSON.toJSONString());
    }

}
