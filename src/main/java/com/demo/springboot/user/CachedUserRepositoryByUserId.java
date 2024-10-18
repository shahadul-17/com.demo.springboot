package com.demo.springboot.user;

import org.springframework.data.repository.CrudRepository;

public interface CachedUserRepositoryByUserId extends CrudRepository<CachedUserEntityByUserId, Long> { }
