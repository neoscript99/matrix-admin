# 第一步：基础支持: 获取access_token接口 /token
- 请求地址：
`https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx601fa973969006a4&secret=ee51729f9127038a5d4fd3a06fabcc15`
- 返回结果:
```
200 OK
Connection: close
Date: Thu, 11 Mar 2021 01:22:36 GMT
Content-Type: application/json; encoding=utf-8
Content-Length: 194
{
"access_token": "43_65gBVyaSHvEE5PHcI1-N8b2c-uk9QESWX5YcaeHLNc7OXpaVfH9RFjMH8jqxDLJ7bIb9U6hNmzXMmfnn0WHh3vdxAZ_Ly_yqPdW36qKtwVT6bx6bwgCX5-7uJkchqHqoIyMKM6Q_bMDmUeXgQPJbAJAAZT",
"expires_in": 7200
}
```
# 第二步：推广支持: 创建二维码ticket接口 /qrcode/create
- 请求地址：
https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=43_65gBVyaSHvEE5PHcI1-N8b2c-uk9QESWX5YcaeHLNc7OXpaVfH9RFjMH8jqxDLJ7bIb9U6hNmzXMmfnn0WHh3vdxAZ_Ly_yqPdW36qKtwVT6bx6bwgCX5-7uJkchqHqoIyMKM6Q_bMDmUeXgQPJbAJAAZT
- 请求JSON报文
```json
{
    "expire_seconds": 600, 
    "action_name": "QR_STR_SCENE", 
    "action_info": {
        "scene": {
            "scene_str": "scene_str"
        }
    }
}
```  
- 返回结果:
```
200 OK
Connection: keep-alive
Date: Thu, 11 Mar 2021 01:26:43 GMT
Content-Type: application/json; encoding=utf-8
Content-Length: 188
{
"ticket": "gQGJ7zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyaV9DRTU0WlljNEUxa0hROXh3Y1EAAgTTcUlgAwRYAgAA",
"expire_seconds": 600,
"url": "http://weixin.qq.com/q/02i_CE54ZYc4E1kHQ9xwcQ"
}
```
# 第三步：用户在客户端展示二维码链接
`https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGJ7zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyaV9DRTU0WlljNEUxa0hROXh3Y1EAAgTTcUlgAwRYAgAA`


# 用户管理: 获取用户基本信息接口 /user/info
- 请求地址：
https://api.weixin.qq.com/cgi-bin/user/info?access_token=43_65gBVyaSHvEE5PHcI1-N8b2c-uk9QESWX5YcaeHLNc7OXpaVfH9RFjMH8jqxDLJ7bIb9U6hNmzXMmfnn0WHh3vdxAZ_Ly_yqPdW36qKtwVT6bx6bwgCX5-7uJkchqHqoIyMKM6Q_bMDmUeXgQPJbAJAAZT&openid=ofTwE6J5RKIMMNTd8Jf05ffiR2Kg
- 返回结果:
```
200 OK
Connection: keep-alive
Date: Thu, 11 Mar 2021 03:20:44 GMT
Content-Type: application/json; encoding=utf-8
Content-Length: 436
{
"subscribe": 1,
"openid": "ofTwE6J5RKIMMNTd8Jf05ffiR2Kg",
"nickname": "楚",
"sex": 1,
"language": "zh_CN",
"city": "宁波",
"province": "浙江",
"country": "中国",
"headimgurl": "http://thirdwx.qlogo.cn/mmopen/aXUpZVUYfjwo47dMcqnGTsdK22cIv6nCkwV6CjoXvxfPG57OKP3O1EAw49vQxYzUnpuwK3HgDJibFDUWYYNiaia6w/132",
"subscribe_time": 1615362142,
"remark": "",
"groupid": 0,
"tagid_list": [ ],
"subscribe_scene": "ADD_SCENE_QR_CODE",
"qr_scene": 0,
"qr_scene_str": ""
}
```
