package cpsruntimeservice;

import com.alibaba.fastjson.JSONObject;
import cps.api.entity.*;
import cps.api.entity.meta.*;
import cps.cpsruntimeservice.CPSRuntimeServiceApplication;
import cps.cpsruntimeservice.dao.CPMetaMapper;
import cps.cpsruntimeservice.dao.CPSMetaMapper;
import cps.cpsruntimeservice.dao.DeviceMetaMapper;
import cps.cpsruntimeservice.dto.*;
import cps.cpsruntimeservice.service.CPEntityServiceImpl;
import cps.cpsruntimeservice.service.CPSInstanceServiceImpl;
import cps.runtime.api.entity.imp.DefaultDevice;
import cps.runtime.api.entity.imp.RedisCPEntity;
import cps.runtime.api.entity.imp.RedisCPSInstance;
import cps.runtime.api.service.*;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CPSRuntimeServiceApplication.class)
public class CPSTest {

    private final static Logger log = LoggerFactory.getLogger(CPSTest.class);

    @Reference
    private DeviceService deviceMetaService;

    @Reference
    private CPSInstanceService cpsInstanceService;

    @Resource
    private CPEntityServiceImpl cpEntityMetaService;

    @Resource
    private CPSInstanceFactory cpsInstanceFactory;

    @Resource
    private CPSInstanceServiceImpl cpsInstanceMetaService;

    @Test
    public void deviceMetaTest() throws DeviceException {
        DeviceMeta deviceMeta = deviceMetaService.getDeviceMetaByUUID("1");
        log.info(JSONObject.toJSONString(deviceMeta));
        System.out.println(JSONObject.toJSONString(deviceMeta));
    }

    @Test
    public void cpEntityMetaTest() throws CPEntityException {
        CPEntityMeta cpEntityMeta = cpEntityMetaService.getCPEntityMetaByUUID("1");
        log.info(JSONObject.toJSONString(cpEntityMeta));
        System.out.println(JSONObject.toJSONString(cpEntityMeta));
    }

    @Test
    public void deviceMetaByIotUUIDTest() throws DeviceException {
        DeviceMeta deviceMeta = deviceMetaService.getDeviceMetaByIotUUID("710r9BWp9V_GPS_500");
        log.info(JSONObject.toJSONString(deviceMeta));
    }

    @Test
    public void addDeviceMetaTest() throws DeviceException {
        DeviceMeta deviceMeta = new DeviceMeta();
        deviceMeta.setName("414test103");
        deviceMeta.setDeviceType(DeviceTypeEnum.powerCollector);
        deviceMeta.setIotUuid("7e6DFz0zgU_414test2");
        deviceMeta.setManagementStatus(ManagementStatusEnum.enable);
        deviceMeta.setCreateTime(new Date());
        deviceMeta.setUpdateTime(new Date());
        deviceMeta.setDeleteFlag(DeleteFlagEnum.valueOf("inuse"));

        deviceMeta.putAttributeMeta("testAttributeMeta", new DeviceAttributeMeta("testAttributeMeta", "testIotAttributeMeta", "String", "属性测试"));
        deviceMeta.putAffairMeta("testAffairMeta", new DeviceAffairMeta("testAffairMeta", "testIotAffairMeta", "String", "事件测试"));
        deviceMeta.putActionMeta("testActionMeta", new DeviceActionMeta("testActionMeta", "testIotActionMeta", "String", "操作测试"));
//        deviceMeta.putAttributeMeta("testAttributeMeta1", new DeviceAttributeMeta("testAttributeMeta1", "String", "testAttributeMeta", "testRoleCategory446", "testRoleContent446"));
//        deviceMeta.putAffairMeta("testAffairMeta", new DeviceAffairMeta("testAffairMeta", "enum", "testAffairMeta", "", ""));
//        deviceMeta.putActionMeta("testActionMeta", new DeviceActionMeta("testActionMeta", "String", "testActionMeta", "", ""));

//        deviceMeta.removeAttributeMeta("testAttributeMeta");
//        deviceMeta.removeAffairMeta("testAffairMeta");
//        deviceMeta.removeActionMeta("testActionMeta");

        deviceMetaService.addOrUpdateDeviceMeta(deviceMeta);

//        DeviceMeta deviceMeta2 = deviceMetaService.getDeviceMetaByUUID("3");
//        deviceMeta2.setIotUuid("testIotUuid3");
//        deviceMeta2.setName("testGPS3");

//        deviceMetaService.addOrUpdateDeviceMeta(deviceMeta);
    }

    @Test
    public void testBuildDevice() throws DeviceException {
        Device device = cpsInstanceFactory.buildDevice("1");
        log.info(JSONObject.toJSONString(device));
    }

    @Test
    public void testBuildCPEntity() throws CPEntityException {
        CPEntity cpEntity = cpsInstanceFactory.buildCPEntity("1");
        cpEntity.startUp();
        log.info("cpEntity============" + JSONObject.toJSONString(cpEntity));
        log.info("devices=============" + JSONObject.toJSONString(cpEntity.allDevices()));
    }

    @Test
    public void testGetCPSInstanceMetaByUUID() throws CPSInstanceException, UnsupportMetaException {
        CPSInstanceMeta cpsInstanceMeta = cpsInstanceMetaService.getCPSInstanceMetaByUUID("1");
        log.info(JSONObject.toJSONString(cpsInstanceMeta));
        System.out.println(JSONObject.toJSONString(cpsInstanceMeta));
    }

    @Test
    public void testBuildCPSInstance() throws CPSInstanceException {
        CPSInstance cpsEntity = cpsInstanceFactory.buildCPSInstance("1");
        log.info("cpsEntity============" + JSONObject.toJSONString(cpsEntity));
        log.info("CPEntities=============" + JSONObject.toJSONString(cpsEntity.allCPEntities()));
    }

    @Test
    public void testGetCPSListByDeviceUuidAndAffairName() throws CPSInstanceException {
        List<DeviceRelatedEntity> result = cpsInstanceMetaService.getCPSListByDeviceUuidAndAffairName("1", "LOS");
        log.info("cpsInstanceMetaIds============" + JSONObject.toJSONString(result));
    }

    @Test
    public void testGetCPSListByDeviceUuidAndAttributeName() throws CPSInstanceException {
        List<DeviceRelatedEntity> result = cpsInstanceMetaService.getCPSListByDeviceUuidAndAttributeName("1", "latitude");
        log.info("cpsInstanceMetaIds============" + JSONObject.toJSONString(result));
    }

    @Test
    public void testGetCPListByDeviceUuidAndAffairName() throws CPEntityException {
        List<DeviceRelatedEntity> result = cpEntityMetaService.getCPListByDeviceUuidAndAffairName("1", "LOS");
        log.info("cpsInstanceMetaIds============" + JSONObject.toJSONString(result));
    }

    @Test
    public void testGetCPListByDeviceUuidAndAttributeName() throws CPEntityException {
        List<DeviceRelatedEntity> result = cpEntityMetaService.getCPListByDeviceUuidAndAttributeName("1", "latitude");
        log.info("cpsInstanceMetaIds============" + JSONObject.toJSONString(result));
    }

    @Resource
    private DeviceMetaMapper deviceMetaMapper;

    @Test
    public void testInsertDeviceMeta() {
        DeviceMetaEntity deviceMetaEntity = new DeviceMetaEntity();
        deviceMetaEntity.setName("testName");
        deviceMetaEntity.setDeviceType("testDeviceType");
        deviceMetaEntity.setIotUuid("testIotUuid");
        deviceMetaEntity.setOrganization("testOrganization");
        deviceMetaEntity.setUseOrganization("testUseOrganization");
        deviceMetaEntity.setField("testFiled");
        deviceMetaEntity.setManagementStatus("enable");
        deviceMetaEntity.setMessageProtocol("testMessageProtocol");
        deviceMetaEntity.setEquipmentType("device");
        deviceMetaEntity.setConnectionProtocol("testConnectionProtocol");
        deviceMetaEntity.setCategory("testCategory");
        deviceMetaEntity.setLocation("testLocation");
        deviceMetaEntity.setNetworkMode("testNetworkMode");
        deviceMetaEntity.setIpAddress("testIpAddress");
        deviceMetaEntity.setAuthenticationMode("testAuthenticationMode");
        deviceMetaEntity.setTags("testTags");
        deviceMetaEntity.setDescription("testDescription");
        deviceMetaEntity.setCreateTime(new Date());
        deviceMetaEntity.setCreateUser("testCreateUser");
        deviceMetaEntity.setUpdateTime(new Date());
        deviceMetaEntity.setUpdateUser("testUpdateUser");
        deviceMetaEntity.setRegisterTime(new Date());
        deviceMetaEntity.setLastOnlineTime(new Date());
        deviceMetaEntity.setDeleteFlag("inuse");
        deviceMetaMapper.insertDeviceMetaEntity(deviceMetaEntity);
    }

    @Test
    public void testSelectDeviceMetaEntityById() {
        DeviceMetaEntity deviceMetaEntity = deviceMetaMapper.selectDeviceMetaEntityById("1246");
        log.info("deviceMetaEntity selectDeviceMetaEntityById============" + JSONObject.toJSONString(deviceMetaEntity));
    }

    @Test
    public void testUpdateDeviceMeta() {
        DeviceMetaEntity deviceMetaEntity = deviceMetaMapper.selectDeviceMetaEntityByIotUUID("testIotUuid");
        log.info("deviceMetaEntity selectDeviceMetaEntityByIotUUID============" + JSONObject.toJSONString(deviceMetaEntity));
        deviceMetaEntity.setName("testName1");
        deviceMetaEntity.setDeviceType("testDeviceType1");
        deviceMetaEntity.setIotUuid("testIotUuid1");
        deviceMetaEntity.setOrganization("testOrganization1");
        deviceMetaEntity.setUseOrganization("testUseOrganization1");
        deviceMetaEntity.setField("testFiled1");
        deviceMetaEntity.setManagementStatus("enable");
        deviceMetaEntity.setMessageProtocol("testMessageProtocol1");
        deviceMetaEntity.setEquipmentType("device");
        deviceMetaEntity.setConnectionProtocol("testConnectionProtocol1");
        deviceMetaEntity.setCategory("testCategory1");
        deviceMetaEntity.setLocation("testLocation1");
        deviceMetaEntity.setNetworkMode("testNetworkMode1");
        deviceMetaEntity.setIpAddress("testIpAddress1");
        deviceMetaEntity.setAuthenticationMode("testAuthenticationMode1");
        deviceMetaEntity.setTags("testTags1");
        deviceMetaEntity.setDescription("testDescription1");
        deviceMetaEntity.setCreateTime(new Date());
        deviceMetaEntity.setCreateUser("testCreateUser1");
        deviceMetaEntity.setUpdateTime(new Date());
        deviceMetaEntity.setUpdateUser("testUpdateUser1");
        deviceMetaEntity.setRegisterTime(new Date());
        deviceMetaEntity.setLastOnlineTime(new Date());
        deviceMetaEntity.setDeleteFlag("inuse");
        deviceMetaMapper.updateDeviceMetaEntity(deviceMetaEntity);
    }

    @Resource
    private CPMetaMapper cpMetaMapper;

    @Test
    public void testSelectCPMetaEntityById() {
        CPMetaEntity cpMetaEntity = cpMetaMapper.selectCPMetaEntityById("1");
        log.info("cpMetaEntity selectCPMetaEntityById============" + JSONObject.toJSONString(cpMetaEntity));
    }

    @Test
    public void testSelectCPMetaDeviceMetaEntityByCPId() {
        List<CPMetaDeviceMetaEntity> cpMetaDeviceMetaEntities = cpMetaMapper.selectCPMetaDeviceMetaEntityByCPId("1");
        log.info("cpMetaDeviceMetaEntities selectCPMetaDeviceMetaEntityByCPId============" + JSONObject.toJSONString(cpMetaDeviceMetaEntities));
    }

    @Resource
    private CPSMetaMapper cpsMetaMapper;

    @Test
    public void testSelectCPSMetaEntityById() {
        CPSMetaEntity cpsMetaEntity = cpsMetaMapper.selectCPSMetaEntityById("1");
        log.info("cpsMetaEntity selectCPSMetaEntityById============" + JSONObject.toJSONString(cpsMetaEntity));
    }

    @Test
    public void testSelectCPSMetaCPMetaEntityByCPSId() {
        List<CPSMetaCPMetaEntity> cpsMetaCPMetaEntities = cpsMetaMapper.selectCPSMetaCPMetaEntityByCPSId("1");
        log.info("cpsMetaCPMetaEntities selectCPSMetaCPMetaEntityByCPSId============" + JSONObject.toJSONString(cpsMetaCPMetaEntities));
    }

    @Test
    public void testUpdateCPSInstanceMetaManagementStatus() {
        cpsInstanceMetaService.updateCPSInstanceMetaManagementStatus("3", ManagementStatusEnum.enable);
    }

    /**
     * 根据机房空调设定温度计算机房空调风量
     */
    @Test
    public void testGetLTOne() {
        // 当前温度Tc以及设定温度Ts
        double Tc = 24.0, Ts = 17.0;
        BigDecimal Q = new BigDecimal("40");
        // 计算当前温度水蒸气分压力 Pv-c,设定温度水蒸气分压力 Pv-s
        BigDecimal Pvc = testGetPv(Tc);
        BigDecimal Pvs = testGetPv(Ts);
        // 计算当前温度含湿量Xc，设定温度含湿量Xs
        BigDecimal Xc = new BigDecimal("0.622").multiply(Pvc).divide(new BigDecimal("101325").subtract(Pvc), BigDecimal.ROUND_HALF_UP);
        BigDecimal Xs = new BigDecimal("0.622").multiply(Pvs).divide(new BigDecimal("101325").subtract(Pvs), BigDecimal.ROUND_HALF_UP);
        // 计算当前温度比焓Ic，设定温度比焓Is  I=1.010∙t+X∙(2490+1.84∙t)
        BigDecimal Ic = new BigDecimal("1.010").multiply(new BigDecimal(Tc)).add(Xc.multiply(new BigDecimal("2490").add(new BigDecimal("1.84").multiply(new BigDecimal(Tc)))));
        BigDecimal Is = new BigDecimal("1.010").multiply(new BigDecimal(Ts)).add(Xs.multiply(new BigDecimal("2490").add(new BigDecimal("1.84").multiply(new BigDecimal(Ts)))));
        // 计算当前温度比容υc，设定温度比容υs  υ=(t+273.15)/(0.00348P-0.00132Pv )
        BigDecimal υc = (new BigDecimal(Tc).add(new BigDecimal("273.15")))
                .divide(new BigDecimal("0.00348").multiply(new BigDecimal("101325")).subtract(new BigDecimal("0.00132").multiply(Pvc)), BigDecimal.ROUND_HALF_UP);
        BigDecimal υs = (new BigDecimal(Ts).add(new BigDecimal("273.15")))
                .divide(new BigDecimal("0.00348").multiply(new BigDecimal("101325")).subtract(new BigDecimal("0.00132").multiply(Pvs)), BigDecimal.ROUND_HALF_UP);
        // 空调风量计算L  Q=(L∙(Ic-Is ))/(υ∙(1+X))  L=Q∙υs∙(1+Xc)/(Ic-Is)
        BigDecimal L = Q.multiply(υs).multiply(new BigDecimal("1").add(Xc)).divide(Ic.subtract(Is), BigDecimal.ROUND_HALF_UP);
        System.out.println("空调设定温度T：（℃）" + Ts + "，风量L：（m3/s）" + L);
    }

    @Test
    public void testGetLTTwo() {
        // 当前温度Tc以及设定温度Ts
        double Tc = 24.0, Ts = 19.0;
        BigDecimal Q = new BigDecimal("40");

        /*************************根据空调风量与空调设定温度公式进行计算  Q∙(Ts+273.15)∙F1∙F2/F3∙(F4-F5+F6+F7)*************************/
        // 开尔文:热力学温标  开尔文(K)=273.15+摄氏度(T)  当前温度对应的开尔文温度Kc，设定温度对应的开尔文温度Ks
        BigDecimal Kc = new BigDecimal("273.15").add(new BigDecimal(Tc));
        BigDecimal Ks = new BigDecimal("273.15").add(new BigDecimal(Ts));
        // 计算当前温度水蒸气分压力 Pv-c,设定温度水蒸气分压力 Pv-s
        BigDecimal Pvc = testGetPv(Tc);
        BigDecimal Pvs = testGetPv(Ts);
        // 计算公式中各个小公式的值 F1=(P-0.378Pvc)
        BigDecimal F1 = new BigDecimal("101325").subtract(new BigDecimal("0.378").multiply(Pvc));
        // F2=(P-Pvs)
        BigDecimal F2 = new BigDecimal("101325").subtract(Pvs);
        // F3=(0.00348P-0.00132Pvs)
        BigDecimal F3 = new BigDecimal("0.00348").multiply(new BigDecimal("101325")).subtract(new BigDecimal("0.00132").multiply(Pvs));
        // F4=(1.01P^2-0.13448PvcPvs)∙(Tc-Ts)
        BigDecimal F4 = (new BigDecimal("1.01").multiply(new BigDecimal("101325").multiply(new BigDecimal("101325"))).subtract(new BigDecimal(0.13448).multiply(Pvc).multiply(Pvs)))
                .multiply(new BigDecimal(Tc).subtract(new BigDecimal(Ts)));
        // F5=1.01P(TcPvs-TsPvc)
        BigDecimal F5 = new BigDecimal("1.01").multiply(new BigDecimal("101325")).multiply(new BigDecimal(Tc).multiply(Pvs).subtract(new BigDecimal(Ts).multiply(Pvc)));
        // F6=0.13448P(TcPvc-TsPvs)
        BigDecimal F6 = new BigDecimal("0.13448").multiply(new BigDecimal("101325")).multiply(new BigDecimal(Tc).multiply(Pvc).subtract(new BigDecimal(Ts).multiply(Pvs)));
        // F7=1548.78P(Pvc-Pvs)
        BigDecimal F7 = new BigDecimal("1548.78").multiply(new BigDecimal("101325")).multiply(Pvc.subtract(Pvs));
        // 套用总公式计算空调风量 Q∙(Ts+273.15)∙F1∙F2/F3∙(F4-F5+F6+F7)
        BigDecimal L = Q.multiply(Ks).multiply(F1).multiply(F2).divide(F3.multiply(F4.subtract(F5).add(F6).add(F7)), BigDecimal.ROUND_HALF_UP);
        System.out.println("空调设定温度T：（℃）" + Ts + "，风量L：（m3/s）" + L);
    }


    /**
     * 根据温度计算水蒸气的分压力Pv
     *
     * @param t
     * @return
     */
    private BigDecimal testGetPv(double t) {
        // 次方数
        BigDecimal a = new BigDecimal(18.3036);
        BigDecimal b = new BigDecimal(3816.44);
        BigDecimal c = new BigDecimal(t + 227.02);
        BigDecimal x = a.subtract(b.divide(c, BigDecimal.ROUND_HALF_UP));
        double e = Math.E;//自然常数e的近似值
        double result = Math.pow(e, x.doubleValue());
        System.out.println("e^" + x + "=" + result);
        // 计算水蒸气的饱和压力Ps（Pa） Ps=133.332×exp{18.3036-3816.44/(t+227.02)}
        BigDecimal Ps = new BigDecimal("133.332").multiply(new BigDecimal(result));
        // 水蒸气的分压力Pv（Pa）
        BigDecimal Pv = new BigDecimal("0.55").multiply(Ps);
        System.out.println("温度为" + t + "℃时对应的水蒸气分压力Pv为：" + Pv);
        return Pv;
    }

    /**
     * 查询平台描述结构信息
     */
    @Test
    public void getDescInfoJson() throws CPSInstanceException {
        long inTime = System.currentTimeMillis();
        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("1");
        JSONObject cpsJson = new JSONObject(new LinkedHashMap<>());
        List<JSONObject> cpsAttrList = new ArrayList<>();
        List<JSONObject> cpsAffList = new ArrayList<>();
        List<JSONObject> cpsActList = new ArrayList<>();
        cpsJson.put("uuid", cpsInstance.getUuid());
        cpsJson.put("name", cpsInstance.getName());
        // CPS属性
        ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = cpsInstance.getCpsInstanceMeta().getCpsAttributeMetas();
        for (CPSInstanceAttributeMeta cpsAttrMeta : cpsAttributeMetas.values()) {
            JSONObject cpsAttrJson = new JSONObject(new LinkedHashMap<>());
            String unit = "";
            cpsAttrJson.put("uuid", cpsAttrMeta.getUuid());
            cpsAttrJson.put("name", cpsAttrMeta.getAttributeName());
            cpsAttrJson.put("aliasName", cpsAttrMeta.getAliasName());
            cpsAttrJson.put("value", StringUtils.isNotBlank(cpsAttrMeta.getDefaultValue()) ? cpsAttrMeta.getDefaultValue() : "");
            // 拼接单位
            switch (cpsAttrMeta.getAttributeName()) {
                case "currentHumidity":
                case "setMaxHumidity":
                case "setMinHumidity":
                    unit = "%";
                    break;
                case "currentTemperature":
                case "setMaxTemperature":
                case "setMinTemperature":
                    unit = "℃";
                    break;
                case "acGrossPower":
                case "ecGrossPower":
                case "acRatedPower":
                case "ecRatedPower":
                    unit = "W";
                    break;
                case "length":
                case "weight":
                case "height":
                    unit = "mm";
                    break;
                case "totalCabinets":
                    unit = "个";
                    break;
            }
            cpsAttrJson.put("unit", unit);
            cpsAttrList.add(cpsAttrJson);
        }
        cpsAttrList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpsAttrList.forEach((attrJson) -> attrJson.remove("uuid"));

        cpsJson.put("attributes", cpsAttrList);
        // CPS事件
        ConcurrentHashMap<String, CPSInstanceAffairMeta> cpsAffairMetas = cpsInstance.getCpsInstanceMeta().getCpsAffairMetas();
        for (CPSInstanceAffairMeta instanceAffairMeta : cpsAffairMetas.values()) {
            JSONObject cpsAffJson = new JSONObject(new LinkedHashMap<>());
            cpsAffJson.put("uuid", instanceAffairMeta.getUuid());
            cpsAffJson.put("name", instanceAffairMeta.getAffairName());
            cpsAffJson.put("aliasName", instanceAffairMeta.getAliasName());
            cpsAffJson.put("value", "");
            cpsAffList.add(cpsAffJson);
        }
        cpsAffList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpsAffList.forEach((affJson) -> affJson.remove("uuid"));
        cpsJson.put("affairs", cpsAffList);
        // CPS操作
        ConcurrentHashMap<String, CPSInstanceActionMeta> cpsActionMetas = cpsInstance.getCpsInstanceMeta().getCpsActionMetas();
        for (CPSInstanceActionMeta instanceActionMeta : cpsActionMetas.values()) {
            JSONObject cpsActJson = new JSONObject(new LinkedHashMap<>());
            cpsActJson.put("uuid", instanceActionMeta.getUuid());
            cpsActJson.put("name", instanceActionMeta.getActionName());
            cpsActJson.put("aliasName", instanceActionMeta.getAliasName());
            cpsActJson.put("value", "");
            cpsActList.add(cpsActJson);
        }
        cpsActList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpsActList.forEach((actJson) -> actJson.remove("uuid"));
        cpsJson.put("actions", cpsActList);

        // CPS关联CP
        ConcurrentHashMap<String, CPSInstanceLinkCPEntityMeta> cpsLinkCpMetas = cpsInstance.getCpsInstanceMeta().getCpsLinkCpMetas();
        List<JSONObject> cpList = new ArrayList<>();
        Enumeration<CPEntity> cpEntities = cpsInstance.allCPEntities();
        while (cpEntities.hasMoreElements()) {
            JSONObject cpJson = new JSONObject(new LinkedHashMap<>());
            List<JSONObject> cpAttrList = new ArrayList<>();
            List<JSONObject> cpAffList = new ArrayList<>();
            List<JSONObject> cpActList = new ArrayList<>();
            CPEntity cpEntity = cpEntities.nextElement();
            // 拼接CP数据
            cpJson.put("uuid", cpEntity.getUuid());
            cpJson.put("name", cpEntity.getName());
            cpJson.put("cpEntityType", cpEntity.getCpEntityType());
            cpsLinkCpMetas.forEach((cpName, linkCPMeta) -> {
                if (linkCPMeta.getLinkCpUuid().equals(cpEntity.getUuid())) {
                    cpJson.put("aliasName", linkCPMeta.getAliasName());
                }
            });
            // CP属性
            ConcurrentHashMap<String, CPEntityAttributeMeta> cpAttributeMetas = cpEntity.getCpEntityMeta().getCpAttributeMetas();
            cpAttributeMetas.forEach((cpAttrName, cpAttributeMeta) -> {
                JSONObject cpAttrJson = new JSONObject(new LinkedHashMap<>());
                String unit = "";
                cpAttrJson.put("uuid", cpAttributeMeta.getUuid());
                cpAttrJson.put("name", cpAttributeMeta.getAttributeName());
                cpAttrJson.put("aliasName", cpAttributeMeta.getAliasName());
                cpAttrJson.put("value", StringUtils.isNotBlank(cpAttributeMeta.getDefaultValue()) ? cpAttributeMeta.getDefaultValue() : "");
                switch (cpAttributeMeta.getAttributeName()) {
                    case "humidity":
                    case "currentHumidity":
                    case "targetHumidity":
                        unit = "%";
                        break;
                    case "temperature":
                    case "currentTemperature":
                    case "targetTemperature":
                        unit = "℃";
                        break;
                    case "actualPower":
                    case "ratedPower":
                    case "acRefrigeratingCapacity":
                        unit = "W";
                        break;
                    case "length":
                    case "weight":
                    case "height":
                        unit = "mm";
                        break;
                    case "engagedPosition":
                    case "remainingPosition":
                        unit = "U";
                        break;
                    case "airVolume":
                        unit = "m³/h";
                        break;
                }
                cpAttrJson.put("unit", unit);
                cpAttrList.add(cpAttrJson);
            });
            cpAttrList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            cpAttrList.forEach((attrJson) -> attrJson.remove("uuid"));
            cpJson.put("attributes", cpAttrList);
            // CP事件
            ConcurrentHashMap<String, CPEntityAffairMeta> cpAffairMetas = cpEntity.getCpEntityMeta().getCpAffairMetas();
            cpAffairMetas.forEach((cpAffName, cpAffairMeta) -> {
                JSONObject cpAffJson = new JSONObject(new LinkedHashMap<>());
                cpAffJson.put("uuid", cpAffairMeta.getUuid());
                cpAffJson.put("name", cpAffairMeta.getAffairName());
                cpAffJson.put("aliasName", cpAffairMeta.getAliasName());
                cpAffJson.put("value", "");
                cpAffList.add(cpAffJson);
            });
            cpAffList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            cpAffList.forEach((affJson) -> affJson.remove("uuid"));
            cpJson.put("affairs", cpAffList);
            // CP操作
            ConcurrentHashMap<String, CPEntityActionMeta> cpActionMetas = cpEntity.getCpEntityMeta().getCpActionMetas();
            cpActionMetas.forEach((cpActName, cpActionMeta) -> {
                JSONObject cpActJson = new JSONObject(new LinkedHashMap<>());
                cpActJson.put("uuid", cpActionMeta.getUuid());
                cpActJson.put("name", cpActionMeta.getActionName());
                cpActJson.put("aliasName", cpActionMeta.getAliasName());
                cpActJson.put("value", "");
                cpActList.add(cpActJson);
            });
            cpActList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            cpActList.forEach((actJson) -> actJson.remove("uuid"));
            cpJson.put("actions", cpActList);

            // CP关联Device
            ConcurrentHashMap<String, CPEntityLinkDeviceMeta> cpLinkDeviceMetas = cpEntity.getCpEntityMeta().getCpLinkDeviceMetas();
            List<JSONObject> deviceList = new ArrayList<>();
            Enumeration<Device> devices = cpEntity.allDevices();
            while (devices.hasMoreElements()) {
                JSONObject devJson = new JSONObject(new LinkedHashMap<>());
                List<JSONObject> devAttrList = new ArrayList<>();
                List<JSONObject> devAffList = new ArrayList<>();
                List<JSONObject> devActList = new ArrayList<>();
                Device device = devices.nextElement();
                // 拼接设备数据
                devJson.put("uuid", device.getUuid());
                devJson.put("name", device.getName());
                devJson.put("deviceType", device.getDeviceType());
                cpLinkDeviceMetas.forEach((devName, linkDeviceMeta) -> {
                    if (linkDeviceMeta.getLinkDeviceUuid().equals(device.getUuid())) {
                        devJson.put("aliasName", linkDeviceMeta.getAliasName());
                    }
                });
                // Device属性
                ConcurrentHashMap<String, DeviceAttributeMeta> devAttrMetas = device.getDeviceMeta().getAttributeMetas();
                devAttrMetas.forEach((devAttrName, devAttrMeta) -> {
                    JSONObject devAttrJson = new JSONObject(new LinkedHashMap<>());
                    String unit = "";
                    devAttrJson.put("uuid", devAttrMeta.getUuid());
                    devAttrJson.put("name", devAttrMeta.getAttributeName());
                    devAttrJson.put("aliasName", devAttrMeta.getAliasName());
                    devAttrJson.put("value", "");
                    switch (devAttrMeta.getAttributeName()) {
                        case "currentHumidity":
                        case "maxHumSet":
                        case "miniHumSet":
                        case "targetHumidity":
                            unit = "%";
                            break;
                        case "currentTemperature":
                        case "maxTempSet":
                        case "miniTempSet":
                        case "targetTemperature":
                            unit = "℃";
                            break;
                        case "airVolume":
                            unit = "m³/h";
                            break;
                        case "actualPower":
                            unit = "W";
                            break;
                    }
                    devAttrJson.put("unit", unit);
                    devAttrList.add(devAttrJson);
                });
                devAttrList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
                devAttrList.forEach((attrJson) -> attrJson.remove("uuid"));
                devJson.put("attributes", devAttrList);
                // Device事件
                ConcurrentHashMap<String, DeviceAffairMeta> devAffMetas = device.getDeviceMeta().getAffairMetas();
                devAffMetas.forEach((devAffName, devAffMeta) -> {
                    JSONObject devAffJson = new JSONObject(new LinkedHashMap<>());
                    devAffJson.put("uuid", devAffMeta.getUuid());
                    devAffJson.put("name", devAffMeta.getAffairName());
                    devAffJson.put("aliasName", devAffMeta.getAliasName());
                    devAffJson.put("value", "");
                    devAffList.add(devAffJson);
                });
                devAffList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
                devAffList.forEach((affJson) -> affJson.remove("uuid"));
                devJson.put("affairs", devAffList);
                // Device操作
                ConcurrentHashMap<String, DeviceActionMeta> devActMetas = device.getDeviceMeta().getActionMetas();
                devActMetas.forEach((devActName, devActMeta) -> {
                    JSONObject devActJson = new JSONObject(new LinkedHashMap<>());
                    devActJson.put("uuid", devActMeta.getUuid());
                    devActJson.put("name", devActMeta.getActionName());
                    devActJson.put("aliasName", devActMeta.getAliasName());
                    devActJson.put("value", "");
                    devActList.add(devActJson);
                });
                devActList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
                devActList.forEach((actJson) -> actJson.remove("uuid"));
                devJson.put("actions", devActList);
                deviceList.add(devJson);
            }

            deviceList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            deviceList.forEach((devJson) -> devJson.remove("uuid"));
            cpJson.put("devices", deviceList);
            cpList.add(cpJson);
        }

        cpList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpList.forEach((cpJson) -> cpJson.remove("uuid"));
        cpsJson.put("cpEntities", cpList);

        System.out.println(System.currentTimeMillis() - inTime);
        System.out.println(cpsJson.toJSONString());
    }

    /**
     * baseProperties 属性、事件、操作
     */
    @Test
    public void getBaseInfoJson() throws CPSInstanceException {
        long inTime = System.currentTimeMillis();
        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("1");
        JSONObject cpsJson = new JSONObject(new LinkedHashMap<>());
        cpsJson.put("uuid", cpsInstance.getUuid());
        cpsJson.put("name", cpsInstance.getName());
        // CPS属性
        ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = cpsInstance.getCpsInstanceMeta().getCpsAttributeMetas();
        JSONObject cpsModelAttrJson = new JSONObject(new LinkedHashMap<>());
        cpsModelAttrJson.put("locationInfo", cpsAttributeMetas.get("location").getDefaultValue());
        cpsModelAttrJson.put("length", cpsAttributeMetas.get("length").getDefaultValue());
        cpsModelAttrJson.put("weight", cpsAttributeMetas.get("weight").getDefaultValue());
        cpsModelAttrJson.put("height", cpsAttributeMetas.get("height").getDefaultValue());
        cpsJson.put("modelData", cpsModelAttrJson);

        JSONObject cpsBasicAttrJson = new JSONObject(new LinkedHashMap<>());
        cpsBasicAttrJson.put("totalCabinets", cpsAttributeMetas.get("totalCabinets").getDefaultValue());
        cpsJson.put("basicData", cpsBasicAttrJson);

        // CPS关联CP
        ConcurrentHashMap<String, CPSInstanceLinkCPEntityMeta> cpsLinkCpMetas = cpsInstance.getCpsInstanceMeta().getCpsLinkCpMetas();
        List<JSONObject> cpList = new ArrayList<>();
        Enumeration<CPEntity> cpEntities = cpsInstance.allCPEntities();
        while (cpEntities.hasMoreElements()) {
            JSONObject cpJson = new JSONObject(new LinkedHashMap<>());
            CPEntity cpEntity = cpEntities.nextElement();

            // 拼接CP属性数据
            cpJson.put("uuid", cpEntity.getUuid());
            cpJson.put("name", cpEntity.getName());
            cpJson.put("cpEntityType", cpEntity.getCpEntityType());
            cpsLinkCpMetas.forEach((cpName, linkCPMeta) -> {
                if (linkCPMeta.getLinkCpUuid().equals(cpEntity.getUuid())) {
                    cpJson.put("aliasName", linkCPMeta.getAliasName());
                }
            });
            // CP属性
            ConcurrentHashMap<String, CPEntityAttributeMeta> cpAttributeMetas = cpEntity.getCpEntityMeta().getCpAttributeMetas();
            JSONObject cpModelAttrJson = new JSONObject(new LinkedHashMap<>());
            cpModelAttrJson.put("length", cpAttributeMetas.get("length").getDefaultValue());
            cpModelAttrJson.put("weight", cpAttributeMetas.get("weight").getDefaultValue());
            cpModelAttrJson.put("height", cpAttributeMetas.get("height").getDefaultValue());
            cpModelAttrJson.put("locationInfo", cpAttributeMetas.get("locationInfo").getDefaultValue());
            cpModelAttrJson.put("location_X", cpAttributeMetas.get("location_X").getDefaultValue());
            cpModelAttrJson.put("location_Y", cpAttributeMetas.get("location_Y").getDefaultValue());
            cpModelAttrJson.put("location_Z", cpAttributeMetas.get("location_Z").getDefaultValue());
            cpModelAttrJson.put("angle_X", cpAttributeMetas.get("angle_X").getDefaultValue());
            cpModelAttrJson.put("angle_Y", cpAttributeMetas.get("angle_Y").getDefaultValue());
            cpModelAttrJson.put("angle_Z", cpAttributeMetas.get("angle_Z").getDefaultValue());
            cpJson.put("modelData", cpModelAttrJson);

            JSONObject cpBasicAttrJson = new JSONObject(new LinkedHashMap<>());
            if (cpEntity.getCpEntityType() == CPEntityTypeEnum.equipmentCabinet) {
                cpBasicAttrJson.put("ratedPower", cpAttributeMetas.get("ratedPower").getDefaultValue());
                cpBasicAttrJson.put("engagedPosition", cpAttributeMetas.get("engagedPosition").getDefaultValue());
                cpBasicAttrJson.put("remainingPosition", cpAttributeMetas.get("remainingPosition").getDefaultValue());
            } else if (cpEntity.getCpEntityType() == CPEntityTypeEnum.airConditioner) {
                cpBasicAttrJson.put("ratedPower", cpAttributeMetas.get("ratedPower").getDefaultValue());
                cpBasicAttrJson.put("acRefrigeratingCapacity", cpAttributeMetas.get("acRefrigeratingCapacity").getDefaultValue());
            }
            cpJson.put("basicData", cpBasicAttrJson);

            // CP关联Device
            ConcurrentHashMap<String, CPEntityLinkDeviceMeta> cpLinkDeviceMetas = cpEntity.getCpEntityMeta().getCpLinkDeviceMetas();
            List<JSONObject> deviceList = new ArrayList<>();
            Enumeration<Device> devices = cpEntity.allDevices();
            while (devices.hasMoreElements()) {
                JSONObject devJson = new JSONObject(new LinkedHashMap<>());
                Device device = devices.nextElement();
                // 拼接设备数据
                devJson.put("uuid", device.getUuid());
                devJson.put("name", device.getName());
                devJson.put("deviceType", device.getDeviceType());
                cpLinkDeviceMetas.forEach((devName, linkDeviceMeta) -> {
                    if (linkDeviceMeta.getLinkDeviceUuid().equals(device.getUuid())) {
                        devJson.put("aliasName", linkDeviceMeta.getAliasName());
                    }
                });
                // Device属性
                JSONObject devModelAttrJson = new JSONObject(new LinkedHashMap<>());
                JSONObject devBasicAttrJson = new JSONObject(new LinkedHashMap<>());
                devJson.put("modelData", devModelAttrJson);
                devJson.put("basicData", devBasicAttrJson);

                deviceList.add(devJson);
            }
            deviceList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            cpJson.put("devices", deviceList);
            cpList.add(cpJson);
        }
        cpList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpsJson.put("cpEntities", cpList);

        System.out.println(System.currentTimeMillis() - inTime);
        System.out.println(cpsJson.toJSONString());
    }

    /**
     * 获取实时数据信息
     * {
     * "uuid":"1",
     * "name":"铜川智能控制机房",
     * "attributes":{"ecRatedPower":"","setMaxTemperature":"27","AC2RunningState":"","AC1RunningState":"","AC3RunningState":"","AC4RunningState":"","ecGrossPower":"71926.39","AC7RunningState":"","AC5RunningState":"","AC6RunningState":"","height":"3000","currentTemperature":"27.99","AC8RunningState":"","weight":"16200","AC9RunningState":"","currentHumidity":"61.76","setMinHumidity":"40","setMinTemperature":"18","acGrossPower":"9701.06","length":"21500","acRatedPower":"","setMaxHumidity":"60"},
     * "affairs":{"EC25temperatureAlarm":"","EC8powerCollectorRunAlarm":"","EC21powerCollectorRunAlarm":"","EC21thSensorRunAlarm":"","EC24thSensorRunAlarm":"","EC27thSensorRunAlarm":"","AC3acControlRunAlarm":"","EC11humidityAlarm":"","EC31temperatureAlarm":"","AC9powerCollectorRunAlarm":"","EC16temperatureAlarm":"","EC19temperatureAlarm":"","EC34powerCollectorRunAlarm":"","EC34temperatureAlarm":"","EC22temperatureAlarm":"","EC37temperatureAlarm":"","EC28temperatureAlarm":"","EC30thSensorRunAlarm":"","EC4powerCollectorRunAlarm":"","EC25powerCollectorRunAlarm":"","EC38powerCollectorRunAlarm":"","EC33thSensorRunAlarm":"","EC36thSensorRunAlarm":"","EC39thSensorRunAlarm":"","EC10humidityAlarm":"","EC18thSensorRunAlarm":"","EC15thSensorRunAlarm":"","EC12thSensorRunAlarm":"","EC34humidityAlarm":"","EC22humidityAlarm":"","EC16powerCollectorRunAlarm":"","EC12humidityAlarm":"","EC13humidityAlarm":"","AC6acControlRunAlarm":"","EC10temperatureAlarm":"","EC13temperatureAlarm":"","EC4temperatureAlarm":"","EC23humidityAlarm":"","EC1temperatureAlarm":"","EC7temperatureAlarm":"","EC33humidityAlarm":"","EC12powerCollectorRunAlarm":"","EC39powerCollectorRunAlarm":"","EC37humidityAlarm":"","EC30humidityAlarm":"","EC39temperatureAlarm":"","EC3temperatureAlarm":"","EC1humidityAlarm":"","EC2humidityAlarm":"","EC16thSensorRunAlarm":"","EC13thSensorRunAlarm":"","EC9temperatureAlarm":"","EC10thSensorRunAlarm":"","EC19thSensorRunAlarm":"","EC6temperatureAlarm":"","AC8powerCollectorRunAlarm":"","EC13powerCollectorRunAlarm":"","EC27temperatureAlarm":"","EC9humidityAlarm":"","EC36temperatureAlarm":"","EC30temperatureAlarm":"","EC33temperatureAlarm":"","EC24temperatureAlarm":"","EC21temperatureAlarm":"","EC31powerCollectorRunAlarm":"","EC9powerCollectorRunAlarm":"","EC15powerCollectorRunAlarm":"","EC20humidityAlarm":"","EC38humidityAlarm":"","EC29powerCollectorRunAlarm":"","AC2acControlRunAlarm":"","EC28humidityAlarm":"","EC32humidityAlarm":"","EC3humidityAlarm":"","EC6thSensorRunAlarm":"","EC9thSensorRunAlarm":"","EC3thSensorRunAlarm":"","EC3powerCollectorRunAlarm":"","AC8acControlRunAlarm":"","EC31humidityAlarm":"","AC1powerCollectorRunAlarm":"","EC29humidityAlarm":"","EC37powerCollectorRunAlarm":"","AC5acControlRunAlarm":"","EC21humidityAlarm":"","EC14powerCollectorRunAlarm":"","EC39humidityAlarm":"","EC22powerCollectorRunAlarm":"","EC30powerCollectorRunAlarm":"","EC32powerCollectorRunAlarm":"","EC6humidityAlarm":"","EC6powerCollectorRunAlarm":"","EC19humidityAlarm":"","EC18humidityAlarm":"","EC10powerCollectorRunAlarm":"","EC36powerCollectorRunAlarm":"","EC5humidityAlarm":"","EC7humidityAlarm":"","EC2powerCollectorRunAlarm":"","AC7acControlRunAlarm":"","AC2powerCollectorRunAlarm":"","EC23powerCollectorRunAlarm":"","EC12temperatureAlarm":"","EC15temperatureAlarm":"","EC18temperatureAlarm":"","EC28powerCollectorRunAlarm":"","EC8humidityAlarm":"","EC4humidityAlarm":"","EC37thSensorRunAlarm":"","EC34thSensorRunAlarm":"","EC31thSensorRunAlarm":"","EC16humidityAlarm":"","EC17humidityAlarm":"","EC28thSensorRunAlarm":"","AC4acControlRunAlarm":"","EC25thSensorRunAlarm":"","EC19powerCollectorRunAlarm":"","EC27powerCollectorRunAlarm":"","AC9acControlRunAlarm":"","EC22thSensorRunAlarm":"","EC24powerCollectorRunAlarm":"","EC27humidityAlarm":"","AC7powerCollectorRunAlarm":"","EC4thSensorRunAlarm":"","EC1powerCollectorRunAlarm":"","EC7thSensorRunAlarm":"","EC1thSensorRunAlarm":"","AC3powerCollectorRunAlarm":"","EC33powerCollectorRunAlarm":"","EC35thSensorRunAlarm":"","EC38thSensorRunAlarm":"","EC26humidityAlarm":"","EC7powerCollectorRunAlarm":"","EC5thSensorRunAlarm":"","EC20powerCollectorRunAlarm":"","EC14temperatureAlarm":"","EC2thSensorRunAlarm":"","EC11temperatureAlarm":"","EC8thSensorRunAlarm":"","EC35powerCollectorRunAlarm":"","EC15humidityAlarm":"","EC14humidityAlarm":"","EC11powerCollectorRunAlarm":"","AC1acControlRunAlarm":"","EC5powerCollectorRunAlarm":"","AC6powerCollectorRunAlarm":"","EC36humidityAlarm":"","AC4powerCollectorRunAlarm":"","EC11thSensorRunAlarm":"","EC14thSensorRunAlarm":"","EC32temperatureAlarm":"","EC24humidityAlarm":"","EC35temperatureAlarm":"","EC5temperatureAlarm":"","EC8temperatureAlarm":"","EC17thSensorRunAlarm":"","EC2temperatureAlarm":"","EC17powerCollectorRunAlarm":"","EC26powerCollectorRunAlarm":"","EC18powerCollectorRunAlarm":"","EC26thSensorRunAlarm":"","EC23thSensorRunAlarm":"","EC20thSensorRunAlarm":"","EC25humidityAlarm":"","EC29thSensorRunAlarm":"","EC20temperatureAlarm":"","EC23temperatureAlarm":"","EC26temperatureAlarm":"","EC17temperatureAlarm":"","EC29temperatureAlarm":"","EC38temperatureAlarm":"","EC32thSensorRunAlarm":"","AC5powerCollectorRunAlarm":"","EC35humidityAlarm":""},
     * "cpEntities":[{"uuid":"1","name":"机柜1","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1800.71","remainingPosition":"20","temperature":"22.7","humidity":"56.77","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"1","name":"DEV_EC_1","deviceType":"powerCollector","attributes":{"actualPower":"1800.71","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"58","name":"DEV_THSS_1","deviceType":"thSensor","attributes":{"currentTemperature":"22.7","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"56.77","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"2","name":"机柜2","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"15","length":"1200","weight":"600","actualPower":"1941.57","remainingPosition":"20","temperature":"24.29","humidity":"61.76","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:49:59湿度超过上限60%，当前温度61.76%"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"2","name":"DEV_EC_2","deviceType":"powerCollector","attributes":{"actualPower":"1941.57","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"59","name":"DEV_THSS_2","deviceType":"thSensor","attributes":{"currentTemperature":"24.29","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"61.76","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 16:49:59湿度超过上限60%，当前温度61.76%"},"runAlarm":""}}]},{"uuid":"3","name":"机柜3","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1989.26","remainingPosition":"20","temperature":"27.52","humidity":"61.11","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:00温度超过上限27℃，当前温度27.52℃"},"thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:00湿度超过上限60%，当前温度61.11%"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"3","name":"DEV_EC_3","deviceType":"powerCollector","attributes":{"actualPower":"1989.26","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"60","name":"DEV_THSS_3","deviceType":"thSensor","attributes":{"currentTemperature":"27.52","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"61.11","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:00温度超过上限27℃，当前温度27.52℃"},"humAlarm":{"alarmMsg":"在2022-04-29 16:50:00湿度超过上限60%，当前温度61.11%"},"runAlarm":""}}]},{"uuid":"4","name":"机柜4","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"14","length":"1200","weight":"600","actualPower":"1867.42","remainingPosition":"20","temperature":"26.37","humidity":"44.81","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 13:26:29温度超过上限27℃，当前温度27.07℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"4","name":"DEV_EC_4","deviceType":"powerCollector","attributes":{"actualPower":"1867.42","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"61","name":"DEV_THSS_4","deviceType":"thSensor","attributes":{"currentTemperature":"26.37","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"44.81","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 13:26:29温度超过上限27℃，当前温度27.07℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"5","name":"机柜5","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"18","length":"1200","weight":"600","actualPower":"1758.77","remainingPosition":"20","temperature":"27.99","humidity":"50.36","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:02温度超过上限27℃，当前温度27.99℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"5","name":"DEV_EC_5","deviceType":"powerCollector","attributes":{"actualPower":"1758.77","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"62","name":"DEV_THSS_5","deviceType":"thSensor","attributes":{"currentTemperature":"27.99","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"50.36","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:02温度超过上限27℃，当前温度27.99℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"6","name":"机柜6","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"17","length":"1200","weight":"600","actualPower":"1853.81","remainingPosition":"20","temperature":"26.75","humidity":"59.78","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"6","name":"DEV_EC_6","deviceType":"powerCollector","attributes":{"actualPower":"1853.81","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"63","name":"DEV_THSS_6","deviceType":"thSensor","attributes":{"currentTemperature":"26.75","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"59.78","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"7","name":"机柜7","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"9","length":"1200","weight":"600","actualPower":"1773.62","remainingPosition":"20","temperature":"26.04","humidity":"61.76","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":{"alarmMsg":"在2022-04-29 16:50:03温湿度传感器运行异常：采集数据异常！"},"humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:03湿度超过上限60%，当前温度61.76%"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"7","name":"DEV_EC_7","deviceType":"powerCollector","attributes":{"actualPower":"1773.62","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"64","name":"DEV_THSS_7","deviceType":"thSensor","attributes":{"currentTemperature":"26.04","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"61.76","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 16:50:03湿度超过上限60%，当前温度61.76%"},"runAlarm":{"alarmMsg":"在2022-04-29 16:50:03温湿度传感器运行异常：采集数据异常！"}}}]},{"uuid":"8","name":"机柜8","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"10","length":"1200","weight":"600","actualPower":"1825.62","remainingPosition":"20","temperature":"26.25","humidity":"38.02","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:04湿度超过下限40%，当前温度38.02湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"8","name":"DEV_EC_8","deviceType":"powerCollector","attributes":{"actualPower":"1825.62","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"65","name":"DEV_THSS_8","deviceType":"thSensor","attributes":{"currentTemperature":"26.25","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"38.02","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 16:50:04湿度超过下限40%，当前温度38.02湿度"},"runAlarm":""}}]},{"uuid":"9","name":"机柜9","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1712.8","remainingPosition":"20","temperature":"23.87","humidity":"56.96","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":{"alarmMsg":"在2022-04-29 16:50:43功耗采集器运行异常：功率信息无法获取！"}},"devices":[{"uuid":"9","name":"DEV_EC_9","deviceType":"powerCollector","attributes":{"actualPower":"1712.8","onLineState":"on"},"affairs":{"runAlarm":{"alarmMsg":"在2022-04-29 16:50:43功耗采集器运行异常：功率信息无法获取！"}}},{"uuid":"66","name":"DEV_THSS_9","deviceType":"thSensor","attributes":{"currentTemperature":"23.87","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"56.96","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"10","name":"机柜10","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1890.63","remainingPosition":"20","temperature":"19.44","humidity":"43.98","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"10","name":"DEV_EC_10","deviceType":"powerCollector","attributes":{"actualPower":"1890.63","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"67","name":"DEV_THSS_10","deviceType":"thSensor","attributes":{"currentTemperature":"19.44","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"43.98","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"11","name":"机柜11","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1783.94","remainingPosition":"20","temperature":"26.96","humidity":"45.66","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"11","name":"DEV_EC_11","deviceType":"powerCollector","attributes":{"actualPower":"1783.94","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"68","name":"DEV_THSS_11","deviceType":"thSensor","attributes":{"currentTemperature":"26.96","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"45.66","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"12","name":"机柜12","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1934.46","remainingPosition":"20","temperature":"20.08","humidity":"45.98","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"12","name":"DEV_EC_12","deviceType":"powerCollector","attributes":{"actualPower":"1934.46","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"69","name":"DEV_THSS_12","deviceType":"thSensor","attributes":{"currentTemperature":"20.08","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"45.98","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"13","name":"机柜13","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1737.67","remainingPosition":"20","temperature":"21.34","humidity":"55.34","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"13","name":"DEV_EC_13","deviceType":"powerCollector","attributes":{"actualPower":"1737.67","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"70","name":"DEV_THSS_13","deviceType":"thSensor","attributes":{"currentTemperature":"21.34","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"55.34","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"14","name":"机柜14","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1794.96","remainingPosition":"20","temperature":"24.35","humidity":"53.89","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"14","name":"DEV_EC_14","deviceType":"powerCollector","attributes":{"actualPower":"1794.96","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"71","name":"DEV_THSS_14","deviceType":"thSensor","attributes":{"currentTemperature":"24.35","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"53.89","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"15","name":"机柜15","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1727.24","remainingPosition":"20","temperature":"20.32","humidity":"58.33","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"15","name":"DEV_EC_15","deviceType":"powerCollector","attributes":{"actualPower":"1727.24","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"72","name":"DEV_THSS_15","deviceType":"thSensor","attributes":{"currentTemperature":"20.32","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"58.33","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"16","name":"机柜16","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1896.23","remainingPosition":"20","temperature":"21.92","humidity":"53.77","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过上限27℃，当前温度27.96℃"},"thSensorRunAlarm":{"alarmMsg":"在2022-04-29 16:50:09温湿度传感器运行异常：采集数据异常！"},"humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度39.37湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"16","name":"DEV_EC_16","deviceType":"powerCollector","attributes":{"actualPower":"1896.23","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"73","name":"DEV_THSS_16","deviceType":"thSensor","attributes":{"currentTemperature":"21.92","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"53.77","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过上限27℃，当前温度27.96℃"},"humAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度39.37湿度"},"runAlarm":{"alarmMsg":"在2022-04-29 16:50:09温湿度传感器运行异常：采集数据异常！"}}}]},{"uuid":"17","name":"机柜17","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"16","length":"1200","weight":"600","actualPower":"1790.1","remainingPosition":"20","temperature":"26.09","humidity":"42.23","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":{"alarmMsg":"在2022-04-29 16:50:10温湿度传感器运行异常：采集数据异常！"},"humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度38.65湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"17","name":"DEV_EC_17","deviceType":"powerCollector","attributes":{"actualPower":"1790.1","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"74","name":"DEV_THSS_17","deviceType":"thSensor","attributes":{"currentTemperature":"26.09","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"42.23","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度38.65湿度"},"runAlarm":{"alarmMsg":"在2022-04-29 16:50:10温湿度传感器运行异常：采集数据异常！"}}}]},{"uuid":"18","name":"机柜18","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"16","length":"1200","weight":"600","actualPower":"1723.98","remainingPosition":"20","temperature":"27.98","humidity":"58.1","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:11温度超过上限27℃，当前温度27.98℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"18","name":"DEV_EC_18","deviceType":"powerCollector","attributes":{"actualPower":"1723.98","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"75","name":"DEV_THSS_18","deviceType":"thSensor","attributes":{"currentTemperature":"27.98","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"58.1","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:11温度超过上限27℃，当前温度27.98℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"19","name":"机柜19","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"17","length":"1200","weight":"600","actualPower":"1760.28","remainingPosition":"20","temperature":"26.93","humidity":"54.04","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过上限60%，当前温度60.47%"},"powerCollectorRunAlarm":{"alarmMsg":"在2022-04-29 16:50:50功耗采集器运行异常：功率信息无法获取！"}},"devices":[{"uuid":"19","name":"DEV_EC_19","deviceType":"powerCollector","attributes":{"actualPower":"1760.28","onLineState":"on"},"affairs":{"runAlarm":{"alarmMsg":"在2022-04-29 16:50:50功耗采集器运行异常：功率信息无法获取！"}}},{"uuid":"76","name":"DEV_THSS_19","deviceType":"thSensor","attributes":{"currentTemperature":"26.93","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"54.04","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过上限60%，当前温度60.47%"},"runAlarm":""}}]},{"uuid":"20","name":"机柜20","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"17","length":"1200","weight":"600","actualPower":"1820.63","remainingPosition":"20","temperature":"19.36","humidity":"51.43","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"20","name":"DEV_EC_20","deviceType":"powerCollector","attributes":{"actualPower":"1820.63","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"77","name":"DEV_THSS_20","deviceType":"thSensor","attributes":{"currentTemperature":"19.36","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"51.43","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"21","name":"机柜21","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1739.73","remainingPosition":"20","temperature":"17.49","humidity":"52.8","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:14温度超过下限18℃，当前温度17.49℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"21","name":"DEV_EC_21","deviceType":"powerCollector","attributes":{"actualPower":"1739.73","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"78","name":"DEV_THSS_21","deviceType":"thSensor","attributes":{"currentTemperature":"17.49","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"52.8","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:14温度超过下限18℃，当前温度17.49℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"22","name":"机柜22","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"12","length":"1200","weight":"600","actualPower":"1846.08","remainingPosition":"20","temperature":"22.36","humidity":"45.7","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度39.22湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"22","name":"DEV_EC_22","deviceType":"powerCollector","attributes":{"actualPower":"1846.08","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"79","name":"DEV_THSS_22","deviceType":"thSensor","attributes":{"currentTemperature":"22.36","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"45.7","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 13:26:30湿度超过下限40%，当前温度39.22湿度"},"runAlarm":""}}]},{"uuid":"23","name":"机柜23","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"15","length":"1200","weight":"600","actualPower":"1798.06","remainingPosition":"20","temperature":"25.2","humidity":"59.06","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"23","name":"DEV_EC_23","deviceType":"powerCollector","attributes":{"actualPower":"1798.06","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"80","name":"DEV_THSS_23","deviceType":"thSensor","attributes":{"currentTemperature":"25.2","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"59.06","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"24","name":"机柜24","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"18","length":"1200","weight":"600","actualPower":"1806.92","remainingPosition":"20","temperature":"20.9","humidity":"41.86","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"24","name":"DEV_EC_24","deviceType":"powerCollector","attributes":{"actualPower":"1806.92","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"81","name":"DEV_THSS_24","deviceType":"thSensor","attributes":{"currentTemperature":"20.9","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"41.86","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"25","name":"机柜25","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1903.09","remainingPosition":"20","temperature":"20.43","humidity":"39.89","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:18湿度超过下限40%，当前温度39.89湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"25","name":"DEV_EC_25","deviceType":"powerCollector","attributes":{"actualPower":"1903.09","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"82","name":"DEV_THSS_25","deviceType":"thSensor","attributes":{"currentTemperature":"20.43","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"39.89","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 16:50:18湿度超过下限40%，当前温度39.89湿度"},"runAlarm":""}}]},{"uuid":"26","name":"机柜26","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1838.11","remainingPosition":"20","temperature":"21.21","humidity":"58.39","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"26","name":"DEV_EC_26","deviceType":"powerCollector","attributes":{"actualPower":"1838.11","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"83","name":"DEV_THSS_26","deviceType":"thSensor","attributes":{"currentTemperature":"21.21","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"58.39","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"27","name":"机柜27","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"11","length":"1200","weight":"600","actualPower":"1979.75","remainingPosition":"20","temperature":"19.12","humidity":"54.81","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"27","name":"DEV_EC_27","deviceType":"powerCollector","attributes":{"actualPower":"1979.75","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"84","name":"DEV_THSS_27","deviceType":"thSensor","attributes":{"currentTemperature":"19.12","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"54.81","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"28","name":"机柜28","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"17","length":"1200","weight":"600","actualPower":"1955.33","remainingPosition":"20","temperature":"25.67","humidity":"43.5","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过下限18℃，当前温度17.11℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"28","name":"DEV_EC_28","deviceType":"powerCollector","attributes":{"actualPower":"1955.33","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"85","name":"DEV_THSS_28","deviceType":"thSensor","attributes":{"currentTemperature":"25.67","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"43.5","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过下限18℃，当前温度17.11℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"29","name":"机柜29","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1705.38","remainingPosition":"20","temperature":"26.31","humidity":"43.98","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过上限27℃，当前温度27.43℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"29","name":"DEV_EC_29","deviceType":"powerCollector","attributes":{"actualPower":"1705.38","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"86","name":"DEV_THSS_29","deviceType":"thSensor","attributes":{"currentTemperature":"26.31","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"43.98","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 13:26:30温度超过上限27℃，当前温度27.43℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"30","name":"机柜30","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"13","length":"1200","weight":"600","actualPower":"1976.66","remainingPosition":"20","temperature":"23.05","humidity":"44.8","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 13:26:31温度超过下限18℃，当前温度17.97℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"30","name":"DEV_EC_30","deviceType":"powerCollector","attributes":{"actualPower":"1976.66","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"87","name":"DEV_THSS_30","deviceType":"thSensor","attributes":{"currentTemperature":"23.05","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"44.8","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 13:26:31温度超过下限18℃，当前温度17.97℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"31","name":"机柜31","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"18","length":"1200","weight":"600","actualPower":"1880.85","remainingPosition":"20","temperature":"27.07","humidity":"40.25","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:23温度超过上限27℃，当前温度27.07℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"31","name":"DEV_EC_31","deviceType":"powerCollector","attributes":{"actualPower":"1880.85","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"88","name":"DEV_THSS_31","deviceType":"thSensor","attributes":{"currentTemperature":"27.07","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"40.25","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:23温度超过上限27℃，当前温度27.07℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"32","name":"机柜32","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"16","length":"1200","weight":"600","actualPower":"1977.62","remainingPosition":"20","temperature":"20.14","humidity":"44.27","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"32","name":"DEV_EC_32","deviceType":"powerCollector","attributes":{"actualPower":"1977.62","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"89","name":"DEV_THSS_32","deviceType":"thSensor","attributes":{"currentTemperature":"20.14","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"44.27","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"33","name":"机柜33","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1806.07","remainingPosition":"20","temperature":"17.42","humidity":"60.13","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:24温度超过下限18℃，当前温度17.42℃"},"thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:24湿度超过上限60%，当前温度60.13%"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"33","name":"DEV_EC_33","deviceType":"powerCollector","attributes":{"actualPower":"1806.07","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"90","name":"DEV_THSS_33","deviceType":"thSensor","attributes":{"currentTemperature":"17.42","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"60.13","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:24温度超过下限18℃，当前温度17.42℃"},"humAlarm":{"alarmMsg":"在2022-04-29 16:50:24湿度超过上限60%，当前温度60.13%"},"runAlarm":""}}]},{"uuid":"34","name":"机柜34","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1946.9","remainingPosition":"20","temperature":"27.3","humidity":"48.73","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:24温度超过上限27℃，当前温度27.3℃"},"thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:31湿度超过下限40%，当前温度38.74湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"34","name":"DEV_EC_34","deviceType":"powerCollector","attributes":{"actualPower":"1946.9","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"91","name":"DEV_THSS_34","deviceType":"thSensor","attributes":{"currentTemperature":"27.3","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"48.73","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:24温度超过上限27℃，当前温度27.3℃"},"humAlarm":{"alarmMsg":"在2022-04-29 13:26:31湿度超过下限40%，当前温度38.74湿度"},"runAlarm":""}}]},{"uuid":"35","name":"机柜35","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"20","length":"1200","weight":"600","actualPower":"1976.03","remainingPosition":"20","temperature":"27.1","humidity":"42.35","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:25温度超过上限27℃，当前温度27.1℃"},"thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"35","name":"DEV_EC_35","deviceType":"powerCollector","attributes":{"actualPower":"1976.03","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"92","name":"DEV_THSS_35","deviceType":"thSensor","attributes":{"currentTemperature":"27.1","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"42.35","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:25温度超过上限27℃，当前温度27.1℃"},"humAlarm":"","runAlarm":""}}]},{"uuid":"36","name":"机柜36","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1998.23","remainingPosition":"20","temperature":"21.69","humidity":"61.48","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 16:50:27湿度超过上限60%，当前温度61.48%"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"36","name":"DEV_EC_36","deviceType":"powerCollector","attributes":{"actualPower":"1998.23","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"93","name":"DEV_THSS_36","deviceType":"thSensor","attributes":{"currentTemperature":"21.69","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"61.48","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":{"alarmMsg":"在2022-04-29 16:50:27湿度超过上限60%，当前温度61.48%"},"runAlarm":""}}]},{"uuid":"37","name":"机柜37","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1819.97","remainingPosition":"20","temperature":"21.26","humidity":"45.83","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"37","name":"DEV_EC_37","deviceType":"powerCollector","attributes":{"actualPower":"1819.97","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"94","name":"DEV_THSS_37","deviceType":"thSensor","attributes":{"currentTemperature":"21.26","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"45.83","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"38","name":"机柜38","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"16","length":"1200","weight":"600","actualPower":"1880.38","remainingPosition":"20","temperature":"25.56","humidity":"48.84","height":"2000"},"affairs":{"temperatureAlarm":"","thSensorRunAlarm":"","humidityAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"38","name":"DEV_EC_38","deviceType":"powerCollector","attributes":{"actualPower":"1880.38","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"95","name":"DEV_THSS_38","deviceType":"thSensor","attributes":{"currentTemperature":"25.56","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"48.84","miniHumSet":""},"affairs":{"tempAlarm":"","humAlarm":"","runAlarm":""}}]},{"uuid":"39","name":"机柜39","cpEntityType":"equipmentCabinet","attributes":{"ratedPower":"2000","engagedPosition":"19","length":"1200","weight":"600","actualPower":"1707.53","remainingPosition":"20","temperature":"17.58","humidity":"41.87","height":"2000"},"affairs":{"temperatureAlarm":{"alarmMsg":"在2022-04-29 16:50:30温度超过下限18℃，当前温度17.58℃"},"thSensorRunAlarm":"","humidityAlarm":{"alarmMsg":"在2022-04-29 13:26:31湿度超过下限40%，当前温度39.49湿度"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"39","name":"DEV_EC_39","deviceType":"powerCollector","attributes":{"actualPower":"1707.53","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"96","name":"DEV_THSS_39","deviceType":"thSensor","attributes":{"currentTemperature":"17.58","maxHumSet":"","miniTempSet":"","onLineState":"on","maxTempSet":"","currentHumidity":"41.87","miniHumSet":""},"affairs":{"tempAlarm":{"alarmMsg":"在2022-04-29 16:50:30温度超过下限18℃，当前温度17.58℃"},"humAlarm":{"alarmMsg":"在2022-04-29 13:26:31湿度超过下限40%，当前温度39.49湿度"},"runAlarm":""}}]},{"uuid":"40","name":"行级空调1","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1355.94","targetTemperature":"19.57","airVolume":"1716.06","height":"2000","targetHumidity":"40.42","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"26.26","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"60.15"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"40","name":"DEV_EC_40","deviceType":"powerCollector","attributes":{"actualPower":"1355.94","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"49","name":"DEV_ACC_1","deviceType":"acControl","attributes":{"targetHumidity":"40.42","currentTemperature":"26.26","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.57","currentHumidity":"60.15","airVolume":"1716.06"},"affairs":{"runAlarm":""}}]},{"uuid":"41","name":"行级空调2","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1052.19","targetTemperature":"19.13","airVolume":"1149.13","height":"2000","targetHumidity":"40.12","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"20.36","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"56.45"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"41","name":"DEV_EC_41","deviceType":"powerCollector","attributes":{"actualPower":"1052.19","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"50","name":"DEV_ACC_2","deviceType":"acControl","attributes":{"targetHumidity":"40.12","currentTemperature":"20.36","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.13","currentHumidity":"56.45","airVolume":"1149.13"},"affairs":{"runAlarm":""}}]},{"uuid":"42","name":"行级空调3","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1238.16","targetTemperature":"18.25","airVolume":"1105.08","height":"2000","targetHumidity":"44.44","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"27.74","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"57.16"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"42","name":"DEV_EC_42","deviceType":"powerCollector","attributes":{"actualPower":"1238.16","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"51","name":"DEV_ACC_3","deviceType":"acControl","attributes":{"targetHumidity":"44.44","currentTemperature":"27.74","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"18.25","currentHumidity":"57.16","airVolume":"1105.08"},"affairs":{"runAlarm":""}}]},{"uuid":"43","name":"行级空调4","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1125.03","targetTemperature":"19.76","airVolume":"1556.56","height":"2000","targetHumidity":"41.41","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"27.96","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"43.93"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"43","name":"DEV_EC_43","deviceType":"powerCollector","attributes":{"actualPower":"1125.03","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"52","name":"DEV_ACC_4","deviceType":"acControl","attributes":{"targetHumidity":"41.41","currentTemperature":"27.96","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.76","currentHumidity":"43.93","airVolume":"1556.56"},"affairs":{"runAlarm":""}}]},{"uuid":"44","name":"行级空调5","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1333.44","targetTemperature":"19.64","airVolume":"1402.93","height":"2000","targetHumidity":"43.56","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"24.09","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"52.35"},"affairs":{"acControlRunAlarm":{"alarmMsg":"在2022-04-29 16:50:35空调控制器运行异常：工作模式信息无法获取！"},"powerCollectorRunAlarm":""},"devices":[{"uuid":"44","name":"DEV_EC_44","deviceType":"powerCollector","attributes":{"actualPower":"1333.44","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"53","name":"DEV_ACC_5","deviceType":"acControl","attributes":{"targetHumidity":"43.56","currentTemperature":"24.09","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.64","currentHumidity":"52.35","airVolume":"1402.93"},"affairs":{"runAlarm":{"alarmMsg":"在2022-04-29 16:50:35空调控制器运行异常：工作模式信息无法获取！"}}}]},{"uuid":"45","name":"行级空调6","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1230.76","targetTemperature":"19.35","airVolume":"1286.08","height":"2000","targetHumidity":"41.36","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"22.6","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"61.43"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"45","name":"DEV_EC_45","deviceType":"powerCollector","attributes":{"actualPower":"1230.76","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"54","name":"DEV_ACC_6","deviceType":"acControl","attributes":{"targetHumidity":"41.36","currentTemperature":"22.6","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.35","currentHumidity":"61.43","airVolume":"1286.08"},"affairs":{"runAlarm":""}}]},{"uuid":"46","name":"行级空调7","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1004.1","targetTemperature":"19.17","airVolume":"1876.56","height":"2000","targetHumidity":"40.47","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"18.25","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"55.1"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":{"alarmMsg":"在2022-04-29 16:51:08功耗采集器运行异常：功率信息无法获取！"}},"devices":[{"uuid":"46","name":"DEV_EC_46","deviceType":"powerCollector","attributes":{"actualPower":"1004.1","onLineState":"on"},"affairs":{"runAlarm":{"alarmMsg":"在2022-04-29 16:51:08功耗采集器运行异常：功率信息无法获取！"}}},{"uuid":"55","name":"DEV_ACC_7","deviceType":"acControl","attributes":{"targetHumidity":"40.47","currentTemperature":"18.25","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"19.17","currentHumidity":"55.1","airVolume":"1876.56"},"affairs":{"runAlarm":""}}]},{"uuid":"47","name":"行级空调8","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"1361.44","targetTemperature":"18.03","airVolume":"1222.32","height":"2000","targetHumidity":"40.26","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"27.4","length":"1200","weight":"600","powerSwitch":"true","currentHumidity":"55.56"},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"47","name":"DEV_EC_47","deviceType":"powerCollector","attributes":{"actualPower":"1361.44","onLineState":"on"},"affairs":{"runAlarm":""}},{"uuid":"56","name":"DEV_ACC_8","deviceType":"acControl","attributes":{"targetHumidity":"40.26","currentTemperature":"27.4","workMode":"","powerSwitch":"true","onLineState":"on","targetTemperature":"18.03","currentHumidity":"55.56","airVolume":"1222.32"},"affairs":{"runAlarm":""}}]},{"uuid":"48","name":"行级空调9","cpEntityType":"airConditioner","attributes":{"workMode":"","actualPower":"","targetTemperature":"","airVolume":"","height":"2000","targetHumidity":"","acRefrigeratingCapacity":"26000","ratedPower":"14000","currentTemperature":"","length":"1200","weight":"600","powerSwitch":"","currentHumidity":""},"affairs":{"acControlRunAlarm":"","powerCollectorRunAlarm":""},"devices":[{"uuid":"48","name":"DEV_EC_48","deviceType":"powerCollector","attributes":{"actualPower":"","onLineState":""},"affairs":{"runAlarm":""}},{"uuid":"57","name":"DEV_ACC_9","deviceType":"acControl","attributes":{"targetHumidity":"","currentTemperature":"","workMode":"","powerSwitch":"","onLineState":"","targetTemperature":"","currentHumidity":"","airVolume":""},"affairs":{"runAlarm":""}}]}]}
     */
    @Test
    public void getRuntimeData() throws CPSInstanceException {
        String host = "172.16.20.64";
        Integer port = 6379;
        String pwd = "future123$%^";
        CPSInstance cpsInstance = cpsInstanceFactory.buildCPSInstance("1");
        RedisCPSInstance redisCPSInstance = new RedisCPSInstance(cpsInstance.getCpsInstanceMeta(), host, port, pwd);
        JSONObject cpsJson = new JSONObject(new LinkedHashMap<>());
        cpsJson.put("uuid", cpsInstance.getUuid());
        cpsJson.put("name", cpsInstance.getName());
        ConcurrentHashMap<String, CPSInstanceAttributeMeta> cpsAttributeMetas = redisCPSInstance.getCpsInstanceMeta().getCpsAttributeMetas();
        JSONObject cpsAttrJson = new JSONObject(new LinkedHashMap<>());
        cpsAttributeMetas.forEach((cpsAttrName, cpsAttrMeta) -> {
            try {
                String attrVal = redisCPSInstance.getAttributeByName(cpsAttrName);
                cpsAttrJson.put(cpsAttrName, attrVal);
            } catch (UnsupportedAttributeNameException e) {
                e.printStackTrace();
            }
        });
        cpsJson.put("attributes", cpsAttrJson);
        ConcurrentHashMap<String, CPSInstanceAffairMeta> cpsAffairMetas = redisCPSInstance.getCpsInstanceMeta().getCpsAffairMetas();
        JSONObject cpsAffJson = new JSONObject(new LinkedHashMap<>());
        cpsAffairMetas.forEach((cpsAffName, cpsAffMeta) -> {
            try {
                String cpsAffVal = redisCPSInstance.getAffairByName(cpsAffName);
                cpsAffJson.put(cpsAffName, StringUtils.isNotBlank(cpsAffVal) ? JSONObject.parseObject(cpsAffVal) : "");
            } catch (UnsupportedAffairNameException e) {
                e.printStackTrace();
            }
        });
        cpsJson.put("affairs", cpsAffJson);

        Enumeration<CPEntity> cpEntities = cpsInstance.allCPEntities();
        List<JSONObject> cpList = new ArrayList<>();
        while (cpEntities.hasMoreElements()) {
            CPEntity cpEntity = cpEntities.nextElement();
            RedisCPEntity redisCPEntity = new RedisCPEntity(cpEntity.getCpEntityMeta(), host, port, pwd);
            JSONObject cpJson = new JSONObject(new LinkedHashMap<>());
            cpJson.put("uuid", cpEntity.getUuid());
            cpJson.put("name", cpEntity.getName());
            cpJson.put("cpEntityType", cpEntity.getCpEntityType());

            ConcurrentHashMap<String, CPEntityAttributeMeta> cpAttributeMetas = cpEntity.getCpEntityMeta().getCpAttributeMetas();
            JSONObject cpAttrJson = new JSONObject(new LinkedHashMap<>());
            cpAttributeMetas.forEach((cpAttrName, cpAttrMeta) -> {
                try {
                    String cpAttrVal = redisCPEntity.getAttributeByName(cpAttrName);
                    cpAttrJson.put(cpAttrName, cpAttrVal);
                } catch (UnsupportedAttributeNameException e) {
                    e.printStackTrace();
                }
            });
            cpJson.put("attributes", cpAttrJson);
            ConcurrentHashMap<String, CPEntityAffairMeta> cpAffairMetas = cpEntity.getCpEntityMeta().getCpAffairMetas();
            JSONObject cpAffJson = new JSONObject(new LinkedHashMap<>());
            cpAffairMetas.forEach((cpAffName, cpAffMeta) -> {
                try {
                    String cpAffVal = redisCPEntity.getAffairByName(cpAffName);
                    cpAffJson.put(cpAffName, StringUtils.isNotBlank(cpAffVal) ? JSONObject.parseObject(cpAffVal) : "");
                } catch (UnsupportedAffairNameException e) {
                    e.printStackTrace();
                }
            });
            cpJson.put("affairs", cpAffJson);

            Enumeration<Device> devices = cpEntity.allDevices();
            List<JSONObject> devList = new ArrayList<>();
            while (devices.hasMoreElements()) {
                Device device = devices.nextElement();
                DefaultDevice redisDevice = new DefaultDevice(device.getDeviceMeta(), host, port, pwd);
                JSONObject devJson = new JSONObject(new LinkedHashMap<>());
                devJson.put("uuid", device.getUuid());
                devJson.put("name", device.getName());
                devJson.put("deviceType", device.getDeviceType());

                ConcurrentHashMap<String, DeviceAttributeMeta> attributeMetas = device.getDeviceMeta().getAttributeMetas();
                JSONObject devAttrJson = new JSONObject(new LinkedHashMap<>());
                attributeMetas.forEach((devAttrName, devAttrMeta) -> {
                    try {
                        String devAttrVal = redisDevice.getAttributeByName(devAttrName);
                        devAttrJson.put(devAttrName, devAttrVal);
                    } catch (UnsupportedAttributeNameException e) {
                        e.printStackTrace();
                    }
                });
                devJson.put("attributes", devAttrJson);

                ConcurrentHashMap<String, DeviceAffairMeta> affairMetas = device.getDeviceMeta().getAffairMetas();
                JSONObject devAffJson = new JSONObject(new LinkedHashMap<>());
                affairMetas.forEach((devAffName, devAffMeta) -> {
                    try {
                        String devAffVal = redisDevice.getAffairByName(devAffName);
                        devAffJson.put(devAffName, StringUtils.isNotBlank(devAffVal) ? JSONObject.parseObject(devAffVal) : "");
                    } catch (UnsupportedAffairNameException e) {
                        e.printStackTrace();
                    }
                });
                devJson.put("affairs", devAffJson);
                devList.add(devJson);
            }
            devList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
            cpJson.put("devices", devList);
            cpList.add(cpJson);
        }
        cpList.sort(Comparator.comparingInt(x -> x.getInteger("uuid")));
        cpsJson.put("cpEntities", cpList);
        System.out.println(cpsJson.toJSONString());
    }

    @Test
    public void testGetCpsAffairIdListByParam() throws CPSInstanceException {
        String scene = "energyControl";
        String industry = "smartCity";
        String category = "wisdomRoom";
        String cpsUuid = "1";
        String affairName = "";
        List<String> idList = cpsInstanceService.getCpsAffairIdListByParam(scene, industry, category, cpsUuid, affairName);
        System.out.println(idList.toString());
    }
}
