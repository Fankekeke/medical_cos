package cc.mrbird.febs.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpaceUtil {

    static String AK = "A73cbd90c90dc8f155d0a5ccd00e130c"; // 百度地图密钥

    public static void main(String[] args) {
        String dom = "北京王府井";
        String coordinate = getCoordinate(dom);
        System.out.println("'" + dom + "'的经纬度为：" + coordinate);
        // System.err.println("######同步坐标已达到日配额6000限制，请明天再试！#####");
    }

    // 调用百度地图API根据地址，获取坐标
    public static String getCoordinate(String address) {
        if (address != null && !"".equals(address)) {
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=" + AK;
            String json = HttpUtil.get(url);
            System.out.println(json);
            if (StrUtil.isNotEmpty(json)) {
                JSONObject obj = JSONUtil.parseObj(json);
                if ("0".equals(obj.get("status").toString())) {
                    double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                    double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                    System.out.println(lng + "," + lat);
                    return lng + "," + lat;
                }
            }
            return null;
        }
        return null;
    }
}
