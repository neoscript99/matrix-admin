package com.feathermind.matrix.bean;

import lombok.Data;

/**
 * 扫描带参数二维码事件，从xml反序列化
 * @see <a href="https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_event_pushes.html">文档</a>
 */
@Data
public class SubscribeCallbackReq {
    //开发者微信号
    private String ToUserName;
    //发送方帐号（一个OpenID）
    private String FromUserName;
    //消息创建时间 （整型）
    private int CreateTime;
    //消息类型，event
    private String MsgType;
    //事件类型，subscribe
    private String Event;
    //事件KEY值，qrscene_为前缀，后面为二维码的参数值
    //对应自定义的参数（scene_str）
    private String EventKey;
    // 二维码的ticket，可用来换取二维码图片
    private String Ticket;
}
