package com.zfettostudios.zfuctone.util.pair;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PairOfTwo<F, S> {
    private F first;
    private S second;

    public static <F, S> PairOfTwo<F, S> of(F first, S second) {
        return new PairOfTwo<>(first, second);
    }
}
