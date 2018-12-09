/*
 * Copyright 2018 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.byteliu.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * 类描述: 图片添加文字水印
 *
 * @author yugu.lx 2018/12/8 8:19 PM
 */
public class ImageUtil {

    public static void main(String args[]) {
        WaterMarkConfig config = new WaterMarkConfig("吴小雨", "/Users/liuxiao/Desktop");
        ImageUtil.textWaterMark("/Users/liuxiao/Desktop/liu.jpeg", config.setRotatedMultiWaterMark(true));

        java.util.List<String> imagePaths = new ArrayList<String>();
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-2018120811504064.png");
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-20181208113749138.png");
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-20181208114848218.png");
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-20181208114914481.png");
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-20181208114937773.png");
        imagePaths.add("/Users/liuxiao/Documents/hexo/source/_posts/如何实现大整数相加/markdown-img-paste-20181208115124960.png");
        imagePaths.add("/Users/liuxiao/Desktop/markdown-img-paste-20181208115124960_byteliu.png");

        ImageUtil.textWaterMark(imagePaths, config.setRotatedMultiWaterMark(true).setWaterMarkText("Byte_Liu").setFontSize(20).setAlpha(0.5f).setFontColor(Color.LIGHT_GRAY));

    }

    /**
     * 计算水印文本长度
     * 中文长度即文本长度
     * 英文长度为文本长度二分之一
     */
    private static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }

        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    public static void textWaterMark(java.util.List<String> imagePaths, WaterMarkConfig config) {
        for(String imagePath:imagePaths){
            textWaterMark(imagePath, config);
        }
//        imagePaths.forEach(imagePath ->textWaterMark(imagePath,config));
    }

    public static void textWaterMark(String imagePath, WaterMarkConfig config) {
        String imageFileName = imagePath.substring(imagePath.lastIndexOf(File.separator) + 1, imagePath.lastIndexOf(".")) + "_byteliu" + imagePath.substring(imagePath.lastIndexOf("."));
        OutputStream os;
        try {
            InputStream is = new FileInputStream(imagePath);
            //使用ImageIO解码图片
            Image image = ImageIO.read(is);
            //计算原始图片宽度长度
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            //创建图片缓存对象
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //创建java绘图工具对象
            Graphics2D graphics2d = bufferedImage.createGraphics();

            //参数主要是，原图，坐标，宽高
            graphics2d.drawImage(image, 0, 0, width, height, null);
            graphics2d.setFont(new Font(config.getFontName(), config.getFontStyle(), config.getFontSize()));
            graphics2d.setColor(config.getFontColor());


            //计算文字水印宽高值
            int waterWidth = config.getFontSize() * getTextLength(config.getWaterMarkText());
            int waterHeight = config.getFontSize();

            //水印透明设置
            graphics2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, config.getAlpha()));

            //使用绘图工具将水印绘制到图片上
            if (!config.isRotatedMultiWaterMark()) {
                //计算水印与原图高宽差
                int widthDiff = width - waterWidth;
                int heightDiff = height - waterHeight;

                //水印坐标设置
                int X = widthDiff - 150;
                int Y = heightDiff - 150;

                //纵坐标在下方，不增加字体高度会靠上
                graphics2d.drawString(config.getWaterMarkText(), X, Y + config.getFontSize());
            } else {
                graphics2d.rotate(Math.toRadians(30), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
                int x = -width / 2;
                int y;
                while (x < width * 1.5) {
                    y = -height / 2;
                    while (y < height * 1.5) {
                        graphics2d.drawString(config.getWaterMarkText(), x, y);
                        y += waterHeight + config.getMultiWaterMarkHeightSeparation();
                    }
                    x += waterWidth + config.getMultiWaterMarkWidthSeparation();
                }
            }

            graphics2d.dispose();
            os = new FileOutputStream(config.getOutputPath() + File.separator + imageFileName);

            //创建图像编码工具类
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);

            //使用图像编码工具类，输出缓存图像到目标文件
            en.encode(bufferedImage);

            if (is != null) {
                is.close();
            }

            if (os != null) {
                os.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static class WaterMarkConfig {
        /**
         * 水印文本
         */
        private String waterMarkText = "Byte_Liu";
        /**
         * 字体名
         */
        private String fontName = "宋体";
        /**
         * 字体类型
         */
        private int fontStyle = Font.ITALIC | Font.BOLD;
        /**
         * 字体大小
         */
        private int fontSize = 500;
        /**
         * 透明度，0-1之间的浮点值，值越小越透明
         */
        private float alpha = 0.1f;
        /**
         * 字颜色
         */
        private Color fontColor = Color.WHITE;
        /**
         * 加水印后的图片输出路劲
         */
        private String outputPath = System.getProperty("user.home");
        /**
         * 添加多条旋转文字水印标识，为false则添加单条文字水印
         */
        private boolean rotatedMultiWaterMark;
        /**
         * 多条水印横向间距
         */
        private int multiWaterMarkWidthSeparation = 100;
        /**
         * 多条水印纵向间距
         */
        private int multiWaterMarkHeightSeparation = 100;

        public WaterMarkConfig() {
        }

        public WaterMarkConfig(String outputPath) {
            this.outputPath = outputPath;
        }

        public WaterMarkConfig(String waterMarkText, String outputPath) {
            this.waterMarkText = waterMarkText;
            this.outputPath = outputPath;
        }

        public String getWaterMarkText() {
            return waterMarkText;
        }

        public WaterMarkConfig setWaterMarkText(String waterMarkText) {
            this.waterMarkText = waterMarkText;
            return this;
        }

        public String getFontName() {
            return fontName;
        }

        public WaterMarkConfig setFontName(String fontName) {
            this.fontName = fontName;
            return this;
        }

        public int getFontStyle() {
            return fontStyle;
        }

        public WaterMarkConfig setFontStyle(int fontStyle) {
            this.fontStyle = fontStyle;
            return this;
        }

        public int getFontSize() {
            return fontSize;
        }

        public WaterMarkConfig setFontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public float getAlpha() {
            return alpha;
        }

        public WaterMarkConfig setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Color getFontColor() {
            return fontColor;
        }

        public WaterMarkConfig setFontColor(Color fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public String getOutputPath() {
            return outputPath;
        }

        public WaterMarkConfig setOutputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public boolean isRotatedMultiWaterMark() {
            return rotatedMultiWaterMark;
        }

        public WaterMarkConfig setRotatedMultiWaterMark(boolean rotatedMultiWaterMark) {
            this.rotatedMultiWaterMark = rotatedMultiWaterMark;
            return this;
        }

        public int getMultiWaterMarkWidthSeparation() {
            return multiWaterMarkWidthSeparation;
        }

        public WaterMarkConfig setMultiWaterMarkWidthSeparation(int multiWaterMarkWidthSeparation) {
            this.multiWaterMarkWidthSeparation = multiWaterMarkWidthSeparation;
            return this;
        }

        public int getMultiWaterMarkHeightSeparation() {
            return multiWaterMarkHeightSeparation;
        }

        public WaterMarkConfig setMultiWaterMarkHeightSeparation(int multiWaterMarkHeightSeparation) {
            this.multiWaterMarkHeightSeparation = multiWaterMarkHeightSeparation;
            return this;
        }
    }

}

