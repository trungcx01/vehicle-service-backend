package com.example.vehicleService.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.vehicleService.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file, String folderPath) {
        try{
            Map params1 = ObjectUtils.asMap(
                    "folder", folderPath,
                    "use_filename", false,  //su dung file name của file upload
                    "unique_filename", true, //neu trung ten thi se tien hanh overwrite
                    "overwrite", false  // enable overwrite(ghi de)
            );
            Map data = this.cloudinary.uploader().upload(file.getBytes(), params1);
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }
}
