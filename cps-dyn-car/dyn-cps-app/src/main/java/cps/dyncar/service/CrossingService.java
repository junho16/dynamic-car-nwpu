package cps.dyncar.service;

import cps.dyncar.entity.DetectorMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/7/18 16:02
 */
@Service
public class CrossingService {

    //探测器500米范围内
    @Value("${detector_range}")
    private double minDis;

    @Resource
    ConcurrentHashMap<String , DetectorMeta> detectorMap;

    @Resource
    DisTanceService disTanceService;

    public DetectorMeta getMinDisDetector(Double lon, Double lat) {
        for(Map.Entry<String , DetectorMeta> entry : detectorMap.entrySet()){
            if(minDis >= disTanceService.calcDistance(lon , lat , entry.getValue().getLight_pos().getLon(), entry.getValue().getLight_pos().getLat())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
