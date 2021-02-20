package cn.dofuntech.qiniu;

import cn.dofuntech.qiniu.common.Config;
import cn.dofuntech.qiniu.common.QiniuException;
import cn.dofuntech.qiniu.http.Response;
import cn.dofuntech.qiniu.storage.UploadManager;

public class Test {

    public static void main(String[] args) throws QiniuException {
        UploadManager uploadManager = new UploadManager();
        Response res = uploadManager.put("d:/40×40.jpg", "40×40.jpg", Config.QINIU_UTOKEN);
        System.out.println(" -- 远程上传文件返回：" + res.bodyString());
    }

    //    public static void main(String[] args) {
    //        //前一个参数是从七牛网站上得到的AccessKey,后一个参数是SecretKey
    //        Auth auth = Auth.create("XSIiA0z2sTzgQrZSWohJCvPDdbPrOB_O7XataC2-", "i1W6K5_4n95Q69Vcy3WCNIS7ZP1FqRBVUexiLl0z");
    //        //第一个参数是你建立的空间名称，第三个是时间长度(按毫秒算的，我这里写的是10年)
    //        String token = auth.uploadToken("cybereasterntest", null, 1000 * 3600 * 24 * 12 * 10, null);
    //        System.out.println(token);
    //    }

}
