package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.exception.BadApiRequestException;
import com.project.ElectronicStore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

      private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

      @Override
      public String uploadFile(MultipartFile file, String path) throws IOException {

            String originalFilename = file.getOriginalFilename();
            logger.info("FileName : {}", originalFilename);
            String filename = UUID.randomUUID().toString();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileNameWithExtension = filename+extension;
            //logger.info("FileNameWithExtension : {}", fileNameWithExtension);
            //String fullPAthWithFileName = path+ File.separator+fileNameWithExtension;
            String fullPAthWithFileName = path+ fileNameWithExtension;
            //logger.info("fullPAthWithFileName : {}", fullPAthWithFileName);

            if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg"))
            {
                        //file save
                  File folder = new File(path);

                  if(!folder.exists()){

                        //create folder
                        folder.mkdirs();
                  }
                  // upload
                  //adding InputStream in try block helps to close resource automatically
                  try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, Paths.get(fullPAthWithFileName));
                  }
//                  //upload
//                  Files.copy(file.getInputStream(), Paths.get(fullPAthWithFileName));
                  //logger.info("return statement : {}", fullPAthWithFileName);
                  return fileNameWithExtension;


            }else{

                  throw new BadApiRequestException("File with extension "+extension+" not allowed");
            }


      }

      @Override
      public InputStream getResource(String path, String name) throws FileNotFoundException {

            String fullPath = path + File.separator + name;
//            InputStream inputStream = new FileInputStream(fullPath);
//            return inputStream;
               return new FileInputStream(fullPath);
      }
}
