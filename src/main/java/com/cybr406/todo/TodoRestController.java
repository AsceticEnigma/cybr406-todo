package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class TodoRestController {

    @Autowired
    TaskJpaRepository jpaTaskRep;

    @Autowired
    TodoJpaRepository jpaTodoRep;

    /*@InitBinder("t")
    public void initTodoBinder(WebDataBinder binder) {
        binder.setValidator(new TodoValidator());
    }*/

    @PostMapping("/todos")
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo td) {
        Todo created = jpaTodoRep.save(td);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getDetails(@PathVariable long id) {

        Optional<Todo> todo = jpaTodoRep.findById(id);

        if(todo.isPresent()) {
            Todo td = todo.get();
            return new ResponseEntity<>(td, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable pageable) {
        return jpaTodoRep.findAll(pageable);
    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id, @Valid @RequestBody Task task)
    {
        Todo todo;
        Optional<Todo> todos = jpaTodoRep.findById(id);
        if (todos.isPresent())
        {
            todo = todos.get();
            todo.getTasks().add(task);
            task.setTodo(todo);
            jpaTaskRep.save(task);
            return new ResponseEntity<>(todo, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable long id) {

        if(jpaTodoRep.existsById(id))
        {
            jpaTodoRep.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable long id) {

        if(jpaTaskRep.existsById(id))
        {
            jpaTaskRep.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}


