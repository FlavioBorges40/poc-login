package com.poc.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.poc.enumerator.RoleUserEnum;
import com.poc.enumerator.converter.RoleUserEnumConverter;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

@Entity
@Table(name = "tb_users")
@RegisterForReflection
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Size(min = 3, max = 60)
    @NotNull
    public String username;

    @NotNull
    public String email;

    @NotNull
	@Size(min = 3, max = 60)
    public String password;

    @Convert(converter = RoleUserEnumConverter.class)
    public RoleUserEnum role;
    
    @Column(length = 1024)
    public String token;
}
