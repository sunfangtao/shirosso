/**
 * @Project:crm
 * @Title:UsernamePasswordToken.java
 * @Author:Riozenc
 * @Datetime:2016年10月24日 下午9:28:24
 */
package com.sft.token;

public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private boolean mobileLogin;

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password, boolean mobileLogin) {
        super(username, password);
        this.mobileLogin = mobileLogin;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

}
