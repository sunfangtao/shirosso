package com.sft.util;

import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * 平台上的错误信息
 */
public class CloudError {

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("221.0.91.34:2181,10.100.15.107:2081,192.168.100.105:2281", 6000, null);
//            zooKeeper.delete("/permission",-1);
            List<String> list = zooKeeper.getChildren("/", null);
            for (String key : list) {
                System.out.println(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回结果
     */
    public enum ResultEnum {

        SUCCESS("success"),
        FAIL("fail"),
        ;

        private String value;

        ResultEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Json返回的类型
     */
    public enum ReasonEnum {
        NODATA("NoDataException"),
        SERVEREXCEPTION("ServerException"),
        NORMAL("Normal"),
        PERMISSION("Permission"),
        NOREQUIREPARAMS("NoRequireParams");

        private String value;

        ReasonEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
