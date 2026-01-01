package com.Snack_BE.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Snack_BE.Model.SystemReportEntity;
import com.Snack_BE.Service.SystemReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("system-report")
public class SystemReportController {
    private final SystemReportService systemReportService;

    @GetMapping("/all")
    public ResponseEntity<List<SystemReportEntity>> getAllResponseEntity() {
        return systemReportService.getAllSysReport();
    }
}
