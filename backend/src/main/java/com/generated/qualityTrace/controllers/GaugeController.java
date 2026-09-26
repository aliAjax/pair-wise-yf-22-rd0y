package com.generated.qualityTrace.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.qualityTrace.constructors.GaugeDtoFactory;
import com.generated.qualityTrace.routes.GaugeRoutes;
import com.generated.qualityTrace.services.GaugeService;
import com.generated.qualityTrace.types.GaugeCheckResult;
import com.generated.qualityTrace.types.GaugePayload;

@RestController
@RequestMapping(GaugeRoutes.PATH)
public class GaugeController {
  private final GaugeService service;

  public GaugeController(GaugeService service) { this.service = service; }

  @GetMapping
  public List<Map<String, Object>> list() { return service.list(); }

  @GetMapping("/{code}/status")
  public Map<String, Object> status(@PathVariable String code) {
    GaugeCheckResult check = service.check(code);
    return GaugeDtoFactory.toDto(check.gauge(), check.status());
  }

  @PostMapping("/{code}/recalibrate")
  public Map<String, Object> recalibrate(@PathVariable String code, @RequestBody(required = false) GaugePayload payload) {
    return service.recalibrate(code, payload);
  }
}
