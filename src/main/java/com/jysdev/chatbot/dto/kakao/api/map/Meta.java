package com.jysdev.chatbot.dto.kakao.api.map;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Meta {
    private boolean is_end;
    private int pageable_count;
    private SameName same_name;
    private int total_count;
}
