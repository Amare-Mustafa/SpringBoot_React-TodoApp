package com.amar.TodoApp.repository;

import com.amar.TodoApp.model.Entity_Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repo_Todo extends JpaRepository<Entity_Todo,Integer> {

/*    @Query(value = "SELECT * FROM todos WHERE completed = false", nativeQuery = true)*/
    @Query("SELECT t FROM Entity_Todo t WHERE t.completed = false")
    List<Entity_Todo> findUncompletedTodos();

    @Query("SELECT t FROM Entity_Todo t WHERE t.completed = true")
    List<Entity_Todo> findCompletedTodos();
}
