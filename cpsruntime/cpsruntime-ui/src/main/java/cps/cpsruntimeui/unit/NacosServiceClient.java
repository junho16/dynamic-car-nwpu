package cps.cpsruntimeui.unit;

import cps.runtime.api.service.NacosService;
/**
 * FeignClient value：nacos中服务提供者名
 */
@org.springframework.cloud.openfeign.FeignClient(value = "cpsruntimeservice")
public interface NacosServiceClient extends NacosService {


}
