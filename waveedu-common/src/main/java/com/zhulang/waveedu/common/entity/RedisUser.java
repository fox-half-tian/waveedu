package com.zhulang.waveedu.common.entity;

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
public class RedisUser implements Serializable {
    /**
     * jwt
     */
    private String jwt;
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

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
