package com.fy.real.min.weibo.util.utils;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/23 19:57 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
public class UploadImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(UploadImageUtils.class);
    private static final Map<String,String> IMAGE_TYPE_SUFFIX = new HashMap<>();

    static {
        UploadImageUtils.IMAGE_TYPE_SUFFIX.put("jpeg","jpg");
        UploadImageUtils.IMAGE_TYPE_SUFFIX.put("x-icon","ico");
        UploadImageUtils.IMAGE_TYPE_SUFFIX.put("gif","gif");
        UploadImageUtils.IMAGE_TYPE_SUFFIX.put("png","png");
    }

    public static String upload(String base64File,String tempDir,String dir){
        if(base64File == null){
            return null;
        }
        // data:image/png;base64,图片数据
        String[] tempArr = base64File.split(",");
        try {
            // 得到类型 png
            String type = tempArr[0].split("/")[1].split(";")[0];
            if (!UploadImageUtils.IMAGE_TYPE_SUFFIX.containsKey(type)) {
                logger.error(type + ":暂未支持的类型");
                return null;
            }
            String suffix = UploadImageUtils.IMAGE_TYPE_SUFFIX.get(type);
            // 随机生成文件名
            String picName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
            String imageDate = tempArr[1];
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] imageByte = decoder.decodeBuffer(imageDate);
            for (int i = 0; i < imageByte.length; ++i){
                if (imageByte[i] < 0) {// 调整异常数据
                    imageByte[i] += 256;
                }
            }
            // 临时存储路径
            File tempFilePath = getDirFile(tempDir);
            // 服务存储路径
            File realPath = getDirFile(dir);
            // 构建临时的文件（服务器临时目录）
            File tempFile = new File(tempFilePath.getAbsolutePath() + File.separator + picName);
            // 上传图片到 -> “临时路径”
            FileUtils.copyToFile(new ByteArrayInputStream(imageByte),tempFile);
            // 构建真实的文件（永久保存）
            File realFile = new File(realPath.getAbsolutePath() + File.separator + picName);
            // 复制图片到 -> “真实路径”
            FileUtils.copyFile(tempFile,realFile);
            return picName;
        }catch (Exception e){
            logger.error("图片上传失败");
            return null;
        }
    }

    public static List<String> upload(List<MultipartFile> pics,String tempDir,String dir){
        List<String> names = new ArrayList<>();
        // 临时存储路径
        File tempFilePath = getDirFile(tempDir);
        // 服务存储路径
        File realPath = getDirFile(dir);
        pics.stream().filter(Objects::nonNull).forEach(pic->{
            // 获取文件后缀(含.号)
            String suffix = pic.getOriginalFilename() == null ? ""
                    : pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf("."));
            // 随机生成文件名
            String picName = UUID.randomUUID().toString().replace("-","")+ suffix;
            try {
                // 构建临时的文件（服务器临时目录）
                File tempFile = new File(tempFilePath.getAbsolutePath() + File.separator + picName);
                // 上传图片到 -> “临时路径”
                pic.transferTo(tempFile);
                // 构建真实的文件（永久保存）
                File realFile = new File(realPath.getAbsolutePath() + File.separator + picName);
                // 复制图片到 -> “真实路径”
                FileUtils.copyFile(tempFile,realFile);
                names.add(picName);
            }catch (Exception e){
                logger.error(pic.getOriginalFilename()+"：上传失败");
            }
        });
        return names;
    }

    private static File getDirFile(String dir){
        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = "";

        File fileDir = new File(dir);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        return fileDir;
    }

}
