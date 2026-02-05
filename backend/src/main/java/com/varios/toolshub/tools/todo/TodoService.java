package com.varios.toolshub.tools.todo;

import com.varios.toolshub.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<TodoDtos.TodoResponse> list(Long userId) {
        return todoRepository.findByUserIdOrderByCompletedAscCreatedAtDesc(userId)
                .stream().map(t -> new TodoDtos.TodoResponse(t.getId(), t.getContent(), t.isCompleted(), t.getCreatedAt(), t.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public void create(Long userId, TodoDtos.TodoCreateRequest request) {
        Todo todo = new Todo();
        todo.setUser(userRepository.getReferenceById(userId));
        todo.setContent(request.content());
        todoRepository.save(todo);
    }

    @Transactional
    public void update(Long userId, Long id, TodoDtos.TodoUpdateRequest request) {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Todo not found"));
        todo.setContent(request.content());
        todo.setCompleted(request.completed());
    }

    @Transactional
    public void toggle(Long userId, Long id, boolean completed) {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Todo not found"));
        todo.setCompleted(completed);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Todo todo = todoRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new EntityNotFoundException("Todo not found"));
        todoRepository.delete(todo);
    }
}
