package com.sft.model;

public class UserModel {
    private String id;//'编号',
    private String parent_id;// 父用户编号
    private String parent_id_set;// 父用户的编号集合
    private String login_name;//'登录名',
    private String password;//'密码',
    private String name;//'姓名',
    private String email;//'邮箱',
    private String phone;//'电话',
    private String mobile;//'手机',
    private String photo;//'头像',
    private String login_ip;//'最后登陆IP',
    private String login_date;//'最后登陆时间',
    private String create_date;//'创建时间',
    private String create_by;//'创建者',
    private String update_date;//'更新时间',
    private String update_by;//'更新者',
    private String remarks;//'备注信息',
    private int del_flag;//'删除标记',

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserModel) {
            UserModel user = (UserModel) obj;
            if (user.id.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_id_set() {
        return parent_id_set;
    }

    public void setParent_id_set(String parent_id_set) {
        this.parent_id_set = parent_id_set;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public String getLogin_date() {
        return login_date;
    }

    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }
}
