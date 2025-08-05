package com.analitycore.analyzer.service;

import com.analitycore.analyzer.model.Job;
import com.analitycore.analyzer.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Optional<Job> procesarJob(UUID id) {
        Optional<Job> jobOpt = jobRepository.findById(id);

        jobOpt.ifPresent(job -> {
            System.out.println(" Recibido trabajo con ID: " + job.getId());
            System.out.println(" Cambiando estado a PROCESANDO...");

            job.setEstado("PROCESANDO");
            jobRepository.save(job); // guardar estado intermedio

            try {
                Thread.sleep(3000); // Simular análisis por 3 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupción durante análisis");
            }

            // 🔍 Analizar el texto con la lógica simple
            String resultado = analizarTexto(job.getTexto());
            job.setResultado(resultado);
            job.setEstado("COMPLETADO");

            jobRepository.save(job);
            System.out.println(" Trabajo COMPLETADO con resultado: " + resultado);
        });

        return jobOpt;
    }

    /**
     * Análisis simulado: calcula sentimiento y palabras clave básicas
     */
    private String analizarTexto(String texto) {
        // Palabras positivas y negativas simuladas
        String[] positivas = {"bueno", "excelente", "feliz", "rápido", "amable", "maravilloso"};
        String[] negativas = {"malo", "lento", "horrible", "triste", "pésimo", "terrible"};

        int score = 0;
        String textoLower = texto.toLowerCase();

        for (String p : positivas) {
            if (textoLower.contains(p)) score++;
        }

        for (String n : negativas) {
            if (textoLower.contains(n)) score--;
        }

        String sentimiento;
        if (score > 0) sentimiento = "POSITIVO";
        else if (score < 0) sentimiento = "NEGATIVO";
        else sentimiento = "NEUTRO";

        // Palabras clave simuladas: las más largas del texto
        String[] palabras = textoLower.split("\\s+");
        List<String> claves = Arrays.stream(palabras)
                .filter(p -> p.length() > 6) // palabras largas
                .distinct()
                .limit(3)
                .collect(Collectors.toList());

        return "Sentimiento: " + sentimiento + "; Palabras clave: " + claves;
    }

    public Job guardarJob(Job job) {
        System.out.println("Creando nuevo trabajo en base de datos con estado PENDIENTE");
        return jobRepository.save(job);
    }
}
