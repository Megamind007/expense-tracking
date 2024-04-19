package com.example.minispring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
   private String fileName;
   private String fileUrl;
   private String fileType;
   private Long fileSize;
}
