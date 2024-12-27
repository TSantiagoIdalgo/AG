package com.ancore.ancoregaming.product.services.upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

  @Value("${CLOUDINARY_URL}")
  private String cloudinaryUrl;

  public String uploadImage(MultipartFile file) throws Exception {
    if (file == null) {
      throw new Exception("File must not be empty");
    }
    if (file.isEmpty()) {
      throw new Exception("File must not be empty");
    }
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);

    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "uploads/images"));
    
    return uploadResult.get("url").toString();

  }

  public String uploadVideo(MultipartFile video) throws Exception {
    if (video == null) {
      throw new Exception("File must not be empty");
    }
    if (video.isEmpty()) {
      throw new Exception("File must not be empty");
    }
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
    Map<?, ?> uploadResult = cloudinary.uploader().upload(video.getBytes(), ObjectUtils.asMap(
            "folder", "uploads/video",
            "resource_type", "video"
    ));
    
    return uploadResult.get("url").toString();
  }

  public List<String> bulkUploadFiles(List<MultipartFile> files) {
    List<String> urls = new ArrayList<>();
    if (files == null || !(files instanceof List)) {
      return urls;
    }
    try {
      for (MultipartFile file : files) {
        String url = this.uploadImage(file);
        urls.add(url);
      }
    } catch (Exception e) {
      return urls;
    }

    return urls;
  }

  public void bulkDeleteFiles(List<String> urls) throws Exception {
    if (urls.isEmpty()) {
      return;
    }
    for (String url : urls) {
      this.deleteImage(url);
    }
  }

  public void deleteVideo(String url) throws Exception {
    if (url == null) {
      return;
    }
    String publicId = extractPublicId(url);
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
    cloudinary.api().deleteResources(List.of(publicId),
            ObjectUtils.asMap("type", "upload", "resource_type", "video"));
  }

  public void deleteImage(String url) throws Exception {
    if (url == null) {
      return;
    }
    String publicId = extractPublicId(url);
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
    cloudinary.api().deleteResources(List.of(publicId),
            ObjectUtils.asMap("type", "upload", "resource_type", "image"));
  }

  private String extractPublicId(String url) {
    String baseUrl = "image/upload/";
    int start = url.indexOf(baseUrl) + baseUrl.length();

    if (start <= baseUrl.length()) {
      start = url.indexOf("video/upload/") + baseUrl.length();
    }

    String pathWithVersion = url.substring(start);
    pathWithVersion = pathWithVersion.replaceFirst("v\\d+/", "");
    int end = pathWithVersion.lastIndexOf(".");
    return (end == -1) ? pathWithVersion : pathWithVersion.substring(0, end);
  }
}
