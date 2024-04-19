package com.example.minispring.model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponse {
   private String fileName;
   private String fileType;
   private Long fileSize;
}