package cps.dyncar.service;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Junho
 * @date 2022/7/18 14:25
 */
@Service
public class DisTanceService{

    public double calcDistance(double startLon, double startLat, double endLon, double endLat) {

        GlobalCoordinates source = new GlobalCoordinates(startLon, startLat);
        GlobalCoordinates target = new GlobalCoordinates(endLon, endLat);
        return getDistanceMeter(source,target,Ellipsoid.Sphere);

    }

    public double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid) {
//        创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }

}
