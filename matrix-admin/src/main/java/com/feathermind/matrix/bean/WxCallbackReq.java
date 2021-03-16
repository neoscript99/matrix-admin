package com.feathermind.matrix.bean;

import lombok.ToString;

/**
 * 扫描带参数二维码事件，从xml反序列化
 *
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html">文档</a>
 */
@ToString
public class WxCallbackReq {
    //微信返回xml报文字段都是大写，不符合get、set规范，所以直接用public的field
    //开发者微信号
    public String ToUserName;
    //发送方帐号（一个OpenID）
    public String FromUserName;
    //消息创建时间 （整型）
    public int CreateTime;
    //消息类型，event
    public String MsgType;
    //事件类型，subscribe
    public String Event;
    //事件KEY值，qrscene_为前缀，后面为二维码的参数值
    //对应自定义的参数（scene_str），实际微信发送的key没有前缀
    public String EventKey;
    // 二维码的ticket，可用来换取二维码图片
    public String Ticket;
}
