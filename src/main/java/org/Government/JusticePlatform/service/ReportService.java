package org.Government.JusticePlatform.service;

import org.Government.JusticePlatform.model.Report;
import org.Government.JusticePlatform.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Optional<Report> getReportById(Integer reportId) {
        return reportRepository.findById(reportId);
    }

    public List<Report> getReportsByUserId(Integer userId) {
        return reportRepository.findByUserId(userId);
    }

    public List<Report> getReportsByArticleId(Integer articleId) {
        return reportRepository.findByArticle_ArticleId(articleId);
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public Report updateReport(Integer reportId, Report updatedReport) {
        return reportRepository.findById(reportId)
                .map(existingReport -> {
                    existingReport.setStatus(updatedReport.getStatus());
                    existingReport.setUpdatedAt(new java.util.Date());
                    return reportRepository.save(existingReport);
                }).orElse(null);
    }

    public boolean deleteReport(Integer reportId) {
        if (reportRepository.existsById(reportId)) {
            reportRepository.deleteById(reportId);
            return true;
        }
        return false;
    }
}
