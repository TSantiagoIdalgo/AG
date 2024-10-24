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

  public String uploadFile(MultipartFile file) throws Exception {
    if (file.isEmpty()) {
      throw new Exception("File must not be empty");
    }
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "uploads"));

    String imageUrl = uploadResult.get("url").toString();

    return imageUrl;

  }

  public List<String> bulkUploadFiles(List<MultipartFile> files) {
    List<String> urls = new ArrayList<>();
    try {
      for (MultipartFile file : files) {
        String url = this.uploadFile(file);
        urls.add(url);
      }
    } catch (Exception e) {
      return urls;
    }

    return urls;
  }

}
