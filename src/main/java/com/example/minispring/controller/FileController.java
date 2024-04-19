package com.example.minispring.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.minispring.model.ApiResponse.ApiResponse;
import com.example.minispring.model.Response.FileResponse;
import com.example.minispring.service.FileService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/files")
@AllArgsConstructor
public class FileController {
   private final FileService fileService;
   @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ResponseEntity<?> uploadOneFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = fileService.saveFile(file);
        FileResponse fileResponse = new FileResponse(fileName,file.getContentType(), file.getSize());
        ApiResponse<FileResponse> response = (ApiResponse.<FileResponse>builder()
                    .message(fileResponse.getFileName().equals("Upload Failed")? "Upload Fail" :"successfully uploaded file")
                    .payload(fileResponse)
                    .httpStatus(HttpStatus.OK)
                    .localDateTime(LocalDateTime.now()).build());
       return ResponseEntity.ok(response);
   }
    @GetMapping
    public ResponseEntity<?> getFile(@RequestParam String fileName) {
        try {
            Resource resource = fileService.getFileByFileName(fileName);
            MediaType mediaType;
            if (fileName.endsWith(".pdf")){mediaType = MediaType.APPLICATION_PDF;}
            else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif")){
                mediaType = MediaType.IMAGE_PNG;} 
            else {mediaType = MediaType.APPLICATION_OCTET_STREAM;}
            return ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
               .contentType(mediaType).body(resource);
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }
}
