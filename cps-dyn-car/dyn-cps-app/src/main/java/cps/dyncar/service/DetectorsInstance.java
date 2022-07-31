package cps.dyncar.service;

import com.alibaba.fastjson.JSONObject;
import cps.dyncar.entity.DetectorMeta;
import cps.dyncar.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/18 13:58
 */
@Component
public class DetectorsInstance {

    //路口探测器文件名
    @Value("${init.bean.detector_file_name}")
    private String detectionFileName;

    /**
     *  init crossing detector
     * @return
     */
    @Bean("detectorMap")
    private ConcurrentHashMap<String , DetectorMeta> initDetectorMap() {
        ConcurrentHashMap<String , DetectorMeta> map = new ConcurrentHashMap<>();
        List<DetectorMeta> list = JSONObject.parseArray(JsonUtil.readJsonFile(detectionFileName), DetectorMeta.class);
        for(DetectorMeta detectorMeta : list){
            map.put(detectorMeta.getDetector_id() , detectorMeta);
        }
        return map;
    }

}
