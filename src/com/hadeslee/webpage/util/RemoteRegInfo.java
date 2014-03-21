/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.io.Serializable;
import java.util.Date;

/**
 * 定义在远程的注册信息
 * @author Hadeslee
 */
public class RemoteRegInfo implements Serializable {

    private static final long serialVersionUID = 20100101L;
    private String regId;
    private String regPwd;
    private Date expiredDate;

    public RemoteRegInfo() {
    }

    public RemoteRegInfo(String regId, String regPwd, Date expiredDate) {
        this.regId = regId;
        this.regPwd = regPwd;
        this.expiredDate = expiredDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegPwd() {
        return regPwd;
    }

    public void setRegPwd(String regPwd) {
        this.regPwd = regPwd;
    }
}
