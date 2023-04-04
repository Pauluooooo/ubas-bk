package com.datasystem.utils;

import com.datasystem.entity.Result;

import java.util.Map;

/**
 * @author Pauluooooo
 * Date:2023/4/3 16:36
 */

public class SendPhoneCode {
  /**
   * 使用AK&SK初始化账号Client
   *
   * @param accessKeyId
   * @param accessKeySecret
   * @return Client
   * @throws Exception
   */
  public static com.aliyun.teaopenapi.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
    com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
            // 必填，您的 AccessKey ID
            .setAccessKeyId(accessKeyId)
            // 必填，您的 AccessKey Secret
            .setAccessKeySecret(accessKeySecret);
    // 访问的域名
    config.endpoint = "dysmsapi.aliyuncs.com";
    return new com.aliyun.teaopenapi.Client(config);
  }

  /**
   * API 相关
   *
   * @return OpenApi.Params
   */
  public static com.aliyun.teaopenapi.models.Params createApiInfo() throws Exception {
    com.aliyun.teaopenapi.models.Params params = new com.aliyun.teaopenapi.models.Params()
            // 接口名称
            .setAction("SendSms")
            // 接口版本
            .setVersion("2017-05-25")
            // 接口协议
            .setProtocol("HTTPS")
            // 接口 HTTP 方法
            .setMethod("POST")
            .setAuthType("AK")
            .setStyle("RPC")
            // 接口 PATH
            .setPathname("/")
            // 接口请求体内容格式
            .setReqBodyType("json")
            // 接口响应体内容格式
            .setBodyType("json");
    return params;
  }

  public static Result sendCode(String phone, int code) throws Exception {
    com.aliyun.teaopenapi.Client client = SendPhoneCode.createClient(SystemConstants.ACCESS_KEY, SystemConstants.ACCESSKEY_SECRET);
    com.aliyun.teaopenapi.models.Params params = SendPhoneCode.createApiInfo();
    // query params
    java.util.Map<String, Object> queries = new java.util.HashMap<>();
    queries.put("PhoneNumbers", phone);
    queries.put("SignName", "用户分析系统");
    queries.put("TemplateCode", "SMS_272920077");
    queries.put("TemplateParam", "{\"code\":\"" + code + "\"}");

    com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
    com.aliyun.teaopenapi.models.OpenApiRequest request = new com.aliyun.teaopenapi.models.OpenApiRequest()
            .setQuery(com.aliyun.openapiutil.Client.query(queries));

    Map<String, ?> callApi = client.callApi(params, request, runtime);
    Integer statusCode = (Integer) callApi.get("statusCode");

    if (!statusCode.equals(SystemConstants.SEND_CODE_STATUS)) {
      Object message = callApi.get("Message");
      return Result.fail("Send error:" + message);
    }

    return Result.ok(code);
  }


}