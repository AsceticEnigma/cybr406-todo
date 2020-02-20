package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController {

    @Autowired
    InMemoryTodoRepository repository;

    @Autowired
    TodoJpaRepository jpaTodoRes;

    /*@InitBinder("t")
    public void initTodoBinder(WebDataBinder binder) {
        binder.setValidator(new TodoValidator());
    }*/

    @PostMapping("/todos")
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo td) {
        Todo created = jpaTodoRes.save(td);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getDetails(@PathVariable long id) {

        Optional<Todo> todo = repository.find(id);

        if(todo.isPresent()) {
            Todo td = todo.get();
            return new ResponseEntity<>(td, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable pageable) {
        return jpaTodoRes.findAll(pageable);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id, @Valid @RequestBody Task task)
    {
        Todo todo = repository.addTask(id, task);
        List<Task> taskList = todo.getTasks();

        if(!taskList.isEmpty()) {
            return new ResponseEntity<>(todo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable long id) {

        try {
            repository.delete(id);
        } catch(NoSuchElementException n) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable long id) {

        try {
            repository.deleteTask(id);
        } catch(NoSuchElementException n) {

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}


