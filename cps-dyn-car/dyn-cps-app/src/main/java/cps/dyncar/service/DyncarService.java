package cps.dyncar.service;

import com.alibaba.fastjson.JSONObject;
import cps.dyncar.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author Junho
 * @date 2022/7/31 12:37
 */
@Service
@Slf4j
public class DyncarService {

    @Value("${amap.driving.baseurl}")
    private String baseurl;

    @Resource
    RestTemplate restTemplate;

    public String getPosInfo(Double lon, Double lat) {

        try{
            String parameters = "&location=" +
                    FormatUtil.getPosFormatDouble(lon) + "," +
                    FormatUtil.getPosFormatDouble(lat);
            String url = baseurl + parameters;
            //经纬度也可以直接写在方法的参数里
            String result = restTemplate.getForObject(url, String.class);
//            log.info("result:{}",result);
            JSONObject jsonObject = JSONObject.parseObject(result);

            if( jsonObject.get("infocode").equals("10000") && jsonObject.get("status").equals("1") ){
                JSONObject regeocode = jsonObject.getJSONObject("regeocode");
                String formatted_address = regeocode.getString("formatted_address");
                return formatted_address;
            }else{
                log.info("逆地理编码失败: {}" ,  jsonObject.get("info"));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("逆地理编码失败，异常为：{}" , e.getMessage());
        }
        return "";
    }
}

