package com.demo.springboot.user;

import org.springframework.data.repository.CrudRepository;

public interface CachedUserRepositoryByPublicId extends CrudRepository<CachedUserEntityByPublicId, String> { }
