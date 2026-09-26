package com.generated.qualityTrace.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.qualityTrace.routes.QualityInspectionRoutes;
import com.generated.qualityTrace.services.QualityInspectionService;
import com.generated.qualityTrace.types.FinalInspectionSubmission;

@RestController
@RequestMapping(QualityInspectionRoutes.PATH)
public class QualityInspectionController {
  private final QualityInspectionService service;

  public QualityInspectionController(QualityInspectionService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() { return service.list(); }

  @GetMapping("/{id}")
  public Map<String, Object> detail(@PathVariable long id) { return service.detail(id); }

  @PostMapping("/final")
  public Map<String, Object> submitFinal(@RequestBody FinalInspectionSubmission req) {
    return service.submitFinalInspection(req);
  }
}
