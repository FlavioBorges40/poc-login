package com.poc.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.poc.model.User;

import io.quarkus.panache.common.Sort;

@ApplicationScoped
@Transactional
public class UserService {

	@Transactional
    public List<User> findAll() {
        return User.listAll();
    }

    @Transactional
    public User findById(Long id) {
        return User.findById(id);
    }
    
    @Transactional
    public User findByUsername(String username) {
    	return User.find("username", username).firstResult();
    }
    
    @Transactional
    public User findUser(User user) {
    	return User.find("username = ?1 OR email = ?2", user.username, user.email).firstResult();
    }

    @Transactional
    public List<User> findFilter(String columnName, String filter) {
        Sort sort = Sort.ascending("username");

        if (filter != null && !filter.isEmpty()) {
            return User.find("LOWER(?1) LIKE LOWER(?2)", sort, columnName, "%" + filter + "%").list();
        } else {
            return User.findAll(sort).list();
        }
    }

    public User insert(User user) {
    	user.persist();
        return user;
    }

    public User update(User persistent) {
    	persistent.persist();
        return persistent;
    }

    public void delete(Long id) {
        User user = User.findById(id);
        if(user!=null) {
            user.delete();
        }
    }
}
