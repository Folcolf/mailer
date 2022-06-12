package com.folcolf.automailer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
public class HeaderMessage {

    private String from;
    private List<String> to;
    private String subject;
    private Map<String, String> attachments;
    private Map<String, Object> props;

}
