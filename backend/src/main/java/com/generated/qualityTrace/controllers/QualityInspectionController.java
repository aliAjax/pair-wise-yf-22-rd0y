package com.generated.qualityTrace.controllers;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.generated.qualityTrace.constants.ErrorCodes;
import com.generated.qualityTrace.constants.ErrorMessages;
import com.generated.qualityTrace.routes.QualityInspectionRoutes;
import com.generated.qualityTrace.services.QualityInspectionService;
import com.generated.qualityTrace.types.QualityInspectionPayload;
@RestController
@RequestMapping(QualityInspectionRoutes.PATH)
public class QualityInspectionController {
  private final QualityInspectionService service;
  public QualityInspectionController(QualityInspectionService service){this.service=service;}

  @GetMapping public List<Map<String,Object>> list(){return service.list();}

  @PostMapping("/submit")
  public ResponseEntity<?> submit(@RequestBody QualityInspectionPayload payload) {
    try {
      return ResponseEntity.ok(service.submit(payload));
    } catch (NoSuchElementException e) {
      String code = ErrorMessages.GAUGE_NOT_FOUND.equals(e.getMessage()) ? ErrorCodes.GAUGE_NOT_FOUND : ErrorCodes.RESOURCE_NOT_FOUND;
      return ResponseEntity.status(404).body(Map.of("code", code, "message", e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> detail(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(service.detail(id));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(404).body(Map.of("code", ErrorCodes.RESOURCE_NOT_FOUND, "message", e.getMessage()));
    }
  }
}
