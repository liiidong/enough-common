package com.enough.common.utils;

import com.enough.common.model.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Pattern;

/**
 * @author lidong
 * @apiNote 文件工具类
 * @program datacube-server
 * @since 2020/10/09
 */
@Slf4j
public final class FileUtil {

    /**
     * Windows下文件名中的无效字符
     */
    public static final  Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");
    public static final  Pattern FILE_NAME_VALID_PATTERN_WIN = Pattern.compile("^(?![\\\\/:*?\"<>|]).*$");

    public static MultipartFile multipartFile(String file, String fieldName) {
        try {
            File f = new File(file);
            FileItem fileItem = new DiskFileItem(fieldName, Files.probeContentType(f.toPath()), false, f.getName(), (int) f.length(), f.getParentFile());
            try (InputStream is = new FileInputStream(f); OutputStream os = fileItem.getOutputStream()) {
                IOUtils.copy(is, os);
                os.flush();
            } catch (Exception e) {
                log.error("MultipartFile转换异常：", e);
            }
            return new CommonsMultipartFile(fileItem);
        } catch (Exception e) {
            log.error("MultipartFile转换异常：", e);
            throw new GlobalException("自定义报表统计结果导出失败,MultipartFile转换异常.");
        }
    }

    public static void downloadFile(String downloadFilePath, HttpServletResponse response) {
        File file = new File(downloadFilePath);
        downloadFile(file, response);
    }

    public static void downloadFile(File downloadFile, HttpServletResponse response) {
        if (downloadFile.exists()) {
            response.reset();
            response.setContentType(getContentType(downloadFile) + ";charset=utf-8");
            try {
                String fileName = new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } catch (Exception e) {
                log.error("文件名转码设置异常：", e);
                response.addHeader("Content-Disposition", "attachment;filename=" + downloadFile.getName());
            }
            byte[] buffer = new byte[1024];
            try (FileInputStream fis = new FileInputStream(downloadFile); BufferedInputStream bis = new BufferedInputStream(fis)) {
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                outputStream.flush();
            } catch (Exception e) {
                log.error("文件下载异常：", e);
                throw new GlobalException("文件下载异常.");
            }
        }
    }

    public static File file(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                boolean createFile = file.createNewFile();
                log.info("文件创建结果-->{}", createFile);
            }
            return file;
        } catch (Exception e) {
            log.info("文件创建失败-->", e);
        }
        return null;
    }

    public static File dir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            boolean createDir = dir.mkdirs();
            log.info("文件目录创建结果-->{}", createDir);
        }
        return dir;
    }

    public static boolean del(String filePath) {
        if (StringUtils.isNotBlank(filePath)) {
            return del(file(filePath));
        }
        return false;
    }

    public static boolean del(File file) {
        if (file != null) {
            if (file.isFile()) {
                return file.delete();
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                } else {
                    for (File childFile : childFiles) {
                        del(childFile);
                    }
                }
                return file.delete();
            }
        }
        return false;
    }

    private static String getContentType(File file) {
        String contextType;
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        switch (suffix.toLowerCase()) {
        case ".xls":
        case ".xlsx":
            contextType = "application/vnd.ms-excel";
            break;
        case ".zip":
            contextType = "application/gzip";
            break;
        case ".csv":
            contextType = "text/csv";
            break;
        case ".txt":
            contextType = "application/x-tex";
            break;
        default:
            //自动判断下载文件类型
            contextType = "multipart/form-data";
            break;
        }
        return contextType;
    }
}
