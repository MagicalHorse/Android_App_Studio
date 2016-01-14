package im.form;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MessageUserInfoBean implements Serializable {
    String _id;//"568cc893424c5a050c649501"
    String creationDate;//"2015-12-31T16:00:00.000Z"
    String logo;//"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3002653473,2069810134&fm=58"
    String name;//"aaa"
    String nickName;//"aaa"

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    int userId;//30944
}
