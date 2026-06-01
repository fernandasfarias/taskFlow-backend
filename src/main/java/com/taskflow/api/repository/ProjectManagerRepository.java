package com.taskflow.api.repository;

import org.springframework.stereotype.Repository;
import com.taskflow.api.entity.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

@Repository
public interface ProjectManagerRepository
    extends JpaRepository<ProjectManager, UUID>{
        boolean existsByEmail (String email);
}
