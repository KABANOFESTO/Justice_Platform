package org.Government.JusticePlatform.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.Government.JusticePlatform.model.Report;
import org.Government.JusticePlatform.model.Article;
import org.Government.JusticePlatform.service.ReportService;
import org.Government.JusticePlatform.controller.ReportController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ReportControllerTests {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    private Report testReport1;
    private Report testReport2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testReport1 = new Report();
        testReport1.setReportId(1);
        testReport1.setReason("This is test report 1");
        Article article1 = new Article();
        article1.setTitle("article1");
        testReport1.setArticle(article1);

        testReport2 = new Report();
        testReport2.setReportId(1);
        testReport2.setReason("This is test report 1");
        Article article2 = new Article();
        article1.setTitle("article2");
        testReport1.setArticle(article2);
    }

    @Test
    public void testGetAllReports() {
        // Arrange
        List<Report> expectedReports = Arrays.asList(testReport1, testReport2);
        when(reportService.getAllReports()).thenReturn(expectedReports);

        // Act
        List<Report> actualReports = reportController.getAllReports();

        // Assert
        assertEquals(expectedReports, actualReports);
        assertEquals(2, actualReports.size());
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    public void testGetAllReports_EmptyList() {
        // Arrange
        when(reportService.getAllReports()).thenReturn(Collections.emptyList());

        // Act
        List<Report> actualReports = reportController.getAllReports();

        // Assert
        assertTrue(actualReports.isEmpty());
        verify(reportService, times(1)).getAllReports();
    }

    @Test
    public void testGetReportById_Found() {
        // Arrange
        Integer reportId = 1;
        when(reportService.getReportById(reportId)).thenReturn(Optional.of(testReport1));

        // Act
        ResponseEntity<Report> response = reportController.getReportById(reportId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testReport1, response.getBody());
        verify(reportService, times(1)).getReportById(reportId);
    }

    @Test
    public void testGetReportById_NotFound() {
        // Arrange
        Integer reportId = 999;
        when(reportService.getReportById(reportId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Report> response = reportController.getReportById(reportId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(reportService, times(1)).getReportById(reportId);
    }

    @Test
    public void testCreateReport() {
        // Arrange
        Report newReport = new Report();
        newReport.setReason("New Description");
        newReport.setArticle(null);

        Report savedReport = new Report();
        savedReport.setReportId(3);
        savedReport.setReason("New Description");
        Article article3 = new Article();
        article3.setTitle("article3");
        savedReport.setArticle(article3);

        when(reportService.createReport(newReport)).thenReturn(savedReport);

       
        Report result = reportController.createReport(newReport);

   
        assertNotNull(result);
        assertEquals(3, result.getReportId());
        assertEquals(newReport.getReason(), result.getReason());
       
        assertNotNull(result.getArticle());
       
        assertEquals("article3", result.getArticle().getTitle());
        verify(reportService, times(1)).createReport(newReport);
    }

    @Test
    public void testUpdateReport_Success() {
      
        Integer reportId = 1;
        Report updatedReport = new Report();
        updatedReport.setReason("Updated Description");
        Article updatedArticle = new Article();
        updatedArticle.setTitle("Updated Article");
        updatedReport.setArticle(updatedArticle);

        Report returnedReport = new Report();
        returnedReport.setReportId(reportId); 
        returnedReport.setReason("Updated Description");
        
        returnedReport.setArticle(updatedArticle);

        when(reportService.updateReport(eq(reportId), any(Report.class))).thenReturn(returnedReport);

    
        ResponseEntity<Report> response = reportController.updateReport(reportId, updatedReport);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reportId, response.getBody().getReportId());
        assertEquals("Updated Description", response.getBody().getReason());
        verify(reportService, times(1)).updateReport(eq(reportId), any(Report.class));
    }

    @Test
    public void testUpdateReport_NotFound() {
      
        Integer reportId = 999;
        Report updatedReport = new Report();
        updatedReport.setReason("Updated Description");

        when(reportService.updateReport(eq(reportId), any(Report.class))).thenReturn(null);

       
        ResponseEntity<Report> response = reportController.updateReport(reportId, updatedReport);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(reportService, times(1)).updateReport(eq(reportId), any(Report.class));
    }

    @Test
    public void testDeleteReport_Success() {
       
        Integer reportId = 1;
        when(reportService.deleteReport(reportId)).thenReturn(true);

      
        ResponseEntity<Void> response = reportController.deleteReport(reportId);

     
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reportService, times(1)).deleteReport(reportId);
    }

    @Test
    public void testDeleteReport_NotFound() {
       
        Integer reportId = 999;
        when(reportService.deleteReport(reportId)).thenReturn(false);

    
        ResponseEntity<Void> response = reportController.deleteReport(reportId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reportService, times(1)).deleteReport(reportId);
    }
}
