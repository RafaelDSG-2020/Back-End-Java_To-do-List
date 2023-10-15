package br.com.rafael.todolist.task;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rafael.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;




@RestController
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel , HttpServletRequest request){

       // taskModel.setPriority("BAIXA");

        //System.out.println("Chegou no Controller ");
        //System.out.println(request.getAttribute("idUser"));
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStratAt()) || currentDate.isAfter(taskModel.getEndAt()))//currentDate.isAfter(taskModel.getCreatedAt())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de inicio ou data de terminio deve ser maior que a atual");
        }

        if(taskModel.getStratAt().isAfter(taskModel.getEndAt()))//currentDate.isAfter(taskModel.getCreatedAt())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de inicio deve ser menor do que de terminio");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request)
    {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }
    @PutMapping("/{id}")
    public ResponseEntity uptade(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id)
    {
        
        var task = this.taskRepository.findById(id).orElse(null);
        
        if(task == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A tarefa não encontrada");
        }



        var idUser = request.getAttribute("idUser");
        
        if(!task.getIdUser().equals(idUser))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("O Usuario nao tem permissao para alterar a tarefa");
        }

        


        // taskModel.setIdUser((UUID)idUser);
        // taskModel.setId(id);
        Utils.copyNonNullProperties(taskModel, task);
        var taksUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(taksUpdated));
        //return this.taskRepository.save(task);
    }
}
