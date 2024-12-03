package com.amar.TodoApp.controller;

import com.amar.TodoApp.model.Entity_Todo;
import com.amar.TodoApp.service.Service_Todo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class Controller_Todo {

    @Autowired
    private Service_Todo service_todo;

    @GetMapping("/todos/uncompleted")
    public List<Entity_Todo> getAllUncompletedTodos(){
        return  service_todo.getAllUncompletedTodos();
    }

    @GetMapping("/todos/completed")
    public List<Entity_Todo> getAllCompletedTodos(){
        return  service_todo.getAllCompletedTodos();
    }

    @GetMapping("/todos/{id}")
    public Entity_Todo getTodo(@PathVariable int id){
        return service_todo.getTodoById(id);
    }

    @PostMapping("/todos")
    public ResponseEntity<?> addTodo(@RequestBody Entity_Todo todo){
        try{
            Entity_Todo savedTodo = service_todo.addTodo(todo);
            return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<String> updateTodo(@PathVariable int id, @RequestBody Entity_Todo todo){
        Entity_Todo updatedTodo;
        try{
            updatedTodo = service_todo.updateTodo(todo,id);
        }catch(EntityNotFoundException ex){
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);

        }

        if(updatedTodo != null){
            return new ResponseEntity<>("Updated",HttpStatus.OK);

        }else{
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable int id){
        try{
            service_todo.deleteTodo(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/todos/{id}/complete")
    public  ResponseEntity<?> markAsCompleted(@PathVariable int id){
        try{
            service_todo.markAsCompleted(id);
            return new ResponseEntity<>("Completed!",HttpStatus.OK);
        }catch (EntityNotFoundException ex){
            return new ResponseEntity<>("Failed to mark todo as Compeleted",HttpStatus.BAD_REQUEST);
        }
    }

}
