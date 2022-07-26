package cps.device.task;

import com.onenet.studio.acc.sdk.OpenApi;
import cps.device.bdgpsdevice.BdGpsOpenApiExtention;
import cps.device.bdgpsdevice.dto.$GeoLocationStructDTO;
import cps.device.engine.EngineOpenApiExtention;
import cps.device.gpsdevice.GpsOpenApiExtention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备主动上报事件的定时任务
 */
@Component
@EnableScheduling
public class DeviceAutoUploadTask {
    private static final Logger log = LoggerFactory.getLogger(DeviceAutoUploadTask.class);

    @Resource
    ConcurrentHashMap<String, List<OpenApi>> openApis;

    @Scheduled(cron = "0 0 1 * * ?")
    public void getOpenAPICount() {
        int statue = 0;
        for (String productId : openApis.keySet()) {    //gps
            if ("710r9BWp9V".equals(productId)) {
                List<OpenApi> gpsOpenApis = this.openApis.get(productId);
                for (OpenApi openApi : gpsOpenApis) {
                    try {
                        GpsOpenApiExtention extention = new GpsOpenApiExtention(openApi);
                        extention.propertyUpload(5000, getLatiTudeValue(), getLongiTudeValue(), statue);
                    } catch (Exception e) {
                        log.info("GPS属性上报失败");
                    }
                }
            } else if ("5P2quw1MIt".equals(productId)) {    //北斗gps
                List<OpenApi> bdGpsOpenApis = this.openApis.get(productId);
                for (OpenApi openApi : bdGpsOpenApis) {
                    try {
                        BdGpsOpenApiExtention extention = new BdGpsOpenApiExtention(openApi);
                        //平台设备内部功能点 地理位置
                        $GeoLocationStructDTO location = new $GeoLocationStructDTO();
                        location.setAltitude(300.00);//高度
                        location.setLongitude(Double.parseDouble(getLongiTudeValue()));//经度
                        location.setLatitude(Double.parseDouble(getLatiTudeValue()));//纬度
                        location.setCoordinateSystem(2);//坐标系统
                        extention.propertyUpload(5000, location, getLatiTudeValue(), getLongiTudeValue(), statue);
//                        extention.latiTudeBDPropertyUpload(getLatiTudeValue(),5000);
//                        extention.longiTudeBDPropertyUpload(getLongiTudeValue(),5000);
//                        extention.runningStateBDPropertyUpload(statue,500);
                    } catch (Exception e) {
                        log.info("BDGps属性上报失败");
                    }
                }
            } else if ("5f5cUXBTC7".equals(productId)) {  //引擎
                List<OpenApi> engineOpenApis = this.openApis.get(productId);
                for (OpenApi openApi : engineOpenApis) {
                    try {
                        EngineOpenApiExtention extention = new EngineOpenApiExtention(openApi);
                        Random rand = new Random();
                        double temp = rand.nextDouble() * 151;
                        DecimalFormat df = new DecimalFormat("0.00");
                        String str = df.format(temp);
                        extention.propertyUpload(5000, statue, Double.parseDouble(str));
                    } catch (Exception e) {
                        log.info("engine属性上报失败");
                    }
                }
            }
        }
    }

    private DecimalFormat df = new DecimalFormat("0.000000");

    //经度
    private String getLatiTudeValue() {
        return df.format(Math.random() * 180);
    }

    //经度
    private String getLongiTudeValue() {
        return df.format(Math.random() * 900);
    }
}
