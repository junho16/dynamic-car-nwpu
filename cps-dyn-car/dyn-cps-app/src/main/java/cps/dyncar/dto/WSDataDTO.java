package cps.dyncar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSDataDTO {

    private String type;

    private Long timeStamp;

    private List<DynCarDTO> cars;
}
