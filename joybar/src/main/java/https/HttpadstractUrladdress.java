package https;


import config.HttpUrlConfig;

/**
 * Created by Administrator on 2015/11/3.
 */
public abstract class HttpadstractUrladdress {
    HttpUrlConfig.Cotrol_Type cotrol_type;
    public HttpadstractUrladdress(HttpUrlConfig.Cotrol_Type cotrol_type)
    {
        this.cotrol_type=cotrol_type;
    }
    public abstract String  getUrl();
}
