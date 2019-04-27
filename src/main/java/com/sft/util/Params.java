package com.sft.util;

public class Params {

    public enum ResultEnum {
        Success("success"),
        Fail("fail");

        private String value;

        ResultEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ReasonEnum {
        NoApp("unknownAPPException"),
        NoFile("unknownFileException"),
        NoId("unknownIDException"),
        DuplicateAccount("duplicateAccountException"),
        TokenInvalidate("tokenInValidateException"),
        DataReadTimeout("dataReadTimeoutException"),
        Repeat("repeatException"),
        NoData("noDataException"),
        Sql("sqlException"),
        Server("serverException"),
        Normal("normal"),
        Permission("permissionException"),
        NoLogin("noLoginException"),
        PasswordError("passwordErrorException"),
        NoAccount("unknownAccountException"),
        NoRequireParams("noRequireParamsException"),
        IoException("iOException"),
        NoMoreDataException("noMoreDataException");

        private String value;

        ReasonEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum JsonFormatEnum {
        Custom("custom"),
        Layui("layui");

        private String value;

        JsonFormatEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}