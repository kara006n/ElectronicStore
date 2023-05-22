package com.project.ElectronicStore.dtos;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {

      private List<T> content;
      private int pageNumber;
      private int pageSize;
      private Long totalElements;
      private int totalPages;
      private boolean lastPage;



}
