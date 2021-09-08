package com.feathermind.matrix.wechat.mp.bean;

import cn.hutool.core.lang.UUID;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class WxQrcodeCreateReq {
    public WxQrcodeCreateReq() {
        String scene_str = UUID.fastUUID().toString(true);
        this.action_info = new ActionInfo(scene_str);
    }

    public WxQrcodeCreateReq(String scene_str) {
        this.action_info = new ActionInfo(scene_str);
    }

    //该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    //默认十分钟有效，前台检查次数也应该有所限制
    private long expire_seconds = TimeUnit.MINUTES.toSeconds(10);
    //二维码类型，QR_SCENE为临时的整型参数值，
    //  QR_STR_SCENE为临时的字符串参数值，
    //  QR_LIMIT_SCENE为永久的整型参数值，
    //  QR_LIMIT_STR_SCENE为永久的字符串参数值
    private String action_name = "QR_STR_SCENE";
    //二维码详细信息
    private ActionInfo action_info;

    @Data
    static public class ActionInfo {
        public ActionInfo(String scene_str) {
            this.scene = new Scene(scene_str);
        }

        private Scene scene;
    }

    @Data
    static public class Scene {
        public Scene(String scene_str) {
            this.scene_str = scene_str;
        }

        //场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
        // 默认为扫码登录场景，设为10
        private int scene_id = 10;
        //场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
        private String scene_str;
    }
}
