package com.alipay.sdk.pay.demo;

import java.util.Map;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 重要说明:
 * <p>
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 */
public class PayDemoActivity extends FragmentActivity {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2017090608586413";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCYob+oQTodvgcLqWAbk+J+yTU4XPEtEbokwygufwe7zpyCihO3GKEWzRbL5t14ZJKaMzr3P8k4ugz1WvGJcuTnFq4ZPU8ZfT8JAYdLWDQSOeimyWNStwGcmI/j6bXq6y6myI0pbQgLhH13oHJNMfbbUoTP/I9LUSj2g91S75WVta52CgZmw00iiiyCxWQqBLi+mDOxBT4Kwa/0en057OGsVhAxv85aCMZ/IPhm6Ta5fFayWGqUGLvL7kwA4hhFk68Xl3UGT6QQaHEKYAzrXSaS9AHdPuFRKlrDE//vrfONPrhHMOxXvpvjThhYIykoebj6DbYmxUtJ4wl+EMSRoOHpAgMBAAECggEAPwMHzL6g74Z0Aix6sOfsqcsHXa2BI8odvu+Stx9aYf56PqoiWYShfHhO4P7+j6V1oJNl1I1Q1Up57xEMhmIYfg6u8VyOO0eprl4jLMfNN3kQw0qA5rUGxU92l/D0WXeeWtyQ6nlIyPh5k9l5VsU51HHMwtDRl5Z6AsuNo5+lcZhbC/8r4hbuhPFCaMYTRpdgM9GaQqEqWeHUxOHKB2Mx9PIwZVswzvzhbSDKyHYKmD072WjytoCxQnw/+dU18BvoPy88tFNKyNRH2N0ryZgw+tzeXFolZifcddUcOhAPK9MoMFb+jGhnXiQMWHlLYTpcIJ4k7PvIFG2s+Szq4iqnAQKBgQDjbALV5XNEZbjHR/9zKO60kdI+8gJemVHFy+ywhW78XK+oi+KSI6tBBHdrMbWyKp61H2KYbh+IUqw0k0rTvbNsrFm+cQ94VzjBosYodtf2mJDUN/6FrcNUQRchIuERS5mnZUvpBAUglnMx2kHRyExiFJQO1z9uZAJb6UyNNHIBBQKBgQCrz8w5bNkv/I4b3K4PKY7C0ewf2ic8I/Y1yi0SaKv7L4Qj58KvBVZAMwiza/8bqUjtgkMQ+vJQqWx1FK0R6pCMpXSOINT+zB5rm+87cLKn2Y5jc/gg5W8s+4nyL4qjnro497PSZV8Q9nR2MYbGF+jvsjVTCFzwk55bKQjopmdClQKBgQCDdX0SHWcK423zK8gazk9la2FH52a9Pg0Js/4mb4sfL4iOegXHCf1FQQqymPJ5ga9p7TF2AToS+A74+SdozCA6MkpSDlKt8mUpcSjwXPorXjdhpNhod3AQdOukyN+muregDqrZj+xS0QTXjV08oXadv11yUrQk4ISIkowgl29K7QKBgDYaw/c5fdOMtruzbOS9c4WKUc9eBYj80iXyOutXJwF83yHnc/llttmUuKK32ag+UQbqRHBudixMjij4j9/afBaua9vuHuT2JoZAnr+bJ8ePzTUoafUCC1ahB7nKmuAXthEGgAw7fAp9cgNeaVCsauBAwGYbdhkUg4O1kmahMFdZAoGBANTTh9pHywzC7270ym4gkDDt1cByySxkx4NS3slra5C7+MePR/Fq7im/0u6Kjzi954ay6NjaYpUKAt+xhsyzvX3A5oIrFLlXzgd6vl0X6EfCCCOinaY7oFRmRgtPTSDzBbVreQvuTXpTY2jnB/TQbiSryqtwylBIQOFkPU49g7be";
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }


                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        //Map第三个参数修改过后可转的自定义的金额
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,1);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayDemoActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
