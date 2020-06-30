package ru.lanit.at.json;

import lombok.Getter;

@Getter
public class JsonRequest {
    private String site_url;
    private String browser;
    private String browser_config;
    private String remote;
    private String proxy;
    private String proxy_config;
    private String hub_url;
}
