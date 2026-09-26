package com.generated.qualityTrace.controllers;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.generated.qualityTrace.constants.ErrorCodes;
import com.generated.qualityTrace.constructors.GaugeDtoFactory;
import com.generated.qualityTrace.models.Gauge;
import com.generated.qualityTrace.routes.GaugeRoutes;
import com.generated.qualityTrace.services.GaugeService;
import com.generated.qualityTrace.types.GaugePayload;
@RestController
@RequestMapping(GaugeRoutes.PATH)
public class GaugeController {
  private final GaugeService service;
  public GaugeController(GaugeService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() { return service.list(); }

  @PostMapping("/{gaugeNo}/recalibrate")
  public ResponseEntity<?> recalibrate(@PathVariable String gaugeNo, @RequestBody GaugePayload payload) {
    try {
      Gauge g = service.recalibrate(gaugeNo, payload.calibrationDueDate());
      return ResponseEntity.ok(GaugeDtoFactory.view(g, service.evaluate(g)));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(404).body(Map.of("code", ErrorCodes.GAUGE_NOT_FOUND, "message", e.getMessage()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("code", ErrorCodes.INVALID_GAUGE_PAYLOAD, "message", e.getMessage()));
    }
  }

  @PostMapping("/{gaugeNo}/disable")
  public ResponseEntity<?> disable(@PathVariable String gaugeNo) {
    try {
      Gauge g = service.disable(gaugeNo);
      return ResponseEntity.ok(GaugeDtoFactory.view(g, service.evaluate(g)));
    } catch (NoSuchElementException e) {
      return ResponseEntity.status(404).body(Map.of("code", ErrorCodes.GAUGE_NOT_FOUND, "message", e.getMessage()));
    }
  }
}
