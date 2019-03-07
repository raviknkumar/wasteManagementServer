package com.example.waste_management_server.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
@Data
public class InputFileConfiguration {

    @Value("${inputFileBasePath}")
    private String inputBaseFilePath;

    private final String fileName = "input";
    private final String txtExtension = ".txt";

    public String getInputFileAbsoluteUrl()
    {
        return inputBaseFilePath+"//"+fileName+txtExtension;
    }

}
