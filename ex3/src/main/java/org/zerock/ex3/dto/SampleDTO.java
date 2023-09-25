package org.zerock.ex3.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data // Getter/Setter, toString()과 같은 메서드를 자동으로 생성함
@Builder(toBuilder = true)
public class SampleDTO {

    private Long sno;

    private String first;

    private String last;

    private LocalDateTime regTime;
}
