package com.login.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER",schema="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 자동 생성 전략
    @Column(name = "ID")
    private Long id;  // 자동 증가하는 PK로 변경

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_PW")
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLE",  // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "ID"),  // USER 테이블의 PK 컬럼
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")  // ROLE 테이블의 컬럼
    )
    private Set<Role> roles;
}