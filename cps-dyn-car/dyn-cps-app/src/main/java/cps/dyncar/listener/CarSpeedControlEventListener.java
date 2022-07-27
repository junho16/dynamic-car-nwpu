//package cps.dyncar.listener;
//
//import cps.api.entity.CPSEventListener;
//import cps.api.entity.UnsupportedActionNameException;
//import cps.api.entity.UnsupportedAttributeNameException;
//import cps.dyncar.entity.DynCarCPSInstance;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 车辆速度控制事件监听
// */
//public class CarSpeedControlEventListener extends CPSEventListener<DynCarCPSInstance> {
//
//
//    private static final Logger logger = LoggerFactory.getLogger(CarSpeedControlEventListener.class);
//
//    ScheduledExecutorService runCarSpeedControlService = Executors.newSingleThreadScheduledExecutor();
//
//    @Override
//    public void beforeStartUp() {
//        super.beforeStartUp();
//        try {
//            // 创建线程定时执行算法
//            Runnable temperatureControlRunnable = () -> {
//                // 机房温度不均衡点出现温度异常
//                // 假设是发现了路口首车的速度不对，需要告警，或者控制
//                try {
//                    speedControl();
//                } catch (UnsupportedAttributeNameException | UnsupportedActionNameException e) {
//                    logger.error("速度控制任务异常：{}", e.getMessage());
//                }
//            };
////            runCarSpeedControlService.scheduleAtFixedRate(temperatureControlRunnable, 0, 10, TimeUnit.MINUTES);
//            runCarSpeedControlService.scheduleAtFixedRate(temperatureControlRunnable, 0, 5, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            logger.error("速度控制算法使用失败：{}", e.getMessage());
//        }
//    }
//
//    @Override
//    public void beforeChange(Event event) {
//        super.beforeChange(event);
//    }
//
//    @Override
//    public void beforeStop() {
//        super.beforeStop();
//        runCarSpeedControlService.shutdownNow();
//    }
//
//    /**
//     * 速度控制-用不到 因为不用控制速度
//     * @throws UnsupportedAttributeNameException
//     * @throws UnsupportedActionNameException
//     */
//    public void speedControl() throws UnsupportedAttributeNameException, UnsupportedActionNameException {
//        logger.info("控制速度...");
//    }
//
////    /**
////     * 机房机柜温度控制，出现异常温湿度进行控制,主要处理关系：机柜温湿度异常由周围的几台空调分被进行调控
////     *
////     * @throws UnsupportedAttributeNameException
////     * @throws UnsupportedActionNameException
////     */
////    public void cabinetTemperatureControl() throws UnsupportedAttributeNameException, UnsupportedActionNameException {
////
////        // 获取存在异常温湿度的机柜和数据
////        List<ConcurrentHashMap<String, String>> ecAbnormalTempAndHumData = getEcAbnormalTempAndHumData();
////        for (ConcurrentHashMap<String, String> abnormalMap : ecAbnormalTempAndHumData) {
////            // 处理异常数据逻辑 获取异常数据集合中的cpName、温度值、湿度值、异常类型
////            String abnormalCpName = abnormalMap.get("cpName");
////            String ecTemperatureVal = abnormalMap.get("temperature");
////            String ecHumidityVal = abnormalMap.get("humidity");
////            String abnormalTypeVal = abnormalMap.get("abnormalType");
////
////            // 获取机柜实体
////            CPEntity cabinetCPEntity = this.entity.getCPEntity(abnormalCpName);
////            // 参与控制空调集合
////            List<CPEntity> acCPEntityList = getAcCpEntityList(abnormalCpName);
////            // 根据异常类型进行异常处理调用
////            if (AbnormalTypeEnum.OVERHEAT == AbnormalTypeEnum.valueOf(abnormalTypeVal) || AbnormalTypeEnum.OVERCOOLING == AbnormalTypeEnum.valueOf(abnormalTypeVal)) {
////                // 过热过冷调用温度处理算法
////                temControlAlgorithm(ecTemperatureVal, AbnormalTypeEnum.valueOf(abnormalTypeVal), cabinetCPEntity, acCPEntityList);
////            } else if (AbnormalTypeEnum.OVERWETTING == AbnormalTypeEnum.valueOf(abnormalTypeVal) || AbnormalTypeEnum.OVERDRY == AbnormalTypeEnum.valueOf(abnormalTypeVal)) {
////                // 过湿过干调用湿度处理算法
////                humidityControlAlgorithm(ecTemperatureVal, AbnormalTypeEnum.valueOf(abnormalTypeVal), acCPEntityList);
////            } else if (AbnormalTypeEnum.OVERHEATWETTING == AbnormalTypeEnum.valueOf(abnormalTypeVal) || AbnormalTypeEnum.OVERHEATDRY == AbnormalTypeEnum.valueOf(abnormalTypeVal)
////                    || AbnormalTypeEnum.OVERCOOLINGWETTING == AbnormalTypeEnum.valueOf(abnormalTypeVal) || AbnormalTypeEnum.OVERCOOLINGDRY == AbnormalTypeEnum.valueOf(abnormalTypeVal)) {
////                // 温湿度都出现异常调用温湿度处理算法
////                temAndHumControlAlgorithm(ecTemperatureVal, ecHumidityVal, AbnormalTypeEnum.valueOf(abnormalTypeVal), cabinetCPEntity, acCPEntityList);
////            } else {
////                throw new UnsupportedActionNameException("不支持的异常类型：" + abnormalTypeVal);
////            }
////        }
////    }
////
////    /**
////     * 获取参与控制的空调实体
////     *
////     * @param cpName cpName
////     * @return 机柜对应的空调实体集合
////     */
////    private List<CPEntity> getAcCpEntityList(String cpName) {
////        List<String> acCpNames = new ArrayList<>();
////        List<CPEntity> acCpList = new ArrayList<>();
////
////        if ("G1_EC_L1".equals(cpName) || "G1_EC_L2".equals(cpName)) {
////            // 左1机柜、左2机柜异常均由空调AC1处理
////            acCpNames.add("G1_AC1");
////        } else if ("G1_EC_R1".equals(cpName) || "G1_EC_R2".equals(cpName) || "G1_EC_R3".equals(cpName) || "G1_EC_R4".equals(cpName)) {
////            // 右1机柜、右2机柜、右3机柜、右4机柜由AC1、AC2处理
////            acCpNames.add("G1_AC1");
////            acCpNames.add("G1_AC2");
////        } else if ("G1_EC_L3".equals(cpName) || "G1_EC_L4".equals(cpName) || "G1_EC_L5".equals(cpName) || "G1_EC_L6".equals(cpName)) {
////            // 左3机柜、左4机柜、左5机柜、左6机柜异常由AC1、AC2、AC3处理
////            acCpNames.add("G1_AC1");
////            acCpNames.add("G1_AC2");
////            acCpNames.add("G1_AC3");
////        } else if ("G1_EC_R5".equals(cpName) || "G1_EC_R6".equals(cpName) || "G1_EC_R7".equals(cpName) || "G1_EC_R8".equals(cpName) || "G1_EC_R9".equals(cpName)) {
////            // 右5机柜、右6机柜、右7机柜、右8机柜、右9机柜由AC2、AC3、AC4处理
////            acCpNames.add("G1_AC2");
////            acCpNames.add("G1_AC3");
////            acCpNames.add("G1_AC4");
////        } else if ("G1_EC_L7".equals(cpName) || "G1_EC_L8".equals(cpName) || "G1_EC_L9".equals(cpName) || "G1_EC_L10".equals(cpName)) {
////            // 左7机柜、左8机柜、左9机柜、左10机柜异常由AC3、AC4、AC5处理
////            acCpNames.add("G1_AC3");
////            acCpNames.add("G1_AC4");
////            acCpNames.add("G1_AC5");
////        } else if ("G1_EC_R10".equals(cpName) || "G1_EC_R11".equals(cpName) || "G1_EC_R12".equals(cpName) || "G1_EC_R13".equals(cpName)) {
////            // 右10机柜、右11机柜、右12机柜、右13机柜异常由AC4、AC5、AC6处理
////            acCpNames.add("G1_AC4");
////            acCpNames.add("G1_AC5");
////            acCpNames.add("G1_AC6");
////        } else if ("G1_EC_L11".equals(cpName) || "G1_EC_L12".equals(cpName) || "G1_EC_L13".equals(cpName) || "G1_EC_L14".equals(cpName)) {
////            // 左11机柜、左12机柜、左13机柜、左14机柜异常由AC5、AC6、AC7处理
////            acCpNames.add("G1_AC5");
////            acCpNames.add("G1_AC6");
////            acCpNames.add("G1_AC7");
////        } else if ("G1_EC_R14".equals(cpName) || "G1_EC_R15".equals(cpName) || "G1_EC_R16".equals(cpName) || "G1_EC_R17".equals(cpName)) {
////            // 右14机柜、右15机柜、右16机柜、右17机柜异常由AC6、AC7、AC8处理
////            acCpNames.add("G1_AC6");
////            acCpNames.add("G1_AC7");
////            acCpNames.add("G1_AC8");
////        } else if ("G1_EC_L15".equals(cpName) || "G1_EC_L16".equals(cpName) || "G1_EC_L17".equals(cpName) || "G1_EC_L18".equals(cpName)) {
////            // 左15机柜、左16机柜、左17机柜、左18机柜异常由AC7、AC8、AC9处理
////            acCpNames.add("G1_AC7");
////            acCpNames.add("G1_AC8");
////            acCpNames.add("G1_AC9");
////        } else if ("G1_EC_R18".equals(cpName) || "G1_EC_R19".equals(cpName) || "G1_EC_R20".equals(cpName) || "G1_EC_L19".equals(cpName)) {
////            // 右18机柜、右19机柜、右20机柜、左19机柜异常均由空调AC8、AC9处理
////            acCpNames.add("G1_AC8");
////            acCpNames.add("G1_AC9");
////        }
////
////        for (String acCpName : acCpNames) {
////            acCpList.add(this.entity.getCPEntity(acCpName));
////        }
////        return acCpList;
////    }
////
////    /**
////     * 温湿度异常类型枚举
////     */
////    private enum AbnormalTypeEnum {
////        /**
////         * 过热
////         */
////        OVERHEAT("过热"),
////        /**
////         * 过冷
////         */
////        OVERCOOLING("过冷"),
////        /**
////         * 过湿
////         */
////        OVERWETTING("过湿"),
////        /**
////         * 过干
////         */
////        OVERDRY("过干"),
////        /**
////         * 过热过湿
////         */
////        OVERHEATWETTING("过热过湿"),
////        /**
////         * 过热过干
////         */
////        OVERHEATDRY("过热过干"),
////        /**
////         * 过冷过湿
////         */
////        OVERCOOLINGWETTING("过冷过湿"),
////        /**
////         * 过冷过干
////         */
////        OVERCOOLINGDRY("过冷过干");
////
////        private String message;
////
////        AbnormalTypeEnum(String msg) {
////            this.message = msg;
////        }
////    }
////
////    /**
////     * 获取CPS实例中的温湿度异常的机柜数据，包含机柜的名称，异常类型，温湿度异常值
////     *
////     * @return
////     * @throws UnsupportedAttributeNameException
////     */
////    public List<ConcurrentHashMap<String, String>> getEcAbnormalTempAndHumData() throws UnsupportedAttributeNameException {
////        // 声明异常数据集合
////        List<ConcurrentHashMap<String, String>> abnormalMapList = new ArrayList<>();
////
////        // 遍历所有关联的CP实体
////        for (CPSInstanceLinkCPEntityMeta cpsInstanceLinkCPEntityMeta : this.entity.getCpsInstanceMeta().getCpsLinkCpMetas().values()) {
////            // 根据cpName获取CP实体
////            CPEntity cpEntity = this.entity.getCPEntity(cpsInstanceLinkCPEntityMeta.getCpName());
////            // 判断机柜温湿度异常数据
////            if (CPEntityTypeEnum.equipmentCabinet == cpEntity.getCpEntityType()) {
////                // 获取机柜温湿度数据
////                String temperatureVal = cpEntity.getAttributeByName("currentTemperature");
////                String humidityVal = cpEntity.getAttributeByName("currentHumidity");
////                ConcurrentHashMap<String, String> abnormalMap = abnormalDataProcess(temperatureVal, humidityVal);
////                if (abnormalMap.size() > 0) {
////                    abnormalMap.put("cpName", cpsInstanceLinkCPEntityMeta.getCpName());
////                    abnormalMap.put("temperature", temperatureVal);
////                    abnormalMap.put("humidity", humidityVal);
////                    abnormalMapList.add(abnormalMap);
////                }
////            }
////
////        }
////        return abnormalMapList;
////    }
////
////    /**
////     * 温湿度数据处理：判断异常类型进行数据封装
////     *
////     * @param temperatureVal 温度key
////     * @param humidityVal    湿度key
////     * @return 异常数据map
////     * @throws UnsupportedAttributeNameException 属性名称不支持异常
////     */
////    public ConcurrentHashMap<String, String> abnormalDataProcess(String temperatureVal, String humidityVal) throws UnsupportedAttributeNameException {
////
////        ConcurrentHashMap<String, String> abnormalMap = new ConcurrentHashMap<>();
////        // 判断温湿度数据
////        if (StringUtils.isNotBlank(temperatureVal) && StringUtils.isNotBlank(humidityVal)) {
////
////            BigDecimal ecTemperatureVal = new BigDecimal(temperatureVal);
////            BigDecimal ecHumidityVal = new BigDecimal(humidityVal);
////            // 封装异常类型数据
////            if (ecTemperatureVal.compareTo(idcSetMaxTemperature) > 0) {
////                if (ecHumidityVal.compareTo(idcSetMaxHumidity) > 0) {
////                    // 采集到的温度大于机房设定最高温度，采集到的湿度大于机房设定最大湿度，异常类型为过热过湿
////                    abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERHEATWETTING.name());
////                } else if (ecHumidityVal.compareTo(idcSetMinHumidity) < 0) {
////                    // 采集到的温度大于机房设定最高温度，采集到的湿度小于机房设定最小湿度，异常类型为过热过干
////                    abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERHEATDRY.name());
////                }
////            } else if (ecTemperatureVal.compareTo(idcSetMinTemperature) < 0) {
////                if (ecHumidityVal.compareTo(idcSetMaxHumidity) > 0) {
////                    // 采集到的温度小于机房设定最低温度，采集到的湿度大于机房设定最大湿度，异常类型为过冷过湿
////                    abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERCOOLINGWETTING.name());
////                } else if (ecHumidityVal.compareTo(idcSetMinHumidity) < 0) {
////                    // 采集到的温度小于机房设定最低温度，采集到的湿度小于机房设定最小湿度，异常类型为过冷过干
////                    abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERCOOLINGDRY.name());
////                }
////            }
////
////        } else if (StringUtils.isNotBlank(temperatureVal)) {
////
////            BigDecimal ecTemperatureVal = new BigDecimal(temperatureVal);
////            if (ecTemperatureVal.compareTo(idcSetMaxTemperature) > 0) {
////                // 采集到的温度大于机房设定最高温度,异常类型为过热
////                abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERHEAT.name());
////            } else if (ecTemperatureVal.compareTo(idcSetMinTemperature) < 0) {
////                // 采集到的温度小于机房设定最低温度,异常类型为过冷
////                abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERCOOLING.name());
////            }
////
////        } else if (StringUtils.isNotBlank(humidityVal)) {
////
////            BigDecimal ecHumidityVal = new BigDecimal(humidityVal);
////            if (ecHumidityVal.compareTo(idcSetMaxHumidity) > 0) {
////                // 采集到的湿度大于机房设定最大湿度，异常类型为过湿
////                abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERWETTING.name());
////            } else if (ecHumidityVal.compareTo(idcSetMinHumidity) < 0) {
////                // 采集到的湿度小于机房设定最小湿度，异常类型为过干
////                abnormalMap.put("abnormalType", AbnormalTypeEnum.OVERDRY.name());
////            }
////        }
////
////        return abnormalMap;
////    }
////
////    /**
////     * 温度控制算法
////     *
////     * @param abnormalTemperature 异常温度值
////     * @param abnormalType        异常类型
////     * @param cabinetCPEntity     异常的机柜实体
////     * @param acCPEntityList      下发指令操作的空调实体
////     * @throws UnsupportedActionNameException
////     * @throws UnsupportedAttributeNameException
////     */
////    public void temControlAlgorithm(String abnormalTemperature, AbnormalTypeEnum abnormalType, CPEntity cabinetCPEntity,
////                                    List<CPEntity> acCPEntityList) throws UnsupportedActionNameException, UnsupportedAttributeNameException {
////        // TODO 获取异常机柜的功率数据（当前redis数据，功率单位为：W） (机柜功率应包含柜内其它设备功率，数据推送时进行确认)
////        String totalPower = cabinetCPEntity.getAttributeByName("actualPower");
////        // 单位换算
////        BigDecimal totalPowerVal = new BigDecimal(totalPower).divide(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_UP);
////        if (StringUtils.isNotBlank(totalPower)) {
////            // 计算机柜所在空间所需制冷量（当前redis数据，长宽高单位为毫米mm，需要换算为米m）
////            BigDecimal cabinetArea = new BigDecimal(cabinetCPEntity.getAttributeByName("length"))
////                    .multiply(new BigDecimal(cabinetCPEntity.getAttributeByName("weight")))
////                    .divide(new BigDecimal(1000).pow(2)).setScale(2, BigDecimal.ROUND_UP);
////            String cabinetRefrigerationCapacity = AlgorithmUtils.getAreaRefrigerationCapacity(cabinetArea.toString(), totalPowerVal.toString());
////            // 设定温度指定 TODO (目前设定温度根据精密空调温度范围自己指定，后续考虑通过测量或者计算，出风量-设定温度-耗电量的关系，取满足条件下耗电量最少的节点数据)
////
////            // P：大气压强  φ：相对湿度  Tc：机房目标温度  Ts：空调设定温度  Q：机房所需制冷量  L：风量
////            double Ts = 19, L, P = 101325, φ = 0.55, Q = Double.parseDouble(cabinetRefrigerationCapacity), Tc = 24;
////            if (AbnormalTypeEnum.OVERCOOLING == abnormalType) {
////                // TODO 计算公式当前针对制冷过程（AbnormalTypeEnum.OVERHEAT）使用，制热过程可根据abnormalType判断，所需制冷量Q应为负数
////            }
////            // 根据国家标准GB/T7725－1996给出的制冷量的计算公式，计算空调出风量
////            L = AlgorithmUtils.getACAirVolume(Q, Ts, Tc, P, φ);
////
////            // 发送控制操作
////            for (CPEntity acCPEntity : acCPEntityList) {
////                // 声明封装的空调操作参数
////                ConcurrentHashMap<String, Object> actionParams = new ConcurrentHashMap<>();
////                actionParams.put("cpUuid", acCPEntity.getUuid());
////                // 温度控制
////                actionParams.put("cpActionName", "temperatureControl");
////                actionParams.put("TargetTemperature", Ts);
////                // 发送温度控制操作
////                this.entity.setActionByName("acTemperatureControl", actionParams);
////
////                // 风量控制
////                actionParams.put("cpActionName", "airVolumeControl");
////                // TODO 风速单位为m3/s，后续实施现场确认精密空调的出风量单位，保持一致
////                actionParams.put("AirVolume", L);
////                // 发送风速控制操作
////                this.entity.setActionByName("airVolumeControl", actionParams);
////            }
////
////        } else {
////            logger.info("机柜实体：" + cabinetCPEntity.getCpEntityMeta().getName() + " redis获取机柜功率为空");
////        }
////    }
////
////    /**
////     * 湿度控制算法
////     *
////     * @param abnormalHumidity 异常湿度值
////     * @param abnormalType     异常类型
////     * @param acCPEntityList   下发指令操作的空调集合
////     * @throws UnsupportedActionNameException
////     */
////    public void humidityControlAlgorithm(String abnormalHumidity, AbnormalTypeEnum abnormalType, List<CPEntity> acCPEntityList) throws UnsupportedActionNameException {
////        // 调用空调的除湿模式
////        for (CPEntity acCPEntity : acCPEntityList) {
////            // 声明封装的空调操作参数
////            ConcurrentHashMap<String, Object> actionParams = new ConcurrentHashMap<>();
////            actionParams.put("cpUuid", acCPEntity.getUuid());
////            actionParams.put("cpActionName", "humidityControl");
////            actionParams.put("TargetHumidity", "55");
////            // 发送湿度控制操作
////            this.entity.setActionByName("acHumidityControl", actionParams);
////        }
////    }
////
////    /**
////     * 温湿度算法
////     *
////     * @param abnormalTemperature 异常温度值
////     * @param abnormalHumidity    异常湿度值
////     * @param abnormalTypeEnum    异常类型
////     * @param cabinetCPEntity     异常机柜实体
////     * @param acCPEntityList      下发指令操作的空调实体
////     * @throws UnsupportedAttributeNameException
////     * @throws UnsupportedActionNameException
////     */
////    public void temAndHumControlAlgorithm(String abnormalTemperature, String abnormalHumidity, AbnormalTypeEnum abnormalTypeEnum, CPEntity cabinetCPEntity, List<CPEntity> acCPEntityList) throws UnsupportedAttributeNameException, UnsupportedActionNameException {
////        // 根据异常类型判断
////        if (abnormalTypeEnum == AbnormalTypeEnum.OVERHEATWETTING) {
////            //过热过湿
////            temControlAlgorithm(abnormalTemperature, AbnormalTypeEnum.OVERHEAT, cabinetCPEntity, acCPEntityList);
////            humidityControlAlgorithm(abnormalHumidity, AbnormalTypeEnum.OVERWETTING, acCPEntityList);
////        } else if (abnormalTypeEnum == AbnormalTypeEnum.OVERHEATDRY) {
////            //过热过干
////            temControlAlgorithm(abnormalTemperature, AbnormalTypeEnum.OVERHEAT, cabinetCPEntity, acCPEntityList);
////            humidityControlAlgorithm(abnormalHumidity, AbnormalTypeEnum.OVERDRY, acCPEntityList);
////        } else if (abnormalTypeEnum == AbnormalTypeEnum.OVERCOOLINGWETTING) {
////            //过冷过湿
////            temControlAlgorithm(abnormalTemperature, AbnormalTypeEnum.OVERCOOLING, cabinetCPEntity, acCPEntityList);
////            humidityControlAlgorithm(abnormalHumidity, AbnormalTypeEnum.OVERWETTING, acCPEntityList);
////        } else if (abnormalTypeEnum == AbnormalTypeEnum.OVERCOOLINGDRY) {
////            //过冷过干
////            temControlAlgorithm(abnormalTemperature, AbnormalTypeEnum.OVERCOOLING, cabinetCPEntity, acCPEntityList);
////            humidityControlAlgorithm(abnormalHumidity, AbnormalTypeEnum.OVERDRY, acCPEntityList);
////        }
////    }
//
//
//}
