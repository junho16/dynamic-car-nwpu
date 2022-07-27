package cps.device.dyncar.instance;

import com.onenet.studio.acc.sdk.OpenApi;
import cps.device.dyncar.entity.TracePos;
import cps.device.onenetapi.DynCarOpenApiExtention;
import cps.device.onenetapi.dto.DynCarListStructDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 实时上报已建立连接的各车辆的行驶信息
 * @author Junho
 * @date 2022/7/26 20:57
 */
@Slf4j
public class DynCarSubInfoTask implements Runnable{

    private Integer dyncarNumLimit;

    private OpenApi openApi;

    public DynCarSubInfoTask(Integer limit, OpenApi openApi) {
        this.dyncarNumLimit = limit;
        this.openApi = openApi;
    }

    @Override
    public void run() {
        while (true){
            DynCarOpenApiExtention apiExt = new DynCarOpenApiExtention(openApi);
            Map<String , ConcurrentLinkedQueue<TracePos>> map = DynCarInstance.getDynCarMap();
            while (map.entrySet().size() > 0){
                try {
                    DynCarListStructDTO[] dyncars = getDynCarData();
                    /**
                     * 经测试 100ms~500ms左右可以保证 客户端上传数据后 可以直接上报至onenet 耗时为1s（即队列size永远为 1）
                     */
                    apiExt.DynCarListPropertyUpload(dyncars , 500);
                    log.info("上报成功，上报的数据：{}",dyncars);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("实时车辆检测器上报当前车辆列表情况失败");
                }
            }
        }
    }

    private DynCarListStructDTO[] getDynCarData(){
        List<TracePos> dyncarList = new ArrayList<>();
        DynCarListStructDTO[] dyncars = new DynCarListStructDTO[dyncarNumLimit];
        Map<String , ConcurrentLinkedQueue<TracePos>> map = DynCarInstance.getDynCarMap();
        for(Map.Entry<String , ConcurrentLinkedQueue<TracePos>> entry : map.entrySet()){
            if(entry.getValue().size() != 0){
                dyncarList.add(entry.getValue().poll());
            }
        }
        for(int idx = 0 , j = 0 ; idx < dyncarNumLimit && dyncarList.size() > 0 ;  idx++ , j = (j + 1) % dyncarList.size()){
            DynCarListStructDTO car = new DynCarListStructDTO(
                dyncarList.get(j).getRid(),
                dyncarList.get(j).getLon(),
                dyncarList.get(j).getLat(),
                dyncarList.get(j).getSpeed(),
                dyncarList.get(j).getTs()
            );
            dyncars[idx] = car;
        }
        return dyncars;
    }
}
