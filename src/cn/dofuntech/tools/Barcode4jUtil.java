package cn.dofuntech.tools;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * <p>
 * 
 * </p>
 * <font size=0.25>Copyright (C) 2018 dofuntech. All Rights Reserved.</font>
 * @author lxu(2018年4月9日)
 * @version 1.0
 * filename:Barcode4jUtil.java 
 */
public class Barcode4jUtil {

    /**
     * 生成文件
     *
     * @param msg
     * @param path
     * @return
     */
    public static File generateFile(String msg, String path) {
        File file = new File(path);
        try {
            generate(msg, new FileOutputStream(file));
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 生成字节
     *
     * @param msg
     * @return
     */
    public static byte[] generate(String msg) {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg, ous);
        return ous.toByteArray();
    }

    /**
     * 生成到流
     *
     * @param msg
     * @param ous
     */
    public static void generate(String msg, OutputStream ous) {
        if (StringUtils.isEmpty(msg) || ous == null) {
            return;
        }

        try {
            Code39Bean bean = new Code39Bean();

            // 精细度
            final int dpi = 150;
            // module宽度
            final double moduleWidth = UnitConv.in2mm(1.0f / dpi);

            // 配置对象
            bean.setModuleWidth(moduleWidth);
            bean.setWideFactor(3);
            bean.doQuietZone(false);
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);

            String format = "image/png";

            // 输出到流
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 生成条形码
            bean.generateBarcode(canvas, msg);

            // 结束绘制
            canvas.finish();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String msg = "123456789";
        String path = "e:/barcode.png";
        generateFile(msg, path);
    }

}
