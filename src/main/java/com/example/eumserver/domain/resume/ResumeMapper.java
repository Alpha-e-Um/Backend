package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeMapper {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    @Mapping(source = "jobCategory", target = "jobCategory")
    @Mapping(source = "jobSubcategory", target = "jobSubcategory")
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "gpa", target = "gpa")
    @Mapping(source = "totalScore", target = "totalScore")
    @Mapping(source = "introduction", target = "introduction")
    @Mapping(source = "careers", target = "careers")
    @Mapping(source = "certificates", target = "certificates")
    @Mapping(source = "projects", target = "projects")
    @Mapping(source = "isPublic", target = "isPublic")
    Resume resumeRequestToResume(ResumeRequest resumeRequest);

    @AfterMapping
    default void linkResume(ResumeRequest resumeRequest, @MappingTarget Resume resume) {
        if (resume.getActivities() != null) {
            resume.getActivities().forEach(resumeActivity -> resumeActivity.setResume(resume));
        }
        if (resume.getCareers() != null) {
            resume.getCareers().forEach(resumeCareer -> resumeCareer.setResume(resume));
        }
        if (resume.getCertificates() != null) {
            resume.getCertificates().forEach(resumeCertificate -> resumeCertificate.setResume(resume));
        }
        if (resume.getHomepages() != null) {
            resume.getHomepages().forEach(resumeHomepage -> resumeHomepage.setResume(resume));
        }
        if (resume.getProjects() != null) {
            resume.getProjects().forEach(resumeProject -> resumeProject.setResume(resume));
        }
    }
}
