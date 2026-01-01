package com.Snack_BE.Service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Snack_BE.Model.SystemReportEntity;
import com.Snack_BE.Repo.SystemReportRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SystemReportService {
    private final SystemReportRepo systemReportRepo;

    public ResponseEntity<List<SystemReportEntity>> getAllSysReport() {
        return ResponseEntity.status(200).body(systemReportRepo.findAll());
    }
}
