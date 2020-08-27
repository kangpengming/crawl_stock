package parser.lianjia;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kp on 2020/8/6.
 */
@Data
public class HouseInfo {

    private String location;
    private BaseInfo baseInfo;
    private TradeInfo tradeInfo;
    private List<DealRecord> dealRecordList = new ArrayList<DealRecord>();

    public void addDealRecord(DealRecord dealRecord) {
        this.dealRecordList.add(dealRecord);
    }
    @Data
    public static class DealRecord {
        private String unitPrice;
        private String detailInfo;

        @Override
        public String toString() {
            return "{" +
                    "unitPrice='" + unitPrice + '\'' +
                    "; detailInfo='" + detailInfo.replaceAll(",", ";") + '\'' +
                    '}';
        }
    }

    @Data
    public static class BaseInfo {
        private String houseType;
        private String floor;
        private String architectureArea;
        private String houseStruct;
        private String houseArea;
        private String architectureType;
        private String houseDirect;
        private String archYear;
        private String finishing;
        private String archStruct;
        private String heatingType;
        private String stairRatio;
        private String hasElevator;
    }

    public void addBase(Map<String, String> baseMap) {
        this.baseInfo = new BaseInfo();
        baseInfo.setHouseType(baseMap.get(attrs[0]));
        baseInfo.setFloor(baseMap.get(attrs[1]));
        baseInfo.setArchitectureArea(baseMap.get(attrs[2]) == null ? "" : baseMap.get(attrs[2]).replace("㎡", ""));
        baseInfo.setHouseStruct(baseMap.get(attrs[3]));
        baseInfo.setHouseArea(baseMap.get(attrs[4]) == null ? "" : baseMap.get(attrs[4]).replace("㎡", ""));
        baseInfo.setArchitectureType(baseMap.get(attrs[5]));
        baseInfo.setHouseDirect(baseMap.get(attrs[6]));
        baseInfo.setArchYear(baseMap.get(attrs[7]));
        baseInfo.setFinishing(baseMap.get(attrs[8]));
        baseInfo.setArchStruct(baseMap.get(attrs[9]));
        baseInfo.setHeatingType(baseMap.get(attrs[10]));
        baseInfo.setStairRatio(baseMap.get(attrs[11]));
        baseInfo.setHasElevator(baseMap.get(attrs[12]));
    }

    @Data
    public static class TradeInfo {
        private String lianjiaSerialNumber;
        private String tradeOwnership;
        private String tradeStartTime;
        private String houseUsage;
        private String houseAge;
        private String houseOwnership;
    }

    public void addTrade(Map<String, String> map) {
        this.tradeInfo = new TradeInfo();
        this.tradeInfo.setLianjiaSerialNumber(map.get(attrs[13]));
        this.tradeInfo.setTradeOwnership(map.get(attrs[14]));
        this.tradeInfo.setTradeStartTime(map.get(attrs[15]));
        this.tradeInfo.setHouseUsage(map.get(attrs[16]));
        this.tradeInfo.setHouseAge(map.get(attrs[17]));
        this.tradeInfo.setHouseOwnership(map.get(attrs[18]));

    }



    public transient static final String[] attrs = {
            "房屋户型", "所在楼层", "建筑面积", "户型结构", "套内面积", "建筑类型", "房屋朝向", "建成年代", "装修情况", "建筑结构", "供暖方式", "梯户比例", "配备电梯",
            "链家编号", "交易权属", "挂牌时间", "房屋用途", "房屋年限", "房权所属", "成交记录"
    };
}
