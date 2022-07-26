package cps.device.dyncar.instance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cps.device.dyncar.entity.DynCar;
import cps.device.dyncar.entity.TracePos;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Junho
 * @date 2022/7/26 20:26
 */
public class DynCarInstance {

    @PostConstruct
    public void initTaxis(){
        initDynCarMap();
    }

    /**
     * key:userid value:位置信息队列
     */
    private static ConcurrentHashMap<String , ConcurrentLinkedQueue<TracePos>> dynCarMap;

    private DynCarInstance(){
    }

    public static ConcurrentHashMap<String , ConcurrentLinkedQueue<TracePos>> getDynCarMap() {
        if (dynCarMap == null) {
            initDynCarMap();
        }
        return dynCarMap;
    }

    private static void initDynCarMap() {
        dynCarMap = new ConcurrentHashMap<>();
    }
}
