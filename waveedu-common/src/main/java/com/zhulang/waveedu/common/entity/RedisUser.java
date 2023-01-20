package com.zhulang.waveedu.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 狐狸半面添
 * @create 2023-01-18 0:41
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedisUser implements Serializable {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 时间
     */
    private Long time;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户头像
     */
    private String icon;
    /**
     * 权限
     */
    private List<String> permissions;

    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions==null){
            return null;
        }
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
