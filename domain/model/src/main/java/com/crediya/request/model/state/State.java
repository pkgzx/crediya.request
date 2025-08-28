package com.crediya.request.model.state;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class State {
    private Long id;
    private String name;
    private String description;
}
