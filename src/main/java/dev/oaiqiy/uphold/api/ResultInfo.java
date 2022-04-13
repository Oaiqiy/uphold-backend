package dev.oaiqiy.uphold.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultInfo<T> {
    private final Integer code;
    private final String msg;
    private T data;
}
