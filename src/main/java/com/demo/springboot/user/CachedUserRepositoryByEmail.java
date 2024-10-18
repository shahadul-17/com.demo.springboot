package com.demo.springboot.user;

import org.springframework.data.repository.CrudRepository;

public interface CachedUserRepositoryByEmail extends CrudRepository<CachedUserEntityByEmail, String> { }
