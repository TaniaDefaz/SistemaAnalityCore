package com.analitycore.analyzer.controller;

import com.analitycore.analyzer.model.Job;
import com.analitycore.analyzer.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/analyze")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Job> analizarTexto(@PathVariable UUID id) {
        Optional<Job> job = jobService.procesarJob(id);
        return job.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

/*@PostMapping("/crear")
public ResponseEntity<Job> crearJob(@RequestBody Job nuevoJob) {
    nuevoJob.setEstado("PENDIENTE");
    return ResponseEntity.ok(jobService.guardarJob(nuevoJob));
}*/

}
