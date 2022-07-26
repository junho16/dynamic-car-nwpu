package cps.cpsdataaccess.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cps.data.api.service.MessageLogService;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 消息日志服务接口实现类，基于es方式存储
 */
@Service
public class MessageLogServiceESImpl implements MessageLogService {

    private final static Logger logger = LoggerFactory.getLogger(MessageLogServiceESImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void saveDeviceMsgLog(String deviceMsgLog) {
        // 设备消息日志 {"attributes":{"currentTemperature":"28"},"uuid":"1269"} {"uuid":"1269","affairs":{"tempAlarm":"on"}}
//        logger.info("receive deviceMsgLog=======" + deviceMsgLog);
        try {

            if (deviceMsgLog != null && !"".equals(deviceMsgLog)) {
                JSONObject deviceMsgLogJson = JSONObject.parseObject(deviceMsgLog);
                String index = "";
                if (deviceMsgLogJson.getJSONObject("attributes") != null) {
                    index = "attributes";
                } else if (deviceMsgLogJson.getJSONObject("affairs") != null) {
                    index = "affairs";
                } else if (deviceMsgLogJson.getJSONObject("actions") != null) {
                    index = "actions";
                }
                IndexRequest indexRequest = new IndexRequest("device_" + index);

                // 封装日志数据
                JSONObject msgVal = deviceMsgLogJson.getJSONObject(index);
                deviceMsgLogJson.remove(index);
                deviceMsgLogJson.put("message", msgVal.toJSONString());
                indexRequest.source(deviceMsgLogJson.toJSONString().getBytes(), XContentType.JSON);

                // 异步执行
                restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                    // 调用成功时回调，返回信息作为参数传入
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        logger.debug("save deviceMsgLog success.");
                    }

                    // 调用失败时回调，错误信息作为参数传入
                    @Override
                    public void onFailure(Exception e) {
                        logger.error("save deviceMsgLog fail,Exception:" + e.getMessage());
                    }
                });

            } else {
                logger.info("receive deviceMsgLog is empty.");
            }

        } catch (Exception e) {
            logger.error("saveDeviceMsgLog catch Exception:" + e.getMessage());
        }
    }

    @Override
    public void saveCPMsgLog(String cpMsgLog) {
        // {"attributes":{"TH_sensor1_currentTemperature":"27"},"uuid":"33"}   {"uuid":"33","affairs":{"temperatureAlarm_1":"on"}}
//        logger.info("receive cpMsgLog=======" + cpMsgLog);
        try {

            if (cpMsgLog != null && !"".equals(cpMsgLog)) {
                JSONObject cpMsgLogJson = JSONObject.parseObject(cpMsgLog);
                String index = "";
                if (cpMsgLogJson.getJSONObject("attributes") != null) {
                    index = "attributes";
                } else if (cpMsgLogJson.getJSONObject("affairs") != null) {
                    index = "affairs";
                } else if (cpMsgLogJson.getJSONObject("actions") != null) {
                    index = "actions";
                }
                IndexRequest indexRequest = new IndexRequest("cp_" + index);

                // 日志数据封装
                JSONObject msgVal = cpMsgLogJson.getJSONObject(index);
                cpMsgLogJson.remove(index);
                cpMsgLogJson.put("message", msgVal.toJSONString());
                indexRequest.source(cpMsgLogJson.toJSONString().getBytes(), XContentType.JSON);

                // 异步执行
                restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                    // 调用成功时回调，返回信息作为参数传入
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        logger.debug("save cpMsgLog success.");
                    }

                    // 调用失败时回调，错误信息作为参数传入
                    @Override
                    public void onFailure(Exception e) {
                        logger.error("save cpMsgLog fail,Exception:" + e.getMessage());
                    }
                });

            } else {
                logger.info("receive cpMsgLog is empty.");
            }

        } catch (Exception e) {
            logger.error("saveCPMsgLog catch Exception:" + e.getMessage());
        }

    }

    @Override
    public void saveCPSMsgLog(String cpsMsgLog) {
//        logger.info("receive cpsMsgLog=======" + cpsMsgLog);
        try {

            if (cpsMsgLog != null && !"".equals(cpsMsgLog)) {
                JSONObject cpsMsgLogJson = JSONObject.parseObject(cpsMsgLog);
                String index = "";
                if (cpsMsgLogJson.getJSONObject("attributes") != null) {
                    index = "attributes";
                } else if (cpsMsgLogJson.getJSONObject("affairs") != null) {
                    index = "affairs";
                } else if (cpsMsgLogJson.getJSONObject("actions") != null) {
                    index = "actions";
                }
                IndexRequest indexRequest = new IndexRequest("cps_" + index);
                // 日志数据封装
                JSONObject msgVal = cpsMsgLogJson.getJSONObject(index);
                cpsMsgLogJson.remove(index);
                cpsMsgLogJson.put("message", msgVal.toJSONString());
                indexRequest.source(cpsMsgLogJson.toJSONString().getBytes(), XContentType.JSON);

                // 异步执行
                restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                    // 调用成功时回调，返回信息作为参数传入
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        logger.debug("save cpsMsgLog success.");
                    }

                    // 调用失败时回调，错误信息作为参数传入
                    @Override
                    public void onFailure(Exception e) {
                        logger.error("save cpsMsgLog fail,Exception:" + e.getMessage());
                    }
                });

            } else {
                logger.info("receive cpsMsgLog is empty.");
            }

        } catch (Exception e) {
            logger.error("saveCPSMsgLog catch Exception:" + e.getMessage());
        }
    }

}
