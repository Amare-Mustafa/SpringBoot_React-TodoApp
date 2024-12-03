package com.amar.TodoApp.service;

import com.amar.TodoApp.model.Entity_Todo;
import com.amar.TodoApp.repository.Repo_Todo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Service_Todo {

    @Autowired
    private Repo_Todo repo;

    public List<Entity_Todo> getAllUncompletedTodos()
    {
        return repo.findUncompletedTodos();
    }
    public List<Entity_Todo> getAllCompletedTodos(){
        return  repo.findCompletedTodos();
    }

    public Entity_Todo getTodoById(int Id){
        return repo.findById(Id).orElse(null);
    }

    public Entity_Todo addTodo(Entity_Todo todo){
        return repo.save(todo);
    }

    public Entity_Todo updateTodo(Entity_Todo todo, int id){
        Entity_Todo exisitngTodo = repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Todo not found with id: "+ id));

        exisitngTodo.setTitle(todo.getTitle());
        exisitngTodo.setDescription(todo.getDescription());
        exisitngTodo.setCompleted(todo.isCompleted());
        exisitngTodo.setCreatedAt(LocalDateTime.now());

        return repo.save(exisitngTodo);
    }

    public void deleteTodo(int id){
        if(repo.existsById(id)) {
            repo.deleteById(id);
        }else{
            throw new EntityNotFoundException("Todo not found with id: "+ id);
        }
    }

    public void markAsCompleted(int id){
        Entity_Todo completedTodo = repo.findById(id).orElseThrow(()->new EntityNotFoundException("Todo not found with the Id: " + id));
        completedTodo.setCompleted(true);
        //after changing save it
        repo.save(completedTodo);
    }
}
