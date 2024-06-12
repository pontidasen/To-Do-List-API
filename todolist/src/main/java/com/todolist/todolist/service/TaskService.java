package com.todolist.todolist.service;

import com.todolist.todolist.dto.TaskDto;
import com.todolist.todolist.models.Task;
import com.todolist.todolist.models.User;
import com.todolist.todolist.repository.TaskRepository;
import com.todolist.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTask(String usernameOrEmail)
    {
        //ไปสรา้ง  taskRepository ที่ query เฉพาะ task ของ user คนนั้นเท่านั้น
        //ตอน user login เข้ามาเราไม่รู้เค้าส่ง username หรือ email เข้ามาเราก็เลยเเก้ปัญหา โดยการส่งให้มันไป query ทั้งสอง field เลย
        return taskRepository.findByOwner_UsernameOrOwner_Email(usernameOrEmail,usernameOrEmail);
    }

    public List<Task> findTaskByName(String usernameOrEmail,String name)
    {
        //ไปสรา้ง  taskRepository ที่ query เฉพาะ task ของ user คนนั้นเท่านั้น
        //ตอน user login เข้ามาเราไม่รู้เค้าส่ง username หรือ email เข้ามาเราก็เลยเเก้ปัญหา โดยการส่งให้มันไป query ทั้งสอง field เลย
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndNameIsContaining(usernameOrEmail,usernameOrEmail,name);
    }

    public Optional<Task> findTaskById(Long id,String usernameOrEmail)
    {
        //คือทำการ query จาก id เเล้วเเล้วนำมากรองอีกทีนึงว่า userOwnerName ตรงกับ userที่เรา login ขึ้นมารึเปล่า ถ้าหากเป็นคนๆเดียวกันก็จะ return ค่าให้
        //คือ id ของ task มันมีเหมือนกันในเเต่ละ user นั่นล่ะ เเต่ทีนี้เราเอา id ของมันมาดูว่ามีเจ้าของคือใครเเล้วเอามาเทียบกับผู้ใช้ที่ login เข้ามาอีกทีว่าตรงกันไหมเเล้วค่อยส่งค่าให้ถ้าตรง
        return  taskRepository.findById(id).filter(task -> Objects.equals(task.getOwner().getName(),usernameOrEmail)
                                                        || Objects.equals(task.getOwner().getEmail(),usernameOrEmail));
    }

    public List<Task> findAllCompletedTask(String usernameOrEmail)
    {
       return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedTrue(usernameOrEmail,usernameOrEmail);
    }

    public List<Task> findAllUnCompletedTask(String usernameOrEmail)
    {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedFalse(usernameOrEmail,usernameOrEmail);
    }


    //Update task by id
    public Optional<Task> updateTask(Long id, Task task, String usernameOrEmail){
        Optional<Task> getTask = taskRepository. findById(id).filter(findtask ->
                Objects.equals(findtask.getOwner().getName(), usernameOrEmail) ||
                        Objects.equals(findtask.getOwner().getEmail(), usernameOrEmail));

        if(getTask.isEmpty()) {
            return getTask;
        }

            if (task.getName() != null ){
                getTask.get().setName(task.getName());

            }

            if (task.getDesc() != null){
                getTask.get().setDesc(task.getDesc());
            }

            if (task.getCompleted() != null){
                getTask.get().setCompleted(task.getCompleted());

            }

            return Optional.of(taskRepository.save(getTask.get()));
    }

    @Autowired
    private UserRepository userRepository;
    //พอเราหาได้เเล้วว่าเป็นuser คนไหนที่ login เข้ามาเราก็จะทำการเพิ่มข้อมูลลงใน field ของ owner_id
    public TaskDto createTask(Task task,String usernameOrEmail)
    {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail,usernameOrEmail);
        task.setOwner(user.get());

        Task taskSaved = taskRepository.save(task);

        //ถ้าเราส่งกลับ task เลยข้อมูลทั้งหมดของ User จะถูกส่งตอบกลับไปเเต่เราไม่ต้องการเเบบนั้นเราจึงต้องมี taskDto เพื่อเป็นเเบบformat ที่เราจะตอบกลับไป ว่าต้องการตอบกลับข้อมูลใดไปบ้างตามที่ต้องการเพื่อเลี่ยงการตอบกลับ Password
        TaskDto taskDto = new TaskDto();
        taskDto.setOwner_id(taskSaved.getOwner().getId());
        taskDto.setName(taskSaved.getName());
        taskDto.setDesc(taskSaved.getDesc());
        taskDto.setComplete(taskSaved.getCompleted());

        return taskDto;

    }

    public boolean deleteTask(Long id,String usernameOrEmail)
    {
        Optional<Task> getTask = taskRepository. findById(id).filter(findtask ->
                Objects.equals(findtask.getOwner().getName(), usernameOrEmail) ||
                Objects.equals(findtask.getOwner().getEmail(), usernameOrEmail));

        if(getTask.isEmpty())
        {
            return false;
        }
        //ต้อง setOwner ให้เป็น null ถึงจะสามารถลบ foreign key ได้
        getTask.get().setOwner(null);
        taskRepository.save(getTask.get());

        //พอทำให้เป็น null เเล้วจึงลบได้
        taskRepository.deleteById(id);
        return true;
    }
}
