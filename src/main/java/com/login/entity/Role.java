package com.login.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROLE",schema="user")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 자동 생성 전략
    @Column(name = "ROLE_ID")
    private Long id;

    @Column(name = "ROLE_NAME", unique = true)
    private String name;  // 예: ROLE_USER, ROLE_ADMIN

}