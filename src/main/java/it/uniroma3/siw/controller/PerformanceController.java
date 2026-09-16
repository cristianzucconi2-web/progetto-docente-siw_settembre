package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.AnalisiPerformanceService;
import it.uniroma3.siw.service.AnalisiPerformanceService.BenchmarkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

@Controller
public class PerformanceController {

    @Autowired
    private AnalisiPerformanceService performanceService;

    @GetMapping("/admin/performance")
    public String analisiPerformance(Model model) {
        Map<String, BenchmarkResult> risultati = performanceService.eseguiBenchmark();

        model.addAttribute("risultati", risultati);
        model.addAttribute("ripetizioni", 5);

        String migliore = risultati.entrySet().stream()
                .filter(e -> e.getValue().getTempoMs() > 0)
                .min((a, b) -> Long.compare(a.getValue().getTempoMs(), b.getValue().getTempoMs()))
                .map(Map.Entry::getKey)
                .orElse("-");

        model.addAttribute("migliore", migliore);
        return "admin/performance.html";
    }
}
